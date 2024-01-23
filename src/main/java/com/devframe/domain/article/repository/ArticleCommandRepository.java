package com.devframe.domain.article.repository;

import com.devframe.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCommandRepository extends JpaRepository<Article, Long> {
}
