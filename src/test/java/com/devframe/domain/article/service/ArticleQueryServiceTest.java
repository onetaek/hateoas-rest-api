package com.devframe.domain.article.service;

import com.devframe.domain.article.dto.proxy.ArticleProxy;
import com.devframe.domain.article.entity.Article;
import com.devframe.domain.article.exception.ArticleException;
import com.devframe.domain.article.repository.ArticleCommandRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.devframe.common.EntityCreator.createArticle;
import static com.devframe.common.LongGenerator.createRandomLongExcluding;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

@ActiveProfiles("test")
@SpringBootTest
class ArticleQueryServiceTest {

    @Autowired
    private ArticleQueryService articleQueryService;
    @Autowired
    private ArticleCommandRepository articleCommandRepository;

    @AfterEach
    void tearDown() {
        articleCommandRepository.deleteAllInBatch();
    }

    @DisplayName("게시글을 하나를 조회한다.")
    @Test
    void findById() {
        //given
        Article article1 = createArticle("title1", "content1", "writer1");
        Article article2 = createArticle("title2", "content2", "writer2");
        Article savedArticle1 = articleCommandRepository.save(article1);
        Article savedArticle2 = articleCommandRepository.save(article2);

        //when
        Article findArticle = articleCommandRepository.findById(savedArticle1.getId())
                .orElseThrow(ArticleException::notFound);

        //then
        assertThat(findArticle)
                .extracting("title","content","writer")
                .containsExactlyInAnyOrder("title1","content1","writer1");
    }

    @DisplayName("존재하지 않는 게시글을 조회하면 예외가 발생한다.")
    @Test
    void findByIdWithNotExistArticle() {
        //given
        Article article1 = createArticle("title1", "content1", "writer1");
        Article article2 = createArticle("title2", "content2", "writer2");
        Article savedArticle1 = articleCommandRepository.save(article1);
        Article savedArticle2 = articleCommandRepository.save(article2);

        //when //then
        assertThatThrownBy(() -> articleQueryService.findById(
                createRandomLongExcluding(savedArticle1.getId(), savedArticle2.getId())))
                .isInstanceOf(ArticleException.class)
                .hasMessage("게시판을 찾을 수 없습니다.");
    }

    @DisplayName("게시글을 3개 생성하고 조회하면 3개가 조회된다.")
    @Test
    void findAll() {
        //given
        Article article1 = createArticle("title1", "content1", "writer1");
        Article article2 = createArticle("title2", "content2", "writer2");
        Article article3 = createArticle("title3", "content3", "writer3");
        articleCommandRepository.saveAll(List.of(article1, article2, article3));

        //when
        List<ArticleProxy> articleProxies = articleQueryService.findAll();

        //then
        assertThat(articleProxies)
                .hasSize(3)
                .extracting("title","content","writer")
                .containsExactlyInAnyOrder(
                        tuple("title1", "content1", "writer1"),
                        tuple("title2", "content2", "writer2"),
                        tuple("title3", "content3", "writer3")
                );
    }

}