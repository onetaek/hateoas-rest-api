package com.devframe.domain.article.repository;

import com.devframe.domain.article.entity.Article;
import com.devframe.domain.article.exception.ArticleException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.devframe.common.EntityCreator.createArticle;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@SpringBootTest
@ActiveProfiles("test")
class ArticleQueryRepositoryTest {

    @Autowired
    private ArticleQueryRepository articleQueryRepository;
    @Autowired
    private ArticleCommandRepository articleCommandRepository;

    @AfterEach
    void tearDown() {
        articleCommandRepository.deleteAllInBatch();
    }

    @DisplayName("게시글 2개 저장 후 하나의 게시글의 id로 조회한다.")
    @Test
    void findById() {
        //given
        Article article1 = createArticle("title1", "content1", "writer1");
        Article article2 = createArticle("title2", "content2", "writer2");
        Article savedArticle1 = articleCommandRepository.save(article1);
        Article savedArticle2 = articleCommandRepository.save(article2);

        //when
        Article findArticle = articleQueryRepository.findById(savedArticle1.getId())
                .orElseThrow(ArticleException::notFound);

        //then
        assertThat(findArticle)
                .extracting("title", "content", "writer")
                .containsExactlyInAnyOrder("title1", "content1", "writer1");
    }

    @DisplayName("게시글 3개를 저장 후 모든 게시글을 조회한다.")
    @Test
    void findAll() {
        //given
        Article article1 = createArticle("title1", "content1", "writer1");
        Article article2 = createArticle("title2", "content2", "writer2");
        Article article3 = createArticle("title3", "content3", "writer3");
        articleCommandRepository.saveAll(List.of(article1, article2, article3));

        //when
        List<Article> findArticles = articleQueryRepository.findAll();

        //then
        assertThat(findArticles)
                .hasSize(3)
                .extracting("title", "content", "writer")
                .containsExactlyInAnyOrder(
                        tuple("title1", "content1", "writer1"),
                        tuple("title2", "content2", "writer2"),
                        tuple("title3", "content3", "writer3")
                );
    }
}