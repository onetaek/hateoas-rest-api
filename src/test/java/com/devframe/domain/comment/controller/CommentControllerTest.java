package com.devframe.domain.comment.controller;

import com.devframe.domain.article.dto.request.ArticleCreateRequest;
import com.devframe.domain.article.entity.Article;
import com.devframe.domain.article.repository.ArticleCommandRepository;
import com.devframe.domain.comment.dto.request.CommentUpdateRequest;
import com.devframe.domain.comment.entity.Comment;
import com.devframe.domain.comment.repository.CommentCommandRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.devframe.common.EntityCreator.createArticle;
import static com.devframe.common.EntityCreator.createComment;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ArticleCommandRepository articleCommandRepository;
    @Autowired
    private CommentCommandRepository commentCommandRepository;

    @AfterEach
    void tearDown() {
        commentCommandRepository.deleteAll();
        articleCommandRepository.deleteAll();
    }

    @Test
    void findById() throws Exception {
        //given
        Article article = createArticle("a title", "a content", "a writer");
        articleCommandRepository.save(article);
        Comment comment = createComment("c title", "c content", "c writer", article);
        commentCommandRepository.save(comment);

        //when //then
        mockMvc.perform(get("/articles/{articleId}/comments/{commentId}",article.getId(),comment.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.id").value(comment.getId()))
                .andExpect(jsonPath("$.content.title").value("c title"))
                .andExpect(jsonPath("$.content.content").value("c content"))
                .andExpect(jsonPath("$.content.writer").value("c writer"))
                .andDo(print());
    }

    @Test
    void findAllByArticleId() throws Exception {
        //given
        Article article1 = createArticle("a title1", "a content1", "a writer1");
        Article article2 = createArticle("a title2", "a content2", "a writer2");
        Article savedArticle1 = articleCommandRepository.save(article1);
        Article savedArticle2 = articleCommandRepository.save(article2);
        Comment comment1 = createComment("c title1", "c content1", "c writer1", savedArticle1);
        Comment comment2 = createComment("c title2", "c content2", "c writer2", savedArticle2);
        Comment comment3 = createComment("c title3", "c content3", "c writer3", savedArticle2);
        Comment savedComment1 = commentCommandRepository.save(comment1);
        Comment savedComment2 = commentCommandRepository.save(comment2);
        Comment savedComment3 = commentCommandRepository.save(comment3);

        //when //then
        mockMvc.perform(get("/articles/{articleId}/comments",savedArticle2.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size", Matchers.is(2)))
                .andExpect(jsonPath("$.content[0].title").value("c title2"))
                .andExpect(jsonPath("$.content[0].content").value("c content2"))
                .andExpect(jsonPath("$.content[0].writer").value("c writer2"))
                .andExpect(jsonPath("$.content[1].title").value("c title3"))
                .andExpect(jsonPath("$.content[1].content").value("c content3"))
                .andExpect(jsonPath("$.content[1].writer").value("c writer3"))
                .andDo(print());
    }

    @Test
    void create() throws Exception {
        //given
        Article article = createArticle("a title", "a content", "a writer");
        articleCommandRepository.save(article);
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title("c title")
                .content("c content")
                .writer("c writer")
                .build();

        //when //then
        mockMvc.perform(post("/articles/{articleId}/comments",article.getId())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content.title").value("c title"))
                .andExpect(jsonPath("$.content.content").value("c content"))
                .andExpect(jsonPath("$.content.writer").value("c writer"))
                .andDo(print());
        List<Comment> comments = commentCommandRepository.findAll();
        assertThat(comments).hasSize(1)
                .extracting("title", "content", "writer")
                .containsExactlyInAnyOrder(
                        tuple("c title", "c content", "c writer")
                );
    }

    @Test
    void update() throws Exception {
        //given
        Article article = createArticle("a title", "a content", "a writer");
        articleCommandRepository.save(article);
        Comment comment = createComment("c title", "c content", "c writer", article);
        commentCommandRepository.save(comment);
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .title("c update title")
                .content("c update content")
                .writer("c update writer")
                .build();

        //when //then
        mockMvc.perform(patch("/articles/{articleId}/comments/{commentId}",article.getId(),comment.getId())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.title").value("c update title"))
                .andExpect(jsonPath("$.content.content").value("c update content"))
                .andExpect(jsonPath("$.content.writer").value("c update writer"))
                .andDo(print());
        List<Comment> comments = commentCommandRepository.findAll();
        assertThat(comments).hasSize(1)
                .extracting("title", "content", "writer")
                .containsExactlyInAnyOrder(
                        tuple("c update title", "c update content", "c update writer")
                );
    }

    @Test
    void deleteArticle() throws Exception {
        //given
        Article article = createArticle("a title", "a content", "a writer");
        articleCommandRepository.save(article);
        Comment comment = createComment("c title", "c content", "c writer", article);
        commentCommandRepository.save(comment);

        //when //then
        mockMvc.perform(delete("/articles/{articleId}/comments/{commentId}",article.getId(),comment.getId()))
                .andExpect(status().isNoContent())
                .andDo(print());
        List<Comment> comments = commentCommandRepository.findAll();
        assertThat(comments).isEmpty();
    }
}