package com.devframe.domain.article.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ArticleUpdateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public static ArticleServiceUpdateRequest toServiceRequest(ArticleUpdateRequest request) {
        return ArticleServiceUpdateRequest.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }
}
