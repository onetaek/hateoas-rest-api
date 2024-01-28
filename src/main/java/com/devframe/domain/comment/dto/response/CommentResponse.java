package com.devframe.domain.comment.dto.response;

import com.devframe.domain.comment.dto.proxy.CommentProxy;
import com.devframe.global.common.hateaos.BasicResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponse extends BasicResponse {

    private String title;
    private String content;
    private String writer;

    @Builder
    public CommentResponse(Long id, String title, String content, String writer) {
        super(id);
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    public static CommentResponse fromProxy(CommentProxy proxy) {
        return CommentResponse.builder()
                .id(proxy.getId())
                .title(proxy.getTitle())
                .content(proxy.getContent())
                .writer(proxy.getWriter())
                .build();
    }
}
