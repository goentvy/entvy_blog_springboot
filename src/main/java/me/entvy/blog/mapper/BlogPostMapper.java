package me.entvy.blog.mapper;

import me.entvy.blog.entity.BlogPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogPostMapper {
    BlogPost findById(Long id);
    BlogPost findBySlug(String slug);
    List<BlogPost> findAll();
    List<BlogPost> findByCategory(String category);
    List<BlogPost> findLatest(int limit);
    List<BlogPost> findRandom(int limit);
    void insert(BlogPost post);
    void updatePost(BlogPost blogPost);
    void deletePost(Long id);
}
