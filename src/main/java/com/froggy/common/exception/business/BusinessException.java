package com.froggy.common.exception.business;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class BusinessException extends RuntimeException {
    private final BusinessErrorCode businessErrorCode;
    private List<String> arguments = new ArrayList<>();

    public BusinessException(BusinessErrorCode businessErrorCode) {
        this.businessErrorCode = businessErrorCode;
    }

    public BusinessException(BusinessErrorCode businessErrorCode, List<?> arguments) {
        this.businessErrorCode = businessErrorCode;
        this.arguments = arguments.stream().map(Object::toString).toList();
    }

    /**
     * 오류 메시지를 찾기 위해 사용되는 검색 키를 반환한다.
     * 키에 매칭되는 오류 메시지에 플레이스 홀더가 있는 경우 해당 플레이스 홀더를 arguments가 가지고 있는 값으로 순서대로 치환한다.
     *
     * @return 오류 메시지를 찾기 위한 code
     */
    public String getErrorMessageKey() {
        return businessErrorCode.name();
    }

}

