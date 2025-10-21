package me.entvy.blog.service;

import lombok.RequiredArgsConstructor;
import me.entvy.blog.entity.BlogPost;
import me.entvy.blog.mapper.BlogPostMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogPostService {
    private final BlogPostMapper blogPostMapper;

    public List<BlogPost> getAllPosts() {
        return blogPostMapper.findAll();
    }

    public BlogPost getPostById(Long id) {
        return blogPostMapper.findById(id);
    }

    public BlogPost getPostBySlug(String slug) {
        return blogPostMapper.findBySlug(slug);
    }

    public List<BlogPost> getPostsByCategory(String category) {
        return blogPostMapper.findByCategory(category);
    }

    public List<BlogPost> getLatestPosts(int limit) {
        return blogPostMapper.findLatest(limit);
    }

    public List<BlogPost> getRandomPosts(int limit) {
        return blogPostMapper.findRandom(limit);
    }

    public void createPost(BlogPost post) {
        post.setCreatedAt(LocalDateTime.now());
        blogPostMapper.insert(post);
    }

    public boolean updatePostBySlug(String slug, BlogPost blogPost) {
        BlogPost existing = blogPostMapper.findBySlug(slug);
        if (existing == null) {
            return false;
        }

        blogPost.setId(existing.getId());
        blogPostMapper.updatePost(blogPost);
        return true;
    }
}
