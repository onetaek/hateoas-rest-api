package com.devframe.global.common.hateaos;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomResponseBody {

    private Boolean succeeded;
    private Map<String, Link> _links;

    public CustomResponseBody(Boolean succeeded) {
        this.succeeded = succeeded;
    }



    public void addLink(LinkProxy linkProxy) {
        if (this._links == null || this._links.isEmpty()) {
            this._links = new HashMap<>(Map.of(linkProxy.getValue(), linkProxy.getLink()));
        } else {
            this._links.put(linkProxy.getValue(), linkProxy.getLink());
        }
    }

    public void addLinks(LinkProxy... links) {
        if (this._links == null || this._links.isEmpty()) {
            this._links = new HashMap<>();
        }
        for (LinkProxy linkProxy : links) {
            this._links.put(linkProxy.getValue(), linkProxy.getLink());
        }
    }
}
