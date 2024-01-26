package com.devframe.global.common.hateaos;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class CustomResponseBody {

    private Boolean succeeded;
    private Map<String, Link> _links;

    public CustomResponseBody(Boolean succeeded) {
        this.succeeded = succeeded;
    }

    public void addLink(LinkProxy linkProxy) {
        this._links = Map.of(linkProxy.getValue(), linkProxy.getLink());
    }

    public void addLinks(LinkProxy... links) {
        this._links = Stream.of(links)
                .collect(Collectors.toMap(LinkProxy::getValue, LinkProxy::getLink));
    }
}
