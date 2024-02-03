package com.devframe.domain.comment.service;

import com.devframe.domain.article.entity.Article;
import com.devframe.domain.article.repository.ArticleCommandRepository;
import com.devframe.domain.comment.dto.proxy.CommentProxy;
import com.devframe.domain.comment.entity.Comment;
import com.devframe.domain.comment.repository.CommentCommandRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.devframe.common.EntityCreator.createArticle;
import static com.devframe.common.EntityCreator.createComment;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@ActiveProfiles("test")
@SpringBootTest
class CommentQueryServiceTest {

    @Autowired
    private CommentQueryService commentQueryService;
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
    void findById() {
        //given
        Article article = createArticle("a title1", "a content1", "a writer1");
        Article savedArticle = articleCommandRepository.save(article);
        Comment comment = createComment("c title1", "c content1", "c writer1", savedArticle);
        Comment savedComment = commentCommandRepository.save(comment);

        //when
        CommentProxy findComment = commentQueryService.findById(savedComment.getId());

        //then
        assertThat(findComment)
                .extracting("title", "content", "writer")
                .containsExactlyInAnyOrder("c title1", "c content1", "c writer1");
    }

    @Test
    void findAllByArticleId() {
        //given
        Article article1 = createArticle("a title1", "a content1", "a writer1");
        Article article2 = createArticle("a title2", "a content2", "a writer2");
        Article savedArticle1 = articleCommandRepository.save(article1);
        Article savedArticle2 = articleCommandRepository.save(article2);
        Comment comment1 = createComment("c title1", "c content1", "c writer1", savedArticle1);
        Comment comment2 = createComment("c title2", "c content2", "c writer2", savedArticle2);
        Comment comment3 = createComment("c title3", "c content3", "c writer3", savedArticle2);
        commentCommandRepository.save(comment1);
        commentCommandRepository.save(comment2);
        commentCommandRepository.save(comment3);

        //when
        List<CommentProxy> findComments = commentQueryService.findAllByArticleId(savedArticle2.getId());

        //then
        assertThat(findComments).hasSize(2)
                .extracting("title","content","writer")
                .containsExactlyInAnyOrder(
                        tuple("c title2","c content2","c writer2"),
                        tuple("c title3","c content3","c writer3")
                );
    }

    @Test
    void findAll() {
        //given
        Article article1 = createArticle("a title1", "a content1", "a writer1");
        Article article2 = createArticle("a title2", "a content2", "a writer2");
        Article savedArticle1 = articleCommandRepository.save(article1);
        Article savedArticle2 = articleCommandRepository.save(article2);
        Comment comment1 = createComment("c title1", "c content1", "c writer1", savedArticle1);
        Comment comment2 = createComment("c title2", "c content2", "c writer2", savedArticle2);
        Comment comment3 = createComment("c title3", "c content3", "c writer3", savedArticle2);
        commentCommandRepository.save(comment1);
        commentCommandRepository.save(comment2);
        commentCommandRepository.save(comment3);

        //when
        List<CommentProxy> findComments = commentQueryService.findAll();

        //then
        assertThat(findComments).hasSize(3)
                .extracting("title","content","writer")
                .containsExactlyInAnyOrder(
                        tuple("c title1","c content1","c writer1"),
                        tuple("c title2","c content2","c writer2"),
                        tuple("c title3","c content3","c writer3")
                );
    }
}