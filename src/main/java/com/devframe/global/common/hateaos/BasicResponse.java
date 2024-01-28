package com.devframe.global.common.hateaos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * HttpMediaTypeNotAcceptableException
 * https://taengsw.tistory.com/49
 *
 * com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.springframework.http.HttpMethod and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.devframe.global.common.hateaos.CustomSingleResponseBody["content"]->com.devframe.domain.article.dto.response.ArticleResponse["_links"]->java.util.ImmutableCollections$Map1["create"]->com.devframe.global.common.hateaos.Link["httpMethod"])
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasicResponse {
    private Long id;
    private Map<String, Link> _links;

    public BasicResponse() {}

    public BasicResponse(Long id) {
        this.id = id;
    }

    public BasicResponse addLink(LinkProxy linkProxy) {
        if (this._links == null || this._links.isEmpty()) {
            this._links = new HashMap<>(Map.of(linkProxy.getValue(), linkProxy.getLink()));
        } else {
            this._links.put(linkProxy.getValue(), linkProxy.getLink());
        }
        return this;
    }

    public BasicResponse addLinks(LinkProxy... links) {
        if (this._links == null || this._links.isEmpty()) {
            this._links = Arrays.stream(links)
                    .collect(Collectors.toMap(LinkProxy::getValue, LinkProxy::getLink));
        } else {
            Arrays.stream(links)
                    .forEach(linkProxy -> this._links.put(linkProxy.getValue(), linkProxy.getLink()));
        }
        return this;
    }
}
