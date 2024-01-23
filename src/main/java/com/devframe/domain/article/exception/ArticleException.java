package com.devframe.domain.article.exception;

import com.devframe.global.error.BasicException;
import org.springframework.http.HttpStatus;

public class ArticleException extends BasicException {
    public ArticleException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public static ArticleException notFound() {
        return new ArticleException(
                "게시판을 찾을 수 없습니다.",
                HttpStatus.NOT_FOUND
        );
    }
}
