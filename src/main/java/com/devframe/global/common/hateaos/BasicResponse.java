package com.devframe.global.common.hateaos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public void addLink(LinkProxy linkProxy) {
        this._links = Map.of(linkProxy.getValue(), linkProxy.getLink());
    }

    public BasicResponse addLinks(LinkProxy... links) {
        this._links = Stream.of(links)
                .collect(Collectors.toMap(LinkProxy::getValue, LinkProxy::getLink));
        return this;
    }
}
