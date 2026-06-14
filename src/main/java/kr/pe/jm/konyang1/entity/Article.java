package kr.pe.jm.konyang1.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "article")
@Getter
@Setter
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String category; // ★ 카테고리를 저장할 새로운 칸을 추가합니다!

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false, length = 50)
    private String author;

    private LocalDateTime createdAt;

    @Builder // category가 추가되었으므로 생성자에도 포함시켜 줍니다.
    public Article(Long id, String category, String title, String content, String author, LocalDateTime createdAt) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}