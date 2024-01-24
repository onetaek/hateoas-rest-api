package com.devframe.global.common.hateaos;

import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

@Getter
public class Link {
    private final String value;
    private final String href;
    private HttpMethod httpMethod;
    private MediaType mediaType;

    public Link(String value, String href, HttpMethod httpMethod, MediaType mediaType) {
        this.value = value;
        this.href = href;
        this.httpMethod = httpMethod;
        this.mediaType = mediaType;
    }

    public static Link of(String value, String href, HttpMethod httpMethod, MediaType mediaType) {
        return new Link(value, href, httpMethod, mediaType);
    }
}
