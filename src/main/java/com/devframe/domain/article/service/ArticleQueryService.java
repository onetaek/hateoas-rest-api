package com.devframe.domain.article.service;

import com.devframe.domain.article.dto.proxy.ArticleProxy;
import com.devframe.domain.article.exception.ArticleException;
import com.devframe.domain.article.repository.ArticleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleQueryService {

    private final ArticleQueryRepository articleQueryRepository;

    public ArticleProxy findById(Long id) {
        return ArticleProxy.fromEntity(
                articleQueryRepository.findById(id).orElseThrow(ArticleException::notFound)
        );
    }

    public List<ArticleProxy> findAll() {
        return articleQueryRepository.findAll().stream()
                .map(ArticleProxy::fromEntity)
                .collect(Collectors.toList());
    }
}
