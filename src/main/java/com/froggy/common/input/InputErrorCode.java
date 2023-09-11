package com.froggy.common.input;

import com.froggy.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum InputErrorCode implements ErrorCode {
    INVALID_INPUT(HttpStatus.BAD_REQUEST),
    INVALID_INPUT_MIN(HttpStatus.BAD_REQUEST),
    INVALID_INPUT_MIN_COUNT(HttpStatus.BAD_REQUEST),
    INVALID_INPUT_MAX(HttpStatus.BAD_REQUEST),
    INVALID_INPUT_NOT_NULL(HttpStatus.BAD_REQUEST),
    INVALID_INPUT_NOT_NULL_SHOP_ID(HttpStatus.BAD_REQUEST),
    INVALID_INPUT_NOT_NULL_PRODUCT_ID(HttpStatus.BAD_REQUEST),
    INVALID_INPUT_NOT_NULL_OPTION_ID(HttpStatus.BAD_REQUEST),
    INVALID_INPUT_SIZE(HttpStatus.BAD_REQUEST),
    INVALID_INPUT_NOT_BLANK(HttpStatus.BAD_REQUEST),
    INVALID_INPUT_LENGTH(HttpStatus.BAD_REQUEST),
    INVALID_INPUT_PRICE(HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER_PICKUP_ORDER_AND_ONSITE_ORDER(HttpStatus.BAD_REQUEST),
    INVALID_SPECIFIC_CLOSED_DAYS(HttpStatus.BAD_REQUEST);

    private final HttpStatus httpStatus;

    @Override
    public String getName() {
        return getName();
    }
}

