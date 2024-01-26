package com.devframe.global.common.hateaos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Link {
    private final String href;
    private final HttpMethod httpMethod;
    private final String mediaType;
    private String description;

    public Link(String href, HttpMethod httpMethod, String mediaType, String description) {
        this.href = href;
        this.httpMethod = httpMethod;
        this.mediaType = mediaType;
        this.description = description;
    }

    public Link(String href, HttpMethod httpMethod, String mediaType) {
        this.href = href;
        this.httpMethod = httpMethod;
        this.mediaType = mediaType;
    }

    public static Link of(String href, HttpMethod httpMethod, String mediaType, String description) {
        return new Link(href, httpMethod, mediaType, description);
    }

    public static Link of(String href, HttpMethod httpMethod, String mediaType) {
        return new Link(href, httpMethod, mediaType);
    }


}
