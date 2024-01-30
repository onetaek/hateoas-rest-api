package com.devframe.domain.comment.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentServiceUpdateRequest {
    private String title;
    private String content;
    private String writer;
}
