package com.devframe.docs;

import com.devframe.common.RestDocsConfiguration;
import com.devframe.domain.article.dto.request.ArticleCreateRequest;
import com.devframe.domain.article.entity.Article;
import com.devframe.domain.article.repository.ArticleCommandRepository;
import com.devframe.domain.comment.dto.request.CommentUpdateRequest;
import com.devframe.domain.comment.entity.Comment;
import com.devframe.domain.comment.repository.CommentCommandRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.devframe.common.EntityCreator.createArticle;
import static com.devframe.common.EntityCreator.createComment;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ExtendWith(RestDocumentationExtension.class)
public class CommentControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentCommandRepository commentCommandRepository;

    @Autowired
    private ArticleCommandRepository articleCommandRepository;

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
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment-read",
                        pathParameters(
                                parameterWithName("articleId").description("게시글 ID"),
                                parameterWithName("commentId").description("댓글 ID")
                        ),
                        responseFields(
                                fieldWithPath("succeeded").description("성공 여부").type(BOOLEAN),
                                fieldWithPath("content.id").description("댓글 ID").type(NUMBER),
                                fieldWithPath("content.title").description("댓글 제목").type(STRING),
                                fieldWithPath("content.content").description("댓글 내용").type(STRING),
                                fieldWithPath("content.writer").description("댓글 작성자").type(STRING),
                                fieldWithPath("content._links.self.href").description("댓글 조회 API 링크").type(STRING),
                                fieldWithPath("content._links.self.httpMethod").description("댓글 조회 API HTTP 메서드").type(STRING),
                                fieldWithPath("content._links.self.mediaType").description("댓글 조회 API 미디어 타입").type(STRING),
                                fieldWithPath("content._links.self.description").description("댓글 조회 API 설명").type(STRING),
                                fieldWithPath("content._links.create.href").description("댓글 생성 API 링크").type(STRING),
                                fieldWithPath("content._links.create.httpMethod").description("댓글 생성 API HTTP 메서드").type(STRING),
                                fieldWithPath("content._links.create.mediaType").description("댓글 생성 API 미디어 타입").type(STRING),
                                fieldWithPath("content._links.create.description").description("댓글 생성 API 설명").type(STRING),
                                fieldWithPath("content._links.update.href").description("댓글 수정 API 링크").type(STRING),
                                fieldWithPath("content._links.update.httpMethod").description("댓글 수정 API HTTP 메서드").type(STRING),
                                fieldWithPath("content._links.update.mediaType").description("댓글 수정 API 미디어 타입").type(STRING),
                                fieldWithPath("content._links.update.description").description("댓글 수정 API 설명").type(STRING),
                                fieldWithPath("content._links.delete.href").description("댓글 삭제 API 링크").type(STRING),
                                fieldWithPath("content._links.delete.httpMethod").description("댓글 삭제 API HTTP 메서드").type(STRING),
                                fieldWithPath("content._links.delete.mediaType").description("댓글 삭제 API 미디어 타입").type(STRING),
                                fieldWithPath("content._links.delete.description").description("댓글 삭제 API 설명").type(STRING)
                        )
                ));

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
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment-read-by-article-id",
                        pathParameters(
                                parameterWithName("articleId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("succeeded").description("성공 여부"),
                                fieldWithPath("_links.self.href").description("현재 리소스에 대한 URL (GET 요청)"),
                                fieldWithPath("_links.self.httpMethod").description("self 링크의 HTTP 메서드 (GET)"),
                                fieldWithPath("_links.self.mediaType").description("self 링크의 미디어 타입 (application/json)"),
                                fieldWithPath("_links.create.href").description("새로운 리소스를 생성하기 위한 URL (POST 요청)"),
                                fieldWithPath("_links.create.httpMethod").description("create 링크의 HTTP 메서드 (POST)"),
                                fieldWithPath("_links.create.mediaType").description("create 링크의 미디어 타입 (application/json)"),
                                fieldWithPath("content[0].id").description("댓글의 고유 식별자"),
                                fieldWithPath("content[0]._links.self.href").description("특정 댓글을 가져오기 위한 URL (GET 요청)"),
                                fieldWithPath("content[0]._links.self.httpMethod").description("self 링크의 HTTP 메서드 (GET)"),
                                fieldWithPath("content[0]._links.self.mediaType").description("self 링크의 미디어 타입 (application/json)"),
                                fieldWithPath("content[0]._links.self.description").description("self 링크의 설명"),
                                fieldWithPath("content[0].title").description("댓글의 제목"),
                                fieldWithPath("content[0].content").description("댓글의 내용"),
                                fieldWithPath("content[0].writer").description("댓글의 작성자"),
                                fieldWithPath("content[1].id").description("댓글의 고유 식별자"),
                                fieldWithPath("content[1]._links.create.href").description("새로운 댓글을 생성하기 위한 URL (POST 요청)"),
                                fieldWithPath("content[1]._links.create.httpMethod").description("create 링크의 HTTP 메서드 (POST)"),
                                fieldWithPath("content[1]._links.create.mediaType").description("create 링크의 미디어 타입 (application/json)"),
                                fieldWithPath("content[1]._links.create.description").description("create 링크의 설명"),
                                fieldWithPath("content[1]._links.update.href").description("특정 댓글을 업데이트하기 위한 URL (PATCH 요청)"),
                                fieldWithPath("content[1]._links.update.httpMethod").description("update 링크의 HTTP 메서드 (PATCH)"),
                                fieldWithPath("content[1]._links.update.mediaType").description("update 링크의 미디어 타입 (application/json)"),
                                fieldWithPath("content[1]._links.update.description").description("update 링크의 설명"),
                                fieldWithPath("content[1]._links.delete.href").description("특정 댓글을 삭제하기 위한 URL (DELETE 요청)"),
                                fieldWithPath("content[1]._links.delete.httpMethod").description("delete 링크의 HTTP 메서드 (DELETE)"),
                                fieldWithPath("content[1]._links.delete.mediaType").description("delete 링크의 미디어 타입 (application/json)"),
                                fieldWithPath("content[1]._links.delete.description").description("delete 링크의 설명"),
                                fieldWithPath("size").description("'content' 배열 내 댓글의 개수")
                        )
                ));
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
                .andDo(document("comment-create",
                        pathParameters(
                                parameterWithName("articleId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("succeeded").description("성공 여부"),
                                fieldWithPath("content.id").description("댓글 ID"),
                                fieldWithPath("content._links.self.href").description("댓글 자체 링크"),
                                fieldWithPath("content._links.self.httpMethod").description("댓글 조회 HTTP 메서드"),
                                fieldWithPath("content._links.self.mediaType").description("댓글 미디어 타입"),
                                fieldWithPath("content.title").description("댓글 제목"),
                                fieldWithPath("content.content").description("댓글 내용"),
                                fieldWithPath("content.writer").description("댓글 작성자")
                        )
                ));
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
                .andDo(document("comment-update",
                        pathParameters(
                                parameterWithName("articleId").description("게시글 ID"),
                                parameterWithName("commentId").description("댓글 ID")
                        ),
                        responseFields(
                                fieldWithPath("succeeded").description("성공 여부"),
                                fieldWithPath("content.id").description("댓글 ID"),
                                fieldWithPath("content._links.self.href").description("댓글 자체 링크"),
                                fieldWithPath("content._links.self.httpMethod").description("댓글 조회 HTTP 메서드"),
                                fieldWithPath("content._links.self.mediaType").description("댓글 미디어 타입"),
                                fieldWithPath("content.title").description("댓글 제목"),
                                fieldWithPath("content.content").description("댓글 내용"),
                                fieldWithPath("content.writer").description("댓글 작성자")
                        )
                ));
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
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("comment-delete",
                        pathParameters(
                                parameterWithName("articleId").description("게시글 ID"),
                                parameterWithName("commentId").description("댓글 ID")
                        )
                ));
    }

}
