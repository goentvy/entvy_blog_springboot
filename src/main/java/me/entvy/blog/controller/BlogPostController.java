package me.entvy.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.entvy.blog.entity.BlogPost;
import me.entvy.blog.repository.BlogPostRepository;
import me.entvy.blog.service.BlogPostService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "블로그 포스트", description = "블로그 포스트 관련 API")
public class BlogPostController {

    private final BlogPostRepository blogPostRepository;
    private static final String UPLOAD_DIR = "upload/";
    private final BlogPostService blogPostService;

//  /api/posts - 모든 post 조회 ( Mybatis )
    @Operation(summary = "모든 포스트 조회", description = "등록된 모든 블로그 포스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포스트 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/posts")
    public List<BlogPost> viewPosts() {
        return blogPostService.getAllPosts();
    }

//  /api/posts/{id} - 단일 포스트 조회 ( Mybatis )
    @Operation(summary = "단일 포스트 조회", description = "등록된 단일 블로그 포스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포스트 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/posts/id/{id}")
    public ResponseEntity<BlogPost> viewPost(@PathVariable Long id) {
        BlogPost post = blogPostService.getPostById(id);
        return post != null ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();
    }

//  /api/posts/{slug} - 특정 포스트 조회 ( Mybatis )
    @Operation(summary = "특정 포스트 조회", description = "등록된 특정 블로그 포스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포스트 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/posts/slug/{slug}")
    public ResponseEntity<BlogPost> getPostBySlug(@PathVariable String slug) {
        BlogPost post = blogPostService.getPostBySlug(slug);
        return post != null ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();
    }

//  /api/posts/category/{category} - 카테고리별 보기 ( Mybatis )
    @Operation(summary = "카테고리 포스트 보기", description = "등록된 카테고리 블로그 포스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포스트 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/posts/category/{category}")
    public List<BlogPost> getPostsByCategory(@PathVariable String category) {
        return blogPostService.getPostsByCategory(category);
    }

//  /api/posts/latest - 최신 포스트 (4개) / 홈화면 보여주기용 ( Mybatis )
    @Operation(summary = "최신 포스트 4개 조회", description = "홈화면 보여주기용 최신 포스트 4개를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포스트 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/posts/latest")
    public List<BlogPost> getLatestPosts(@RequestParam(defaultValue = "4") int limit) {
        return blogPostService.getLatestPosts(limit);
    }

//  /api/posts/random - 랜덤 포스트 (4개) / 홈화면 보여주기용 ( Mybatis )
    @Operation(summary = "랜덤 포스트 4개 조회", description = "홈화면 보여주기용 랜덤 포스트 4개를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포스트 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/posts/random")
    public List<BlogPost> getRandomPosts(@RequestParam(defaultValue = "4") int limit) {
        return blogPostService.getRandomPosts(limit);
    }

//  /api/posts - Markdown 포스트 등록 ( Mybatis )
    @Operation(summary = "포스트 등록", description = "새로운 블로그 포스트를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포스트 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(@RequestBody BlogPost post) {
        blogPostService.createPost(post);
        return ResponseEntity.ok().build();
    }

//  /api/posts/slug/{slug} - 포스트 수정 ( Mybatis )
    @Operation(summary = "포스트 수정", description = "선택한 블로그 포스트를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포스트 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/posts/slug/{slug}")
    public ResponseEntity<?> updatePost(@PathVariable String slug, @RequestBody BlogPost blogPost) {
        boolean updated = blogPostService.updatePostBySlug(slug, blogPost);
        if(updated) {
            return ResponseEntity.ok("글이 수정되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 글을 찾을 수 없습니다.");
        }
    }

//  /api/posts/{id}
    @Operation(summary = "포스트 삭제", description = "선택한 블로그 포스트를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포스트 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        blogPostService.deletePost(id);
        return ResponseEntity.ok().build();
    }

//  /api/images - 이미지 업로드 ( JPA )
    @Operation(summary = "이미지 업로드", description = "새로운 이미지를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/images")
    public ResponseEntity<String> imageUpload(
            @Parameter(description = "업로드할 이미지 파일")
            @RequestParam("file") MultipartFile file) throws IOException {
        // 파일을 로컬에 저장하고 URL 반환
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        String imageUrl = "/uploads/" + fileName;
        return ResponseEntity.ok(imageUrl);
    }

//  /api/image/{filename} - 이미지 불러오기 ( JPA )
    @Operation(summary = "이미지 불러오기", description = "등록된 이미지를 불러옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 불러오기 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
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
