package com.devframe.common;

import com.devframe.domain.article.entity.Article;

public class EntityCreator {

    public static Article createArticle(String title3, String content3, String writer3) {
        return Article.builder()
                .title(title3)
                .content(content3)
                .writer(writer3)
                .build();
    }

}
