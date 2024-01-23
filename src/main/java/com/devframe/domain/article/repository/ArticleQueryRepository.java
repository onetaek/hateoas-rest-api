package com.devframe.domain.article.repository;

import com.devframe.domain.article.entity.Article;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.devframe.domain.article.entity.QArticle.article;

@Repository
@RequiredArgsConstructor
public class ArticleQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<Article> findById(@NotNull Long id) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(article)
                        .where(article.id.eq(id))
                        .fetchOne());
    }

    public List<Article> findAll() {
        return jpaQueryFactory.selectFrom(article)
                .fetch();
    }
}
