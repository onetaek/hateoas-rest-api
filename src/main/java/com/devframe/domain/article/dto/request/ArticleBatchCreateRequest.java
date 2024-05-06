package com.devframe.domain.article.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ArticleBatchCreateRequest {
    @NotNull @Valid
    private List<ArticleCreateRequest> body;
}
