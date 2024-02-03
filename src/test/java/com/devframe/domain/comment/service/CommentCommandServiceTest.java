package com.devframe.domain.comment.service;

import com.devframe.domain.article.entity.Article;
import com.devframe.domain.article.repository.ArticleCommandRepository;
import com.devframe.domain.comment.dto.proxy.CommentProxy;
import com.devframe.domain.comment.dto.request.CommentServiceCreateRequest;
import com.devframe.domain.comment.dto.request.CommentServiceUpdateRequest;
import com.devframe.domain.comment.entity.Comment;
import com.devframe.domain.comment.repository.CommentCommandRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.devframe.common.EntityCreator.createArticle;
import static com.devframe.common.EntityCreator.createComment;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
class CommentCommandServiceTest {

    @Autowired
    private CommentCommandService commentCommandService;
    @Autowired
    private ArticleCommandRepository articleCommandRepository;
    @Autowired
    private CommentCommandRepository commentCommandRepository;

    @AfterEach
    void tearDown() {
        commentCommandRepository.deleteAll();
        articleCommandRepository.deleteAll();
    }

    @DisplayName("댓글 작성")
    @Test
    void create() {
        //given
        Article article = createArticle("a title1", "a content1", "a writer1");
        Article savedArticle = articleCommandRepository.save(article);
        CommentServiceCreateRequest request = CommentServiceCreateRequest.builder()
                .title("c title1")
                .content("c content1")
                .writer("c writer1")
                .build();

        //when
        CommentProxy savedComment = commentCommandService.create(savedArticle.getId(), request);

        //then
        assertThat(savedComment)
                .extracting("title", "content", "writer")
                .containsExactlyInAnyOrder("c title1", "c content1", "c writer1");
    }

    @DisplayName("댓글 수정")
    @Test
    void update() {
        //given
        Article article = createArticle("a title1", "a content1", "a writer1");
        Article savedArticle = articleCommandRepository.save(article);
        Comment comment = createComment("c title1", "c content1", "c writer1", savedArticle);
        Comment savedComment = commentCommandRepository.save(comment);
        CommentServiceUpdateRequest request = CommentServiceUpdateRequest.builder()
                .title("c update title1")
                .content("c update content1")
                .writer("c update writer1")
                .build();

        //when
        CommentProxy updatedComment = commentCommandService.update(savedComment.getId(), request);

        //then
        assertThat(updatedComment)
                .extracting("title", "content", "writer")
                .containsExactlyInAnyOrder("c update title1", "c update content1", "c update writer1");
    }

    @DisplayName("댓글 삭제하면 댓글 테이블에 데이터가 존재하지 않는다.")
    @Test
    void delete() {
        //given
        Article article = createArticle("a title1", "a content1", "a writer1");
        Article savedArticle = articleCommandRepository.save(article);
        Comment comment1 = createComment("c title1", "c content1", "c writer1", savedArticle);
        Comment comment2 = createComment("c title2", "c content2", "c writer2", savedArticle);
        Comment comment3 = createComment("c title3", "c content3", "c writer3", savedArticle);
        Comment savedComment1 = commentCommandRepository.save(comment1);
        Comment savedComment2 = commentCommandRepository.save(comment2);
        Comment savedComment3 = commentCommandRepository.save(comment3);


        //when
        commentCommandService.delete(savedComment1.getId());

        //then
        List<Comment> findComments = commentCommandRepository.findAll();
        assertThat(findComments).hasSize(2)
                .extracting("title", "content", "writer")
                .containsExactlyInAnyOrder(
                        tuple("c title2", "c content2", "c writer2"),
                        tuple("c title3", "c content3", "c writer3")
                );
    }
}