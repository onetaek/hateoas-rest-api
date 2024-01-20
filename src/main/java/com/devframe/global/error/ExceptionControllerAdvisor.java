package com.devframe.global.error;

import com.devframe.global.common.dto.response.CustomResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvisor {

    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CustomResponseEntity<CustomErrorResponse> invalidRequestHandler(MethodArgumentNotValidException e) {
        CustomErrorResponse response = CustomErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message("잘못된 요청입니다.")
                .build();

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        fieldErrors.forEach(fieldError -> {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return CustomResponseEntity.of(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BaseException.class)
    public CustomResponseEntity<CustomErrorResponse> rollBackException(BaseException e) throws IOException {
        if (isAjaxRequest(httpServletRequest)) {
            HttpStatus httpStatus = e.getHttpStatus();
            CustomErrorResponse body = CustomErrorResponse.builder()
                    .httpStatus(httpStatus)
                    .code(httpStatus.value())
                    .message(e.getMessage())
                    .build();
            return CustomResponseEntity.of(body, httpStatus);
        } else {
            httpServletResponse.sendError(e.getHttpStatus().value());
            return null;
        }
    }

    @ExceptionHandler(Exception.class)
    public CustomResponseEntity<CustomErrorResponse> unProcessableException(Exception e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        CustomErrorResponse body = CustomErrorResponse.builder()
                .httpStatus(httpStatus)
                .code(httpStatus.value())
                .message(String.format("관리자에게 문의하세요. %s",e.getMessage()))
                .build();
        return CustomResponseEntity.of(body, httpStatus);
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        String contentType = request.getHeader("Content-Type");
        String requestURI = request.getRequestURI();

        return (accept != null && accept.contains("application/json")) ||
                (contentType != null && contentType.contains("application/json")) ||
                requestURI.startsWith("/api");
    }

}
