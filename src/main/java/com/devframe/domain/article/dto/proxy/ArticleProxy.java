package com.devframe.domain.article.dto.proxy;

import com.devframe.domain.article.entity.Article;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ArticleProxy {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private Long views;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public static ArticleProxy fromEntity(Article entity) {
        if (entity == null) return null;
        return ArticleProxy.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .views(entity.getViews())
                .createdTime(entity.getCreatedTime())
                .modifiedTime(entity.getModifiedTime())
                .build();
    }
}
