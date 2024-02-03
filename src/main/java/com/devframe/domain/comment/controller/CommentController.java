package com.devframe.domain.comment.controller;

import com.devframe.domain.comment.dto.request.CommentCreateRequest;
import com.devframe.domain.comment.dto.request.CommentUpdateRequest;
import com.devframe.domain.comment.dto.response.CommentResponse;
import com.devframe.domain.comment.service.CommentCommandService;
import com.devframe.domain.comment.service.CommentQueryService;
import com.devframe.global.common.hateaos.CustomResponse;
import com.devframe.global.common.hateaos.CustomResponseEntity;
import com.devframe.global.common.hateaos.LinkBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.devframe.domain.comment.controller.CommentController.REQUEST_URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(REQUEST_URI)
public class CommentController {

    public final static String REQUEST_URI = "/articles/{articleId}/comments";

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;


    @GetMapping
    public CustomResponseEntity findAllByArticleId(@PathVariable Long articleId) {
        return CustomResponse.succeeded(
                commentQueryService.findAllByArticleId(articleId).stream()
                        .map(CommentResponse::fromProxy)
                        .map(response -> response.addLinks(
                                LinkBuilder.crud("/articles/" + articleId + "/comments", response.getId()))
                        ).toList()
                ).addLinks(
                        LinkBuilder.self("/articles/" + articleId + "/comments"),
                        LinkBuilder.create("/articles/" + articleId + "/comments")
                );
    }

    @GetMapping("/{id}")
    public CustomResponseEntity findById(@PathVariable Long articleId, @PathVariable Long id) {
        return CustomResponse.succeeded(
                CommentResponse.fromProxy(
                        commentQueryService.findById(id)
                ).addLinks(LinkBuilder.crud("/articles/" + articleId + "/comments", id)));
    }

    @PostMapping
    public CustomResponseEntity create(@PathVariable Long articleId,
                                       @Validated @RequestBody CommentCreateRequest request) {
        CommentResponse commentResponse = CommentResponse.fromProxy(
                commentCommandService.create(articleId, CommentCreateRequest.toServiceRequest(request)));
        return CustomResponse.created(
                commentResponse.addLinks(
                        LinkBuilder.self("/articles/" + articleId + "/comments/" + commentResponse.getId())
                )
        );
    }

    @PatchMapping("/{id}")
    public CustomResponseEntity update(@PathVariable Long articleId, @PathVariable Long id,
                                       @Validated @RequestBody CommentUpdateRequest request) {
        return CustomResponse.succeeded(CommentResponse.fromProxy(
                commentCommandService.update(id, CommentUpdateRequest.toServiceRequest(request)))
                .addLinks(LinkBuilder.self("/articles/" + articleId + "/comments/" + id)));
    }

    @DeleteMapping("/{id}")
    public CustomResponseEntity delete(@PathVariable Long articleId, @PathVariable Long id) {
        commentCommandService.delete(id);
        return CustomResponse.noContent();
    }

}
