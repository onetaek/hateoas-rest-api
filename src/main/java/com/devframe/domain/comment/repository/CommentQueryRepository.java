package com.devframe.domain.comment.repository;

import com.devframe.domain.comment.entity.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.devframe.domain.comment.entity.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<Comment> findById(@NotNull Long id) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(comment)
                        .where(comment.id.eq(id))
                        .fetchOne());
    }

    public List<Comment> findAll() {
        return jpaQueryFactory.selectFrom(comment)
                .fetch();
    }

}
