package com.devframe.domain.comment.controller;

import com.devframe.domain.comment.dto.proxy.CommentProxy;
import com.devframe.domain.comment.dto.response.CommentResponse;
import com.devframe.domain.comment.service.CommentCommandService;
import com.devframe.domain.comment.service.CommentQueryService;
import com.devframe.global.common.hateaos.BasicResponse;
import com.devframe.global.common.hateaos.CustomResponse;
import com.devframe.global.common.hateaos.CustomResponseEntity;
import com.devframe.global.common.hateaos.LinkBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
        List<CommentProxy> all = commentQueryService.findAllByArticleId(articleId);
        List<BasicResponse> collect = all.stream()
                .map(CommentResponse::fromProxy)
                .map(response ->
                        response.addLinks(LinkBuilder.crud("/articles/" + articleId + "/comments", response.getId()))
                ).toList();
        CustomResponseEntity commentList = CustomResponse.succeeded(collect).addLink(LinkBuilder.self("comment list"));
        return commentList;
    }

    @GetMapping
    public CustomResponseEntity findById() {

    }

}
