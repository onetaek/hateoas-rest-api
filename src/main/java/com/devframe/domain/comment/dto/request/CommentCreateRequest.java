package com.devframe.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentCreateRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String writer;

    public static CommentServiceCreateRequest toServiceRequest(CommentCreateRequest request) {
        return CommentServiceCreateRequest.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .writer(request.getWriter())
                .build();
    }

}
