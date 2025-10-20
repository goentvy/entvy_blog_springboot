package me.entvy.blog.controller;

import lombok.RequiredArgsConstructor;
import me.entvy.blog.entity.BlogPost;
import me.entvy.blog.repository.BlogPostRepository;
import me.entvy.blog.service.BlogPostService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostRepository blogPostRepository;
    private static final String UPLOAD_DIR = "upload/";
    private final BlogPostService blogPostService;

//  /api/posts - 모든 post 조회 ( JPA )
//    @GetMapping("/posts")
//    public List<BlogPost> viewPosts() {
//        return blogPostRepository.findAll();
//    }

//  /api/posts - 모든 post 조회 ( Mybatis )
    @GetMapping("/posts")
    public List<BlogPost> viewPosts() {
        return blogPostService.getAllPosts();
    }


//  /api/posts/{id} - 단일 포스트 조회 ( JPA )
//    @GetMapping("/posts/{id}")
//    public ResponseEntity<BlogPost> viewPost(@PathVariable Long id) {
//        return blogPostRepository.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//  /api/posts/{id} - 단일 포스트 조회 ( Mybatis )
    @GetMapping("/posts/id/{id}")
    public ResponseEntity<BlogPost> viewPost(@PathVariable Long id) {
        BlogPost post = blogPostService.getPostById(id);
        return post != null ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();
    }

//  /api/posts/{slug} - 특정 포스트 조회 ( JPA )
//    @GetMapping("/posts/slug/{slug}")
//    public ResponseEntity<BlogPost> getPostBySlug(@PathVariable String slug) {
//        return blogPostRepository.findBySlug(slug)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//  /api/posts/{slug} - 특정 포스트 조회 ( Mybatis )
    @GetMapping("/posts/slug/{slug}")
    public ResponseEntity<BlogPost> getPostBySlug(@PathVariable String slug) {
        BlogPost post = blogPostService.getPostBySlug(slug);
        return post != null ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();
    }

//  /api/posts/category/{category} - 카테고리별 보기 ( JPA )
//    @GetMapping("/posts/category/{category}")
//    public List<BlogPost> getPostsByCategory(@PathVariable String category) {
//        return blogPostRepository.findByCategoryOrderByCreatedAtDesc(category);
//    }
//  /api/posts/category/{category} - 카테고리별 보기 ( Mybatis )
    @GetMapping("/posts/category/{category}")
    public List<BlogPost> getPostsByCategory(@PathVariable String category) {
        return blogPostService.getPostsByCategory(category);
    }

//  /api/posts/latest - 최신 포스트 (4개) / 홈화면 보여주기용 ( JPA )
//    @GetMapping("/posts/latest")
//    public List<BlogPost> getLatestPosts(@RequestParam(defaultValue = "4") int limit) {
//        return blogPostRepository.findLatestPosts(PageRequest.of(0, limit));
//    }

//  /api/posts/latest - 최신 포스트 (4개) / 홈화면 보여주기용 ( Mybatis )
    @GetMapping("/posts/latest")
    public List<BlogPost> getLatestPosts(@RequestParam(defaultValue = "4") int limit) {
        return blogPostService.getLatestPosts(limit);
    }

//  /api/posts/random - 랜덤 포스트 (4개) / 홈화면 보여주기용 ( JPA )
//    @GetMapping("/posts/random")
//    public List<BlogPost> getRandomPosts(@RequestParam(defaultValue = "4") int limit) {
//        return blogPostRepository.findRandomPosts(PageRequest.of(0, limit));
//    }

//  /api/posts/random - 랜덤 포스트 (4개) / 홈화면 보여주기용 ( Mybatis )
    @GetMapping("/posts/random")
    public List<BlogPost> getRandomPosts(@RequestParam(defaultValue = "4") int limit) {
        return blogPostService.getRandomPosts(limit);
    }

//  /api/posts - Markdown 포스트 등록 ( JPA )
//    @PostMapping("/posts")
//    public ResponseEntity<BlogPost> createPost(@RequestBody BlogPost post) {
//        post.setCreatedAt(LocalDateTime.now());
//        BlogPost saved = blogPostRepository.save(post);
//        return ResponseEntity.ok(saved);
//    }
//  /api/posts - Markdown 포스트 등록 ( Mybatis )
    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(@RequestBody BlogPost post) {
        blogPostService.createPost(post);
        return ResponseEntity.ok().build();
    }

//  /api/images - 이미지 업로드 ( JPA )
    @PostMapping("/images")
    public ResponseEntity<String> imageUpload(@RequestParam("file") MultipartFile file) throws IOException {
        // 파일을 로컬에 저장하고 URL 반환
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        String imageUrl = "/uploads/" + fileName;
        return ResponseEntity.ok(imageUrl);
    }

//  /api/image/{filename} - 이미지 불러오기 ( JPA )
    @GetMapping("/image/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path imagePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
            Resource resource = new UrlResource(imagePath.toUri());

            if(!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(imagePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
