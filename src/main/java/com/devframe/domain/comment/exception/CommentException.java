package com.devframe.domain.comment.exception;

import com.devframe.global.error.BasicException;
import org.springframework.http.HttpStatus;

public class CommentException extends BasicException {
    public CommentException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public static CommentException notFound() {
        return new CommentException(
                "댓글을 찾을 수 없습니다.",
                HttpStatus.NOT_FOUND
        );
    }
}
