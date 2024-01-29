package com.devframe.domain.comment.repository.expression;

import com.devframe.domain.comment.entity.QComment;
import com.querydsl.core.types.dsl.BooleanExpression;

public class CommentBooleanExpression {
    public static BooleanExpression eqArticleId(Long articleId) {
        return articleId == null ? null: QComment.comment.article.id.eq(articleId);
    }
}
