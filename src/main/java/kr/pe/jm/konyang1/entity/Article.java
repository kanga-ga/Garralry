package kr.pe.jm.konyang1.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // 작성자 회원을 id로 연결해서 닉네임이 바뀌어도 같은 회원인지 안정적으로 확인합니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 게시글이 삭제될 때 해당 게시글의 댓글도 함께 삭제되도록 연결합니다.
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    private LocalDateTime createdAt;

    @Builder // category가 추가되었으므로 생성자에도 포함시켜 줍니다.
    public Article(Long id, String category, String title, String content, String author, Member member, LocalDateTime createdAt) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.content = content;
        this.author = author;
        this.member = member;
        this.createdAt = createdAt;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // 게시글 수정 화면에서 넘어온 값으로 기존 글의 내용을 바꿉니다.
    public void update(String category, String title, String content) {
        this.category = category;
        this.title = title;
        this.content = content;
    }
}