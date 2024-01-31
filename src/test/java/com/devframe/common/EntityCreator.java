package com.devframe.common;

import com.devframe.domain.article.entity.Article;

public class EntityCreator {

    public static Article createArticle(String title, String content, String writer) {
        return Article.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .build();
    }

}
