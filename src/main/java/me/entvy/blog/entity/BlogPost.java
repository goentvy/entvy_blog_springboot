package me.entvy.blog.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "포스트 ID", example = "1")
    private Long id;

    @Schema(description = "제목", example = "스프링 부트 Swagger 적용하기")
    private String title;

    @Column(unique = true)
    @Schema(description = "슬러그", example = "Swagger")
    private String slug; // URL-friendly 고유 키

    @Schema(description = "작성자", example = "Entvy")
    private String author;

    @Schema(description = "카테고리", example = "Spring")
    private String category;

    @Schema(description = "이미지URL", example = "/uploads/springboot.jpg")
    private String imageUrl;

    @Schema(description = "포스트등록일자", example = "2025-10-23T11:21:48")
    private LocalDateTime createdAt;

    @Lob
    @Column(columnDefinition = "TEXT")
    @Schema(description = "포스트내용", example = "## 스프링 부트 Swagger 적용하기 ...")
    private String markdownContent;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        BlogPost blogPost = (BlogPost) o;
        return getId() != null && Objects.equals(getId(), blogPost.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
