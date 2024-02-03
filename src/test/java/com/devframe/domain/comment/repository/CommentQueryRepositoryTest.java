package com.devframe.domain.comment.repository;

import com.devframe.domain.article.entity.Article;
import com.devframe.domain.article.repository.ArticleCommandRepository;
import com.devframe.domain.article.repository.ArticleQueryRepository;
import com.devframe.domain.comment.entity.Comment;
import com.devframe.domain.comment.exception.CommentException;
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
import static org.assertj.core.groups.Tuple.tuple;

@SpringBootTest
@ActiveProfiles("test")
class CommentQueryRepositoryTest {

    @Autowired
    private CommentQueryRepository commentQueryRepository;
    @Autowired
    private CommentCommandRepository commentCommandRepository;
    @Autowired
    private ArticleQueryRepository articleQueryRepository;
    @Autowired
    private ArticleCommandRepository articleCommandRepository;
//    Referential integrity constraint violation: "FK5YX0UPHGJC6IK6HB82KKW501Y: PUBLIC.COMMENT FOREIGN KEY(ARTICLE_ID) REFERENCES PUBLIC.ARTICLE(ID) (CAST(1 AS BIGINT))"; SQL statement:
//    delete from article where (article.is_deleted = false) [23503-224]
    @AfterEach
    void tearDown() {
        commentCommandRepository.deleteAllInBatch();
        articleCommandRepository.deleteAllInBatch();
    }

    @DisplayName("댓글을 단건으로 조회한다.")
    @Test
    void findById() {
        //given
        Article article = createArticle("a title1", "a content1", "a writer1");
        Article savedArticle = articleCommandRepository.save(article);
        Comment comment = createComment("c title1", "c content1", "c writer1", savedArticle);
        Comment savedComment = commentCommandRepository.save(comment);

        //when
        Comment findComment = commentQueryRepository.findById(savedComment.getId()).orElseThrow(CommentException::notFound);

        //expect
        assertThat(findComment)
                .extracting("title","content","writer",
                        "article.title", "article.content","article.writer")
                .containsExactlyInAnyOrder("c title1","c content1","c writer1",
                        "a title1","a content1","a writer1");
    }

    @DisplayName("댓글을 3개 생성하고, 모두 조회하면 3개가 조회된다.")
    @Test
    void findAll() {
        //given
        Article article = createArticle("a title1", "a content1", "a writer1");
        Article savedArticle = articleCommandRepository.save(article);
        Comment comment1 = createComment("c title1", "c content1", "c writer1", savedArticle);
        Comment comment2 = createComment("c title2", "c content2", "c writer2", savedArticle);
        Comment comment3 = createComment("c title3", "c content3", "c writer3", savedArticle);
        commentCommandRepository.save(comment1);
        commentCommandRepository.save(comment2);
        commentCommandRepository.save(comment3);

        //when
        List<Comment> findComments = commentQueryRepository.findAll();

        //then
        assertThat(findComments).extracting("title","content","writer",
                "article.title", "article.content","article.writer")
                .containsExactlyInAnyOrder(
                        tuple("c title1","c content1","c writer1", "a title1","a content1","a writer1"),
                        tuple("c title2","c content2","c writer2", "a title1","a content1","a writer1"),
                        tuple("c title3","c content3","c writer3", "a title1","a content1","a writer1")
                );
    }

    @DisplayName("게시글 ID로 댓글을 조회한다.")
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
        List<Comment> findComments = commentQueryRepository.findAllByArticleId(savedArticle2.getId());

        //then
        assertThat(findComments).hasSize(2)
                .extracting("title","content","writer",
                        "article.title", "article.content","article.writer")
                .containsExactlyInAnyOrder(
                        tuple("c title2","c content2","c writer2", "a title2","a content2","a writer2"),
                        tuple("c title3","c content3","c writer3", "a title2","a content2","a writer2")
                );
    }
}