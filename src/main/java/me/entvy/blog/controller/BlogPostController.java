package me.entvy.blog.controller;

import lombok.RequiredArgsConstructor;
import me.entvy.blog.entity.BlogPost;
import me.entvy.blog.repository.BlogPostRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostRepository blogPostRepository;

    @GetMapping("/posts")
    public List<BlogPost> viewPosts() {
        return blogPostRepository.findAll();
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<BlogPost> viewPost(@PathVariable Long id) {
        return blogPostRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/images")
    public ResponseEntity<String> imageUpload(@RequestParam("file") MultipartFile file) throws IOException {
        // 파일을 로컬에 저장하고 URL 반환
        String uploadDir = "upload/";
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        String imageUrl = "/uploads/" + fileName;
        return ResponseEntity.ok(imageUrl);
    }
}
