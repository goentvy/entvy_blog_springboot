package me.entvy.blog.repository;

import me.entvy.blog.entity.BlogPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    Optional<BlogPost> findBySlug(String slug);
    List<BlogPost> findByCategoryOrderByCreatedAtDesc(String category);

    @Query("SELECT p FROM BlogPost p ORDER BY p.createdAt DESC")
    List<BlogPost> findLatestPosts(Pageable pageable);

    @Query("SELECT p FROM BlogPost p ORDER BY function('RAND')")
    List<BlogPost> findRandomPosts(Pageable pageable);
}
