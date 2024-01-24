package com.devframe.domain.comment.dto.proxy;

import com.devframe.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentProxy {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public static CommentProxy fromEntity(Comment entity) {
        return CommentProxy.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .createdTime(entity.getCreatedTime())
                .modifiedTime(entity.getModifiedTime())
                .build();
    }
}
