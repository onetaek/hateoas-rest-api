package com.devframe.domain.article.controller;

import com.devframe.domain.article.dto.proxy.ArticleProxy;
import com.devframe.domain.article.dto.request.ArticleBatchCreateRequest;
import com.devframe.domain.article.dto.request.ArticleCreateRequest;
import com.devframe.domain.article.dto.request.ArticleUpdateRequest;
import com.devframe.domain.article.dto.response.ArticleResponse;
import com.devframe.domain.article.service.ArticleCommandService;
import com.devframe.domain.article.service.ArticleQueryService;
import com.devframe.global.aop.ListValid;
import com.devframe.global.common.hateaos.CustomResponse;
import com.devframe.global.common.hateaos.CustomResponseEntity;
import com.devframe.global.common.hateaos.LinkBuilder;
import com.devframe.global.common.hateaos.LinkProxy;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.devframe.domain.article.controller.ArticleController.REQUEST_URI;

@Controller
@RequiredArgsConstructor
@RequestMapping(REQUEST_URI)
public class ArticleController {

    public final static String REQUEST_URI = "/articles";

    private final ArticleCommandService articleCommandService;
    private final ArticleQueryService articleQueryService;

    @GetMapping
    public CustomResponseEntity findAll() {
        return CustomResponse.succeeded(
                articleQueryService.findAll().stream()
                        .map(ArticleResponse::fromProxy)
                        .peek(response -> {
                            LinkProxy[] crudLinks = LinkBuilder.crud(REQUEST_URI, response.getId());
                            String commentsUri = String.format("%s/%s/comments", REQUEST_URI, response.getId());
                            LinkProxy commentsLink = LinkBuilder.of("comments", commentsUri);
                            response.addLinks(commentsLink);
                            response.addLinks(crudLinks);
                        })
                        .toList()
        ).addLink(LinkBuilder.self(REQUEST_URI, "article list"));
    }

    @GetMapping("/{id}")
    public CustomResponseEntity findById(@PathVariable Long id) {
        return CustomResponse.succeeded(
                ArticleResponse.fromProxy(
                        articleQueryService.findById(id)
                ).addLinks(LinkBuilder.crud(REQUEST_URI, id))
        );
    }

    @PostMapping
    public CustomResponseEntity create(@Validated @RequestBody ArticleCreateRequest request) {
        ArticleResponse articleResponse = ArticleResponse.fromProxy(
                articleCommandService.create(ArticleCreateRequest.toServiceRequest(request)));
        return CustomResponse.created(articleResponse)
                .addLink(LinkBuilder.self(REQUEST_URI + "/" + articleResponse.getId()));
    }

    @ListValid
    @PostMapping("/batch")
    public CustomResponseEntity createBatch(@RequestBody List<ArticleCreateRequest> requests) {
        List<ArticleProxy> articleProxies = articleCommandService.createBatch(requests
                .stream()
                .map(ArticleCreateRequest::toServiceRequest)
                .toList());
        List<ArticleResponse> articleResponse = articleProxies
                .stream()
                .map(ArticleResponse::fromProxy)
                .toList();
        return CustomResponse.succeeded(articleResponse);
    }

    @PostMapping("/batch2")
    public CustomResponseEntity createBatch2(@RequestBody List<ArticleCreateRequest> requests) {
        List<ArticleProxy> articleProxies = articleCommandService.createBatch(requests
                .stream()
                .map(ArticleCreateRequest::toServiceRequest)
                .toList());
        List<ArticleResponse> articleResponse = articleProxies
                .stream()
                .map(ArticleResponse::fromProxy)
                .toList();
        return CustomResponse.succeeded(articleResponse);
    }

    /**
     * spring validation 이 동작하는 케이스
     */
    @PostMapping("/batch3")
    public CustomResponseEntity createBatch3(@Valid @RequestBody ArticleBatchCreateRequest request) {
        List<ArticleProxy> articleProxies = articleCommandService.createBatch(request.getBody()
                .stream()
                .map(ArticleCreateRequest::toServiceRequest)
                .toList());
        List<ArticleResponse> articleResponse = articleProxies
                .stream()
                .map(ArticleResponse::fromProxy)
                .toList();
        return CustomResponse.succeeded(articleResponse);
    }

    @PatchMapping("/{id}")
    public CustomResponseEntity update(@PathVariable Long id, @Validated @RequestBody ArticleUpdateRequest request) {
        return CustomResponse.succeeded(
                ArticleResponse.fromProxy(
                        articleCommandService.update(id , ArticleUpdateRequest.toServiceRequest(request))
                )
        ).addLink(LinkBuilder.self(REQUEST_URI + "/" + id));
    }

    @DeleteMapping("/{id}")
    public CustomResponseEntity delete(@PathVariable Long id) {
        articleCommandService.delete(id);
        return CustomResponse.noContent();
    }
}
