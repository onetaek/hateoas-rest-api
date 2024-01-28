package com.devframe.domain.comment.dto;

import com.devframe.domain.comment.dto.request.CommentServiceUpdateRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentUpdateRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public static CommentServiceUpdateRequest toServiceRequest(CommentUpdateRequest request) {
        return CommentServiceUpdateRequest.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
    }

}
