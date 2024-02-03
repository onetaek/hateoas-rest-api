package com.devframe.common;

import com.devframe.domain.article.entity.Article;
import com.devframe.domain.comment.entity.Comment;

public class EntityCreator {

    public static Article createArticle(String title, String content, String writer) {
        return Article.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .build();
    }

    public static Comment createComment(String title, String content, String writer, Article article) {
        return Comment.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .article(article)
                .build();
    }
}
