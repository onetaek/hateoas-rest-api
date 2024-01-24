package com.devframe.domain.comment.service;

import com.devframe.domain.comment.dto.proxy.CommentProxy;
import com.devframe.domain.comment.dto.request.CommentServiceCreateRequest;
import com.devframe.domain.comment.dto.request.CommentServiceUpdateRequest;
import com.devframe.domain.comment.entity.Comment;
import com.devframe.domain.comment.exception.CommentException;
import com.devframe.domain.comment.repository.CommentCommandRepository;
import com.devframe.domain.comment.repository.CommentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentCommandService {

    private final CommentCommandRepository commentCommandRepository;
    private final CommentQueryRepository commentQueryRepository;

    public CommentProxy create(CommentServiceCreateRequest request) {
        return CommentProxy.fromEntity(
                commentCommandRepository.save(
                        CommentServiceCreateRequest.toEntity(request)
                )
        );
    }

    public CommentProxy update(Long id, CommentServiceUpdateRequest request) {
        Comment updateTarget = commentQueryRepository.findById(id).orElseThrow(CommentException::notFound);
        return CommentProxy.fromEntity(
                updateTarget.update(
                        request.getTitle(),
                        request.getContent()
                )
        );
    }

    public void delete(Long id) {
        Comment deleteTarget = commentQueryRepository.findById(id).orElseThrow(CommentException::notFound);
        commentCommandRepository.delete(deleteTarget);
    }


}
