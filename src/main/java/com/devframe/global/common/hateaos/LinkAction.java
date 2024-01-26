package com.devframe.global.common.hateaos;

import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
public enum LinkAction {

    SELF("self", HttpMethod.GET, "read"),
    CREATE("create", HttpMethod.POST ,"create"),
    UPDATE("update", HttpMethod.PATCH, "update"),
    DELETE("delete", HttpMethod.DELETE, "delete"),
    ;

    private final String value;
    private final HttpMethod httpMethod;
    private final String description;

    LinkAction(String value, HttpMethod httpMethod, String description) {
        this.value = value;
        this.httpMethod = httpMethod;
        this.description = description;
    }
}
