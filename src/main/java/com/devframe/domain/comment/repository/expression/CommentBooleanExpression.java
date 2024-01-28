package com.devframe.domain.comment.repository.expression;

import com.querydsl.core.types.dsl.BooleanExpression;

public class CommentBooleanExpression {
    public static BooleanExpression eqArticleId(Long articleId) {
        return articleId == null ? null:
                null;
    }
}
