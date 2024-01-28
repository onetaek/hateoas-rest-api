package com.devframe.global.common.hateaos;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Getter
@Component
public class LinkBuilder {

    private static String host;
    private static Boolean showHost;

    @Value("${frame.web.host}")
    public void setHost(String hostValue) {
        host = hostValue;
    }

    @Value("${frame.web.show-host}")
    public void setShowHost(Boolean showHostValue) {
        showHost = showHostValue;
    }

    public static LinkProxy of(String value ,String uri) {
        return LinkProxy.of(value, Link.of(handleUrl(uri) , HttpMethod.GET, MediaType.APPLICATION_JSON_VALUE));
    }

    public static LinkProxy of(String value ,String uri, HttpMethod httpMethod) {
        return LinkProxy.of(value, Link.of(handleUrl(uri) , httpMethod, MediaType.APPLICATION_JSON_VALUE));
    }

    public static LinkProxy of(String value ,String uri, HttpMethod httpMethod, String mediaType) {
        return LinkProxy.of(value, Link.of(handleUrl(uri) , httpMethod, mediaType));
    }

    public static LinkProxy of(String value ,String uri, HttpMethod httpMethod, String mediaType, String description) {
        return LinkProxy.of(value, Link.of(handleUrl(uri) , httpMethod, mediaType, description));
    }

    public static LinkProxy self(String uri) {
        return LinkProxy.of("self", Link.of(handleUrl(uri) , HttpMethod.GET, MediaType.APPLICATION_JSON_VALUE));
    }

    public static LinkProxy self(String uri, String description) {
        return LinkProxy.of("self", Link.of(handleUrl(uri) , HttpMethod.GET,
                MediaType.APPLICATION_JSON_VALUE ,description));
    }

    public static LinkProxy read(String uri) {
        return LinkProxy.of("read", Link.of(handleUrl(uri) , HttpMethod.GET,
                MediaType.APPLICATION_JSON_VALUE));
    }

    public static LinkProxy read(String uri, String description) {
        return LinkProxy.of("read", Link.of(handleUrl(uri) , HttpMethod.GET,
                MediaType.APPLICATION_JSON_VALUE, description));
    }

    public static LinkProxy create(String uri) {
        return LinkProxy.of("create", Link.of(handleUrl(uri) , HttpMethod.POST,
                MediaType.APPLICATION_JSON_VALUE));
    }

    public static LinkProxy create(String uri, String description) {
        return LinkProxy.of("create", Link.of(handleUrl(uri) , HttpMethod.POST,
                MediaType.APPLICATION_JSON_VALUE, description));
    }

    public static LinkProxy update(String uri) {
        return LinkProxy.of("update", Link.of(handleUrl(uri) , HttpMethod.PATCH,
                MediaType.APPLICATION_JSON_VALUE));
    }

    public static LinkProxy update(String uri, String description) {
        return LinkProxy.of("update", Link.of(handleUrl(uri) , HttpMethod.PATCH,
                MediaType.APPLICATION_JSON_VALUE, description));
    }

    public static LinkProxy delete(String uri) {
        return LinkProxy.of("delete", Link.of(handleUrl(uri) , HttpMethod.DELETE,
                MediaType.APPLICATION_JSON_VALUE));
    }

    public static LinkProxy delete(String uri, String description) {
        return LinkProxy.of("delete", Link.of(handleUrl(uri) , HttpMethod.DELETE,
                MediaType.APPLICATION_JSON_VALUE, description));
    }

    public static LinkProxy[] crud(String uri, Long id) {
        return Arrays.stream(LinkAction.values())
                .map(item -> {
                    String totalUri = LinkAction.CREATE == item ? uri : String.format("%s/%s",uri, id);
                    return LinkProxy.of(item.getValue(),Link.of(
                            showHost ? host + totalUri : totalUri, item.getHttpMethod(),
                            MediaType.APPLICATION_JSON_VALUE,
                            String.format("%s %s rest api", item.getDescription(), convertUri(uri)))
                    );
                }).toArray(LinkProxy[]::new);
    }

    private static String convertUri(String uri) {
        if (uri != null) {
            if (uri.startsWith("/")) {
                uri = uri.substring(1);
            }
            if (uri.endsWith("s")) {
                uri = uri.substring(0, uri.length() - 1);
            }
        }
        return uri;
    }

    private static String handleUrl(String uri) {
        return showHost ? host + uri : uri;
    }
}
