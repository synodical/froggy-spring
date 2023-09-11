package com.froggy.common;

import com.froggy.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema
@NoArgsConstructor
public class CommonResponse<T> {
    @Schema(description = "응답 메시지")
    private String message;

    @Schema(description = "에러 발생 시 에러 코드")
    private ErrorCode errorCode;

    @Schema(description = "에러 발생 시 에러 정보")
    private T data;

    @Builder
    public CommonResponse(String message, ErrorCode errorCode, T data) {
        this.message = message;
        this.errorCode = errorCode;
        this.data = data;
    }

    public static <T> CommonResponse<T> success(String message, T data) {
        return (CommonResponse<T>) CommonResponse.builder()
                .message(message)
                .data(data)
                .build();
    }

    public static <T> CommonResponse<T> success(String message) {
        return (CommonResponse<T>) CommonResponse.builder()
                .message(message)
                .build();
    }

    public static <T> CommonResponse<T> fail(String message, ErrorCode errorCode) {
        return (CommonResponse<T>) CommonResponse.builder()
                .message(message)
                .errorCode(errorCode)
                .build();
    }

    public static <T> CommonResponse<T> fail(ErrorCode errorCode, T data) {
        return (CommonResponse<T>) CommonResponse.builder()
                .errorCode(errorCode)
                .data(data)
                .build();
    }

    public static <T> CommonResponse<T> fail(String message, ErrorCode errorCode, T data) {
        return (CommonResponse<T>) CommonResponse.builder()
                .message(message)
                .errorCode(errorCode)
                .data(data)
                .build();
    }
}

