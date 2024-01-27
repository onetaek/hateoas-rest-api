package com.devframe.domain.article.dto.response;

import com.devframe.domain.article.dto.proxy.ArticleProxy;
import com.devframe.global.common.hateaos.BasicResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleResponse extends BasicResponse {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private Long views;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public static ArticleResponse fromProxy(ArticleProxy proxy) {
        if (proxy == null) return null;
        return ArticleResponse.builder()
                .id(proxy.getId())
                .title(proxy.getTitle())
                .content(proxy.getContent())
                .writer(proxy.getWriter())
                .views(proxy.getViews())
                .createdTime(proxy.getCreatedTime())
                .modifiedTime(proxy.getModifiedTime())
                .build();
    }
}
