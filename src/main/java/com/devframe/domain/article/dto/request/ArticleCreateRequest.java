package com.devframe.domain.article.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ArticleCreateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String writer;
    public static ArticleServiceCreateRequest toServiceRequest(ArticleCreateRequest request) {
        return ArticleServiceCreateRequest.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .writer(request.getWriter())
                .build();
    }
}
