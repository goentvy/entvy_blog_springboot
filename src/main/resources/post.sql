CREATE DATABASE blog_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE blog_db;

CREATE TABLE blog_post (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           title VARCHAR(255) NOT NULL,
                           slug VARCHAR(255) NOT NULL UNIQUE,
                           author VARCHAR(100) NOT NULL,
                           category VARCHAR(100) NOT NULL,
                           image_url VARCHAR(255),
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                           markdown_content TEXT
);


INSERT INTO blog_post (title, slug, author, category, image_url, markdown_content)
VALUES (
           'Zustand 기본 사용법',
           'zustand-guide',
           'Entvy',
           'React',
           '/uploads/zustand-guide.jpg',
           '## 3. 기본 사용법\n\n### 설치\n`npm install zustand`\n...'
       );


SELECT * FROM blog_post WHERE id = 1;