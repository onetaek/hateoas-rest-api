package com.devframe.docs.article;

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

    @DisplayName("게시글 단일건 조회 API")
    @Test
    void findById() throws Exception {
        //given
        Article article = createArticle("title1", "content1", "writer1");
        Article savedArticle = articleCommandRepository.save(article);

        //when //then
        mockMvc.perform(get("/articles/{id}",savedArticle.getId())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("article-read",
                        pathParameters(
                            parameterWithName("id").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("succeeded").description("Whether the request succeeded or not"),
                                fieldWithPath("content.id").description("The ID of the article"),
                                fieldWithPath("content._links.self.href").description("The self link of the article"),
                                fieldWithPath("content._links.create.href").description("The create link of the article"),
                                fieldWithPath("content._links.update.href").description("The update link of the article"),
                                fieldWithPath("content._links.delete.href").description("The delete link of the article"),
                                fieldWithPath("content._links.self.httpMethod").description("HTTP method for self link"),
                                fieldWithPath("content._links.self.mediaType").description("Media type for self link"),
                                fieldWithPath("content._links.self.description").description("Description for self link"),
                                fieldWithPath("content._links.create.httpMethod").description("HTTP method for create link"),
                                fieldWithPath("content._links.create.mediaType").description("Media type for create link"),
                                fieldWithPath("content._links.create.description").description("Description for create link"),
                                fieldWithPath("content._links.update.httpMethod").description("HTTP method for update link"),
                                fieldWithPath("content._links.update.mediaType").description("Media type for update link"),
                                fieldWithPath("content._links.update.description").description("Description for update link"),
                                fieldWithPath("content._links.delete.httpMethod").description("HTTP method for delete link"),
                                fieldWithPath("content._links.delete.mediaType").description("Media type for delete link"),
                                fieldWithPath("content._links.delete.description").description("Description for delete link"),
                                fieldWithPath("content.title").description("The title of the article"),
                                fieldWithPath("content.content").description("The content of the article"),
                                fieldWithPath("content.writer").description("The writer of the article"),
                                fieldWithPath("content.views").description("The number of views of the article"),
                                fieldWithPath("content.createdTime").description("The creation time of the article"),
                                fieldWithPath("content.modifiedTime").description("The modification time of the article")
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
