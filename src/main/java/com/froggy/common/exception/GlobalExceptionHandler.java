package com.froggy.common.exception;


import com.froggy.common.CommonResponse;
import com.froggy.common.exception.business.BusinessErrorCode;
import com.froggy.common.exception.business.BusinessException;
import com.froggy.common.input.InputErrorCode;
import com.froggy.common.input.InputException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;

@Slf4j
//TODO: order에만 임시 적용
@RestControllerAdvice(basePackages = "com.tmax.commerce.focus.order")
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ConstraintViolationResolver violationMessageResolver;
    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> exceptionHandler(Exception e) {
        log.error("INTERNAL SERVER ERROR: ", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.fail(e.getCause().getMessage(), BusinessErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<CommonResponse<String>> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("ConstraintViolationException: ", e);

        InputErrorCode inputErrorCode = violationMessageResolver.getErrorCode(e);
        List<String> errorMessage = violationMessageResolver.getErrorMessageAndPath(e);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail(errorMessage.get(0), inputErrorCode, errorMessage.get(1)));
    }

    @ExceptionHandler(InputException.class)
    ResponseEntity<CommonResponse<Void>> handleInputException(InputException e) {
        String errorMessage = getErrorMessage(e.getErrorMessageKey(), e.getArguments());
        log.error("InputException -> errorCode: {}, errorMessage: {}", e.getInputErrorCode(), errorMessage);

        InputErrorCode inputErrorCode = e.getInputErrorCode();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail(errorMessage, inputErrorCode));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonResponse<Void>> businessExceptionHandler(BusinessException e) {
        String errorMessage = getErrorMessage(e.getErrorMessageKey(), e.getArguments());
        log.error("BusinessException -> errorCode: {}, errorMessage: {}", e.getBusinessErrorCode(), errorMessage);

        BusinessErrorCode businessErrorCode = e.getBusinessErrorCode();
        return ResponseEntity
                .status(businessErrorCode.getHttpStatus())
                .body(CommonResponse.fail(errorMessage, businessErrorCode));
    }

    private String getErrorMessage(String errorCode, List<String> arguments) {
        return messageSource.getMessage(
                errorCode,
                arguments.toArray(),
                "해당 키와 매칭되는 예외 메시지를 찾을 수 없습니다. 메시지 파일에서 예외 메시지를 설정해 주세요. \n" +
                        "해당 메시지를 읽은 사용자는 관련 백엔드 담당자에게 알려주시면 감사하겠습니다",
                Locale.getDefault());
    }
}
