package me.entvy.blog.service;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestBlogPostService {
    @Bean
    public BlogPostService blogPostService() {
        BlogPostService mock = Mockito.mock(BlogPostService.class);
        // 필요한 stub 설정
        return mock;
    }
}
