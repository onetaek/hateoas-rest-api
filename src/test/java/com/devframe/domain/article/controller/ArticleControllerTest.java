package com.devframe.domain.article.controller;

import com.devframe.domain.article.dto.request.ArticleCreateRequest;
import com.devframe.domain.article.entity.Article;
import com.devframe.domain.article.repository.ArticleCommandRepository;
import com.devframe.domain.article.repository.ArticleQueryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.IntStream;

import static com.devframe.common.EntityCreator.createArticle;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ArticleCommandRepository articleCommandRepository;
    @Autowired
    private ArticleQueryRepository articleQueryRepository;

    @AfterEach
    void tearDown() {
        articleCommandRepository.deleteAll();
    }

    @DisplayName("게시글 단건을 조회한다.")
    @Test
    void findById() throws Exception {
        //given
        Article article = createArticle("title1", "content1", "writer1");
        Article savedArticle = articleCommandRepository.save(article);

        //when //then
        mockMvc.perform(get("/articles/{id}", savedArticle.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.id").value(savedArticle.getId()))
                .andExpect(jsonPath("$.content.title").value("title1"))
                .andExpect(jsonPath("$.content.content").value("content1"))
                .andExpect(jsonPath("$.content.writer").value("writer1"))
                .andDo(print());
    }

    @DisplayName("게시글 목록을 조회한다.")
    @Test
    void findAll() throws Exception {
        //given
        List<Article> articles = IntStream.range(1, 6)
                .mapToObj(i -> createArticle("title" + i, "content" + i, "writer" + i))
                .toList();
        articleCommandRepository.saveAll(articles);

        //when //then
        mockMvc.perform(get("/articles")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size", Matchers.is(5)))
                .andExpect(jsonPath("$.content[0].title").value("title1"))
                .andExpect(jsonPath("$.content[0].content").value("content1"))
                .andExpect(jsonPath("$.content[0].writer").value("writer1"))
                .andExpect(jsonPath("$.content[2].title").value("title3"))
                .andExpect(jsonPath("$.content[2].content").value("content3"))
                .andExpect(jsonPath("$.content[2].writer").value("writer3"))
                .andDo(print());
    }

    @DisplayName("게시글을 작성하면 DB에 값이 저장된다.")
    @Test
    void create() throws Exception {
        //given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title("title1")
                .content("content1")
                .writer("writer1")
                .build();

        //when //then
        mockMvc.perform(post("/articles")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content.title").value("title1"))
                .andExpect(jsonPath("$.content.content").value("content1"))
                .andExpect(jsonPath("$.content.writer").value("writer1"))
                .andDo(print());
        List<Article> findArticles = articleQueryRepository.findAll();
        assertThat(findArticles).hasSize(1)
                .extracting("title", "content", "writer")
                .containsExactlyInAnyOrder(
                        tuple("title1", "content1", "writer1")
                );
    }

    @DisplayName("게시글 제목, 내용 수정")
    @Test
    void update() throws Exception {
        //given
        Article article = createArticle("title1", "content1", "writer1");
        Article savedArticle = articleCommandRepository.save(article);
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title("title update")
                .content("content update")
                .build();

        //when //then
        mockMvc.perform(patch("/articles/{id}", savedArticle.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.title").value("title update"))
                .andExpect(jsonPath("$.content.content").value("content update"))
                .andExpect(jsonPath("$.content.writer").value("writer1"))
                .andDo(print());
    }

    @DisplayName("게시글을 삭제하면 DB에 데이터가 존재하지 않는다.")
    @Test
    void deleteArticle() throws Exception {
        //given
        Article article = createArticle("title1", "content1", "writer1");
        Article savedArticle = articleCommandRepository.save(article);

        //when //then
        mockMvc.perform(delete("/articles/{id}", savedArticle.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
        List<Article> articles = articleQueryRepository.findAll();
        assertThat(articles).isEmpty();
    }

    @DisplayName("존재하지 않는 게시글 수정")
    @Test
    void updateNotExistArticle() throws Exception {
        //given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title("title update")
                .content("content update")
                .build();

        //when //then
        mockMvc.perform(patch("/articles/{id}", 1L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}