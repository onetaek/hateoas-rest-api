package com.devframe.domain.article.dto.request;

import com.devframe.domain.article.entity.Article;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArticleServiceCreateRequest {
    private String title;
    private String content;
    private String writer;
    public static Article toEntity(ArticleServiceCreateRequest request) {
        return Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .writer(request.getWriter())
                .build();
    }
}
