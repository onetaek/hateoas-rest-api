package com.devframe.global.common.hateaos;

import org.springframework.http.HttpStatus;

import java.util.Collection;

public class CustomResponse {

    public static <T extends BasicResponse> CustomResponseEntity created(T basicResponse) {
        return CustomResponseEntity.created(new CustomSingleResponseBody(basicResponse));
    }

    public static <T extends BasicResponse> CustomResponseEntity succeeded(T basicResponse) {
        return CustomResponseEntity.succeeded(new CustomSingleResponseBody(basicResponse));
    }

    public static CustomResponseEntity noContent() {
        return CustomResponseEntity.of(HttpStatus.NO_CONTENT);
    }

    public static <T extends BasicResponse> CustomResponseEntity of(T basicResponse, HttpStatus httpStatus) {
        return CustomResponseEntity.of(new CustomSingleResponseBody(basicResponse), httpStatus);
    }

    public static <T extends BasicResponse> CustomResponseEntity error(T basicResponse, HttpStatus httpStatus) {
        return CustomResponseEntity.error(new CustomSingleResponseBody(basicResponse), httpStatus);
    }

    public static <C extends Collection<? extends BasicResponse>> CustomResponseEntity succeeded(C basicResponseList) {
        return CustomResponseEntity.succeeded(new CustomCollectionResponseBody<>(basicResponseList));
    }
}
