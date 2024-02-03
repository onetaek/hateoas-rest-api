package com.devframe.domain.article.service;

import com.devframe.domain.article.dto.proxy.ArticleProxy;
import com.devframe.domain.article.dto.request.ArticleServiceCreateRequest;
import com.devframe.domain.article.dto.request.ArticleServiceUpdateRequest;
import com.devframe.domain.article.entity.Article;
import com.devframe.domain.article.repository.ArticleCommandRepository;
import com.devframe.domain.article.repository.ArticleQueryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.devframe.common.EntityCreator.createArticle;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
class ArticleCommandServiceTest {

    @Autowired
    private ArticleQueryRepository articleQueryRepository;
    @Autowired
    private ArticleCommandRepository articleCommandRepository;
    @Autowired
    private ArticleCommandService articleCommandService;

    @AfterEach
    void tearDown() {
        articleCommandRepository.deleteAll();
    }

    @DisplayName("게시글을 생성한다. 전체 게시글의 개수는 1개이고 내용이 작성했을 때와 일치한다.")
    @Test
    void create() {
        //given
        ArticleServiceCreateRequest request = ArticleServiceCreateRequest.builder()
                .title("title1")
                .content("content1")
                .writer("writer1")
                .build();

        //when
        ArticleProxy articleProxy = articleCommandService.create(request);

        //then
        assertThat(articleProxy)
                .extracting("title","content","writer")
                .contains("title1","content1","writer1");

        List<Article> findArticles = articleQueryRepository.findAll();
        assertThat(findArticles).hasSize(1)
                .extracting("title","content","writer")
                .containsExactlyInAnyOrder(
                        tuple("title1","content1","writer1")
                );
    }

    @DisplayName("제목, 내용, 작성자 데이터를 수정한다.")
    @Test
    void update() {
        //given
        Article article = createArticle("title1", "content1", "writer1");
        Article savedArticle = articleCommandRepository.save(article);

        //when
        ArticleServiceUpdateRequest request = ArticleServiceUpdateRequest.builder()
                .title("update title")
                .content("update content")
                .writer("update writer")
                .build();
        ArticleProxy updatedArticle = articleCommandService.update(savedArticle.getId(), request);

        //then
        assertThat(updatedArticle)
                .extracting("title","content","writer")
                .containsExactlyInAnyOrder("update title","update content","update writer");
    }

    @DisplayName("수정할 값으로 제목만 넣으면 나머지 값은 유지된 채로 제목만 수정된다.")
    @Test
    void updateTitle() {
        //given
        Article article = createArticle("title1", "content1", "writer1");
        Article savedArticle = articleCommandRepository.save(article);

        //when
        ArticleServiceUpdateRequest request = ArticleServiceUpdateRequest.builder()
                .title("update title")
                .build();
        ArticleProxy updatedArticle = articleCommandService.update(savedArticle.getId(), request);

        //then
        assertThat(updatedArticle)
                .extracting("title","content","writer")
                .containsExactlyInAnyOrder("update title","content1","writer1");
    }

    @DisplayName("수정할 값으로 내용만 넣으면 나머지 값은 유지된 채로 내용만 수정된다.")
    @Test
    void updateContent() {
        //given
        Article article = createArticle("title1", "content1", "writer1");
        Article savedArticle = articleCommandRepository.save(article);

        //when
        ArticleServiceUpdateRequest request = ArticleServiceUpdateRequest.builder()
                .content("update content")
                .build();
        ArticleProxy updatedArticle = articleCommandService.update(savedArticle.getId(), request);

        //then
        assertThat(updatedArticle)
                .extracting("title","content","writer")
                .containsExactlyInAnyOrder("title1","update content","writer1");
    }

    @DisplayName("수정할 값으로 작성자만 넣으면 나머지 값은 유지된 채로 작성자만 수정된다.")
    @Test
    void updateWriter() {
        //given
        Article article = createArticle("title1", "content1", "writer1");
        Article savedArticle = articleCommandRepository.save(article);

        //when
        ArticleServiceUpdateRequest request = ArticleServiceUpdateRequest.builder()
                .writer("update writer")
                .build();
        ArticleProxy updatedArticle = articleCommandService.update(savedArticle.getId(), request);

        //then
        assertThat(updatedArticle)
                .extracting("title","content","writer")
                .containsExactlyInAnyOrder("title1","content1","update writer");
    }

    @DisplayName("3개의 데이터가 리스트에서 1개를 삭제하면 2개가 남는다.")
    @Test
    void delete() {
        //given
        Article article1 = createArticle("title1", "content1", "writer1");
        Article article2 = createArticle("title2", "content2", "writer2");
        Article article3 = createArticle("title3", "content3", "writer3");
        articleCommandRepository.saveAll(List.of(article1, article2, article3));

        //when
        articleCommandService.delete(article2.getId());

        //then
        List<Article> findArticles = articleQueryRepository.findAll();
        assertThat(findArticles).hasSize(2)
                .extracting("title","content","writer")
                .containsExactlyInAnyOrder(
                        tuple("title1", "content1", "writer1"),
                        tuple("title3", "content3", "writer3")
                );
    }
}