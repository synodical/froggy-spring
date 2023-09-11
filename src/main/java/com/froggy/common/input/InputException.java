package com.froggy.common.input;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class InputException extends RuntimeException {
    private final InputErrorCode inputErrorCode;
    private List<String> arguments = new ArrayList<>();

    public InputException(InputErrorCode inputErrorCode) {
        this.inputErrorCode = inputErrorCode;
    }

    public InputException(InputErrorCode inputErrorCode, List<?> arguments) {
        this.inputErrorCode = inputErrorCode;
        this.arguments = arguments.stream().map(Object::toString).collect(Collectors.toList());
    }

    /**
     * 오류 메시지를 찾기 위해 사용되는 검색 키를 반환한다.
     *
     * @return 오류 메시지를 찾기 위한 Message key
     */
    public String getErrorMessageKey() {
        return inputErrorCode.name();
    }

}

