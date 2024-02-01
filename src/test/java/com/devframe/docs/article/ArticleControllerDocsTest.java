package com.devframe.docs.article;

import com.devframe.common.RestDocsConfiguration;
import com.devframe.domain.article.dto.request.ArticleCreateRequest;
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
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @DisplayName("게시글 등록 API")
    @Test
    void test() throws Exception {
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
                .andDo(MockMvcRestDocumentation.document("article-create",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(STRING).description("게시글 제목"),
                                fieldWithPath("content").type(STRING).description("게시글 내용"),
                                fieldWithPath("writer").type(STRING).description("게시글 작성자")
                        ),
                        responseFields(
                                fieldWithPath("content.id").type(NUMBER).description("ID of the created article"),
                                fieldWithPath("content.title").type(STRING).description("게시글 제목"),
                                fieldWithPath("content.content").type(STRING).description("게시글 내용"),
                                fieldWithPath("content.writer").type(STRING).description("게시글 작성자"),
                                fieldWithPath("content.views").type(NUMBER).description("Number of views for the created article"),
                                fieldWithPath("succeeded").type(BOOLEAN).description("API 호출 성공여부"),
                                fieldWithPath("content.createdTime").type(STRING).description("Creation time of the article"),
                                fieldWithPath("content.modifiedTime").type(STRING).description("Modification time of the article"),
                                fieldWithPath("_links.self.href").type(STRING).description("Link to the created article"),
                                fieldWithPath("_links.self.httpMethod").type(STRING).description("HTTP method for the self link"),
                                fieldWithPath("_links.self.mediaType").type(STRING).description("Media type for the self link")
                        )
                ));

    }


}
