package com.devframe.domain.comment.service;

import com.devframe.domain.comment.dto.proxy.CommentProxy;
import com.devframe.domain.comment.exception.CommentException;
import com.devframe.domain.comment.repository.CommentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryService {

    private final CommentQueryRepository commentQueryRepository;

    public CommentProxy findById(Long id) {
        return CommentProxy.fromEntity(
                commentQueryRepository.findById(id).orElseThrow(CommentException::notFound)
        );
    }

    public List<CommentProxy> findAllByArticleId(Long articleId) {
        return commentQueryRepository.findAllByArticleId(articleId)
                .stream()
                .map(CommentProxy::fromEntity)
                .toList();
    }

    public List<CommentProxy> findAll() {
        return commentQueryRepository.findAll()
                .stream()
                .map(CommentProxy::fromEntity)
                .toList();
    }
}
