package com.froggy.common.exception;

import com.froggy.common.input.InputErrorCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.metadata.ConstraintDescriptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

import java.util.*;

/**
 * ConstraintViolationException을 ErrorCode 또는 ErrorMessage로 매핑해주는 역할을 한다.
 */
@Component
@RequiredArgsConstructor
public class ConstraintViolationResolver {
    private static final String INVALID_INPUT = "INVALID_INPUT";
    private final MessageSource messageSource;
    private final MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    /**
     * ConstraintViolationException에 매핑되는 ErrorCode를 반환한다.
     *
     * ConstraintViolationException에 여러 ConstraintViolation이 들어있는 경우 ConstraintViolation의 propertyPath를 기준으로
     * 오름차순 정렬된 첫 번째 원소를 선택한다. 선택된 ConstraintViolation과 매칭되는 ErrorCode를 MessageFile에서 찾는다.
     * 매칭되는 ErrorCode의 우선 순위는 아래와 같다.
     * 1. INVALID_INPUT_애노테이션이름_객체이름_필드이름
     * 2. INVALID_INPUT_애노테이션이름_필드이름
     * 3. INVALID_INPUT_애노테이션이름_필드타입
     * 4. INVALID_INPUT_애노테이션이름
     * 5. INVALID_INPUT
     * @param constraintViolationException
     * @return
     */
    public InputErrorCode getErrorCode(ConstraintViolationException constraintViolationException) {
        Objects.requireNonNull(constraintViolationException);

        Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();

        if (constraintViolations.isEmpty())
            throw new IllegalArgumentException();

        ConstraintViolation<?> constraintViolation = pickFirstViolation(constraintViolations);
        return getErrorCode(constraintViolation);
    }

    private static ConstraintViolation<?> pickFirstViolation(Set<ConstraintViolation<?>> constraintViolations) {
        TreeSet<ConstraintViolation<?>> sortedSet = new TreeSet<>(Comparator.comparing(c -> c.getPropertyPath().toString()));
        sortedSet.addAll(constraintViolations);
        return sortedSet.first();
    }

    private InputErrorCode getErrorCode(ConstraintViolation<?> constraintViolation) {
        ConstraintDescriptor<?> descriptor = constraintViolation.getConstraintDescriptor();
        String annotationName = getAnnotationName(descriptor);
        String propertyPath = getPropertyPath(constraintViolation.getPropertyPath());
        String rootBeanName = getRootBeanName(constraintViolation);

        String[] messageCodes = codesResolver.resolveMessageCodes(annotationName, rootBeanName, propertyPath, null);

        for (String code : messageCodes) {
            String messageCode = INVALID_INPUT + "_" + toScreamingSnakeCase(code);

            try {
                messageSource.getMessage(messageCode, null, Locale.getDefault());
                return InputErrorCode.valueOf(messageCode);
            } catch (NoSuchMessageException ignored) {
                // messageSource에서 찾을 수 없는 경우
            } catch (IllegalArgumentException illegalArgumentException) {
                // ErrorCode에서 찾을 수 없는 경우
            }
        }

        return InputErrorCode.INVALID_INPUT;
    }

    private String getPropertyPath(Object propertyPath) {
        return propertyPath.toString();
    }

    private String getAnnotationName(ConstraintDescriptor<?> descriptor) {
        String simpleName = descriptor.getAnnotation().annotationType().getSimpleName();
        return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
    }

    private String getRootBeanName(ConstraintViolation<?> violation) {
        String rootBeanName = violation.getRootBeanClass().getSimpleName();
        return rootBeanName.substring(0, 1).toLowerCase() + rootBeanName.substring(1);
    }

    private static String toScreamingSnakeCase(String string) {
        return string
                .replace('.', '_')
                .replaceAll("(.)(\\p{Upper})", "$1_$2")
                .toUpperCase();
    }

    /**
     * ConstraintViolationException에 매핑되는 에러 메시지를 반환한다.
     *
     * ConstraintViolationException에 여러 ConstraintViolation이 들어있는 경우 ConstraintViolation의 propertyPath를 기준으로
     * 오름차순 정렬된 첫 번째 원소를 선택한다. 선택된 ConstraintViolation과 매칭되는 ErrorCode를 MessageFile에서 찾는다.
     * 매칭되는 ErrorCode를의 우선 순위는 아래와 같다.
     * 1. INVALID_INPUT_애노테이션이름_객체이름_필드이름
     * 2. INVALID_INPUT_애노테이션이름_필드이름
     * 3. INVALID_INPUT_애노테이션이름_필드타입
     * 4. INVALID_INPUT_애노테이션이름
     * 5. INVALID_INPUT
     * 매칭 되는 ErrorCode를 찾으면 해당 되는 값을 에러 메시지로 반환한다.
     * @param constraintViolationException
     * @return 에러 메시지와 프로퍼티 패스를 반환한다.
     */
    public List<String> getErrorMessageAndPath(ConstraintViolationException constraintViolationException) {
        Objects.requireNonNull(constraintViolationException);

        Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();

        if (constraintViolations.isEmpty())
            throw new IllegalArgumentException();

        ConstraintViolation<?> constraintViolation = pickFirstViolation(constraintViolations);
        String errorMessage = getErrorMessageAndPath(constraintViolation);
        String path = constraintViolation.getPropertyPath().toString();

        return List.of(errorMessage, path);
    }

    private String getErrorMessageAndPath(ConstraintViolation constraintViolation) {
        ConstraintDescriptor<?> descriptor = constraintViolation.getConstraintDescriptor();
        Map<String, Object> attributes = descriptor.getAttributes();

        String annotationName = getAnnotationName(descriptor);
        String rootBeanName = getRootBeanName(constraintViolation);
        String propertyPath = getPropertyPath(constraintViolation.getPropertyPath());

        String[] codes = codesResolver.resolveMessageCodes(annotationName, rootBeanName, propertyPath, null);

        for (String code : codes) {
            String messageCode = INVALID_INPUT + "_" + toScreamingSnakeCase(code);

            try {
                String message = messageSource.getMessage(messageCode, null, Locale.getDefault());

                for (Map.Entry<String, Object> es : attributes.entrySet()) {
                    message = message.replace("{" + es.getKey() + "}", getPropertyPath(es.getValue()));
                }

                if (constraintViolation.getInvalidValue() != null)
                    message = message.replace("{inputValue}", getPropertyPath(constraintViolation.getInvalidValue()));

                return message;
            } catch (NoSuchMessageException ignored) {
            }
        }

        return messageSource.getMessage(INVALID_INPUT, null, Locale.getDefault());
    }

}