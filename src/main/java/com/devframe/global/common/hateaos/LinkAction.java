package com.devframe.global.common.hateaos;

import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
public enum LinkAction {

    SELF("self", HttpMethod.GET),
    CREATE("create", HttpMethod.POST),
    UPDATE("update", HttpMethod.PATCH),
    DELETE("delete", HttpMethod.DELETE),
    ;

    private final String value;
    private final HttpMethod httpMethod;

    LinkAction(String value, HttpMethod httpMethod) {
        this.value = value;
        this.httpMethod = httpMethod;
    }
}
