package com.devframe.global.common.hateaos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class LinkBuilder {

    @Value("${frame.web.host}")
    private static String host;

    @Value("${frame.web.show-host}")
    private static Boolean showHost;

    public static Link of(String value, String uri, HttpMethod httpMethod, MediaType mediaType) {
        return Link.of(value, showHost ? host + uri : uri , httpMethod, mediaType);
    }

    public static Link read(String value, String uri) {
        return Link.of(value, showHost ? host + uri : uri , HttpMethod.GET, MediaType.APPLICATION_JSON);
    }

    public static Link create(String value, String uri) {
        return Link.of(value, showHost ? host + uri : uri , HttpMethod.POST, MediaType.APPLICATION_JSON);
    }

    public static Link update(String value, String uri) {
        return Link.of(value, showHost ? host + uri : uri , HttpMethod.PATCH, MediaType.APPLICATION_JSON);
    }

    public static Link delete(String value, String uri) {
        return Link.of(value, showHost ? host + uri : uri , HttpMethod.DELETE, MediaType.APPLICATION_JSON);
    }

    public static Link[] crud(String uri, Long id) {
        return Arrays.stream(LinkAction.values())
                .map(item -> {
                    String totalUri = LinkAction.CREATE == item ? uri : uri + id;
                    return Link.of(
                            item.getValue(),
                            showHost ? host + totalUri : totalUri, item.getHttpMethod(),
                            MediaType.APPLICATION_JSON);
                    }
                ).toArray(Link[]::new);
    }
}
