package com.devframe.domain.article.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArticleServiceUpdateRequest {
    private String title;
    private String content;
    private String writer;
}
