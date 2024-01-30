package com.devframe.domain.comment.dto.request;

import lombok.Getter;

@Getter
public class CommentUpdateRequest {
    private String title;
    private String content;
    private String writer;

    public static CommentServiceUpdateRequest toServiceRequest(CommentUpdateRequest request) {
        return CommentServiceUpdateRequest.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .writer(request.getWriter())
                .build();
    }

}
