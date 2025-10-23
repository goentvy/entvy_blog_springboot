package me.entvy.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.entvy.blog.entity.BlogPost;
import me.entvy.blog.service.BlogPostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlogPostController.class)
@Import(BlogPostControllerTest.TestConfig.class)
class BlogPostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BlogPostService blogPostService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {

        public BlogPostService blogPostService() {
            return Mockito.mock(BlogPostService.class);
        }
    }

    @Test
    @DisplayName("모든 포스트 조회")
    void testViewPosts() throws Exception {
        BlogPost post = BlogPost.builder()
                .id(1L)
                .title("테스트 제목")
                .slug("test-slug")
                .author("Entvy")
                .category("React")
                .createdAt(LocalDateTime.now())
                .build();
        when(blogPostService.getAllPosts()).thenReturn(Collections.singletonList(post));

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("테스트 제목"));
    }

    @Test
    @DisplayName("단일 포스트 조회")
    void testViewPostById() throws Exception {
        BlogPost post = BlogPost.builder()
                .id(1L)
                .title("Zustand")
                .slug("test1")
                .author("Entvy")
                .category("React")
                .createdAt(LocalDateTime.now())
                .build();

        when(blogPostService.getPostById(1L)).thenReturn(post);

        mockMvc.perform(get("/api/posts/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Zustand"));
    }

    @Test
    @DisplayName("포스트 등록")
    void testCreatePost() throws Exception {
        BlogPost post = BlogPost.builder()
                .title("New Post")
                .slug("new-post")
                .author("Entvy")
                .category("Java")
                .markdownContent("내용입니다")
                .build();

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk());
    }

}
