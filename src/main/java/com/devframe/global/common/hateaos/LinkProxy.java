package com.devframe.global.common.hateaos;

import lombok.Getter;

@Getter
public class LinkProxy {

    private String value;
    private Link link;

    public LinkProxy(String value, Link link) {
        this.value = value;
        this.link = link;
    }

    public static LinkProxy of(String value, Link link) {
        return new  LinkProxy(value, link);
    }
}
