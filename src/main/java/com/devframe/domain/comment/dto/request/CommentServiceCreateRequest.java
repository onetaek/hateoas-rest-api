package com.devframe.domain.comment.dto.request;

import com.devframe.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentServiceCreateRequest {

    private String title;
    private String content;
    private String writer;

    public static Comment toEntity(CommentServiceCreateRequest request) {
        return Comment.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .writer(request.getWriter())
                .build();
    }
}
