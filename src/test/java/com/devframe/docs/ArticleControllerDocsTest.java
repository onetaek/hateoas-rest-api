package com.devframe.docs;

import com.devframe.common.RestDocsConfiguration;
import com.devframe.domain.article.dto.request.ArticleCreateRequest;
import com.devframe.domain.article.dto.request.ArticleUpdateRequest;
import com.devframe.domain.article.entity.Article;
import com.devframe.domain.article.repository.ArticleCommandRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
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
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ExtendWith(RestDocumentationExtension.class)
public class ArticleControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleCommandRepository articleCommandRepository;

    @DisplayName("게시글 단일 조회 API")
    @Test
    void findById() throws Exception {
        //given
        Article article = createArticle("제목1", "내용1", "작성자1");
        Article savedArticle = articleCommandRepository.save(article);

        //when //then
        mockMvc.perform(get("/articles/{id}", savedArticle.getId())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("article-read",
                        pathParameters(
                                parameterWithName("id").description("게시글 식별자")
                        ),
                        responseFields(
                                fieldWithPath("succeeded").description("요청 성공 여부"),
                                fieldWithPath("content.id").description("게시글 식별자"),
                                fieldWithPath("content._links.self.href").description("게시글 자기 참조 링크"),
                                fieldWithPath("content._links.create.href").description("게시글 생성 링크"),
                                fieldWithPath("content._links.update.href").description("게시글 수정 링크"),
                                fieldWithPath("content._links.delete.href").description("게시글 삭제 링크"),
                                fieldWithPath("content._links.self.httpMethod").description("자기 참조 링크의 HTTP 메서드"),
                                fieldWithPath("content._links.self.mediaType").description("자기 참조 링크의 미디어 타입"),
                                fieldWithPath("content._links.self.description").description("자기 참조 링크 설명"),
                                fieldWithPath("content._links.create.httpMethod").description("생성 링크의 HTTP 메서드"),
                                fieldWithPath("content._links.create.mediaType").description("생성 링크의 미디어 타입"),
                                fieldWithPath("content._links.create.description").description("생성 링크 설명"),
                                fieldWithPath("content._links.update.httpMethod").description("수정 링크의 HTTP 메서드"),
                                fieldWithPath("content._links.update.mediaType").description("수정 링크의 미디어 타입"),
                                fieldWithPath("content._links.update.description").description("수정 링크 설명"),
                                fieldWithPath("content._links.delete.httpMethod").description("삭제 링크의 HTTP 메서드"),
                                fieldWithPath("content._links.delete.mediaType").description("삭제 링크의 미디어 타입"),
                                fieldWithPath("content._links.delete.description").description("삭제 링크 설명"),
                                fieldWithPath("content.title").description("게시글 제목"),
                                fieldWithPath("content.content").description("게시글 내용"),
                                fieldWithPath("content.writer").description("게시글 작성자"),
                                fieldWithPath("content.views").description("게시글 조회 수"),
                                fieldWithPath("content.createdTime").description("게시글 작성 시간"),
                                fieldWithPath("content.modifiedTime").description("게시글 수정 시간")
                        )
                ));
    }

    @DisplayName("게시글 등록 API")
    @Test
    void create() throws Exception {
        //given
        ArticleCreateRequest request = ArticleCreateRequest.builder()
                .title("title1")
                .content("content1")
                .writer("writer1")
                .build();

        //when //then
        mockMvc.perform(
                post("/articles")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("article-create",
                        requestFields(
                                fieldWithPath("title").type(STRING).description("게시글 제목"),
                                fieldWithPath("content").type(STRING).description("게시글 내용"),
                                fieldWithPath("writer").type(STRING).description("게시글 작성자")
                        ),
                        responseFields(
                                fieldWithPath("succeeded").type(BOOLEAN).description("API 호출 성공여부"),
                                fieldWithPath("content.id").type(NUMBER).description("게시글 ID"),
                                fieldWithPath("_links.self.href").type(STRING).description("게시글 단일건 조회 URL"),
                                fieldWithPath("_links.self.httpMethod").type(STRING).description("게시글 단일건 조회 HTTP method"),
                                fieldWithPath("_links.self.mediaType").type(STRING).description("게시글 단일건 조회 mediaType"),
                                fieldWithPath("content.title").type(STRING).description("게시글 제목"),
                                fieldWithPath("content.content").type(STRING).description("게시글 내용"),
                                fieldWithPath("content.writer").type(STRING).description("게시글 작성자"),
                                fieldWithPath("content.views").type(NUMBER).description("게시글 조회수"),
                                fieldWithPath("content.createdTime").type(STRING).description("게시글 생성시각"),
                                fieldWithPath("content.modifiedTime").type(STRING).description("게시글의 마지막 수정시간")
                        )
                ));
    }

    @DisplayName("게시글 수정 REST API")
    @Test
    void update() throws Exception {
        //given
        Article article = createArticle("title1", "content1", "writer1");
        Article savedArticle = articleCommandRepository.save(article);
        ArticleUpdateRequest request = ArticleUpdateRequest.builder()
                .title("title update")
                .content("content update")
                .writer("writer update")
                .build();

        //when //then
        mockMvc.perform(patch("/articles/{id}", savedArticle.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("article-update",
                        pathParameters(
                                parameterWithName("id").description("게시글 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").type(STRING).description("게시글 제목"),
                                fieldWithPath("content").type(STRING).description("게시글 내용"),
                                fieldWithPath("writer").type(STRING).description("게시글 작성자")
                        ),
                        responseFields(
                                fieldWithPath("succeeded").type(BOOLEAN).description("API 호출 성공여부"),
                                fieldWithPath("content.id").type(NUMBER).description("게시글 ID"),
                                fieldWithPath("_links.self.href").type(STRING).description("게시글 단일건 조회 URL"),
                                fieldWithPath("_links.self.httpMethod").type(STRING).description("게시글 단일건 조회 HTTP method"),
                                fieldWithPath("_links.self.mediaType").type(STRING).description("게시글 단일건 조회 mediaType"),
                                fieldWithPath("content.title").type(STRING).description("게시글 제목"),
                                fieldWithPath("content.content").type(STRING).description("게시글 내용"),
                                fieldWithPath("content.writer").type(STRING).description("게시글 작성자"),
                                fieldWithPath("content.views").type(NUMBER).description("게시글 조회수"),
                                fieldWithPath("content.createdTime").type(STRING).description("게시글 생성시각"),
                                fieldWithPath("content.modifiedTime").type(STRING).description("게시글의 마지막 수정시간")
                        )
                ));
    }
    
    
    @DisplayName("게시글 삭제 REST API")
    @Test
    void deleteArticle() throws Exception {
        //given
        Article article = createArticle("title1", "content1", "writer1");
        Article savedArticle = articleCommandRepository.save(article);

        //when //then
        mockMvc.perform(delete("/articles/{id}", savedArticle.getId())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("article-delete",
                        pathParameters(
                                parameterWithName("id").description("게시글 ID")
                        )
                ));
    }


}
