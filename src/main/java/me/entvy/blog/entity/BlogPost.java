package me.entvy.blog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

import java.time.LocalDateTime;

@Entity
public class BlogPost {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String author;

    @Lob
    private String markdownContent;
    private String imageUrl;
    private LocalDateTime createdAt;
}
