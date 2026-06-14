package kr.pe.jm.konyang1.dto;

import kr.pe.jm.konyang1.entity.Article;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleForm {

    private String category; // ★ 화면에서 넘어올 카테고리 데이터
    private String title;
    private String content;

    public Article toEntity(String author) {
        return Article.builder()
                .category(category) // 엔티티로 변환할 때 카테고리도 함께 포장합니다.
                .title(title)
                .author(author)
                .content(content)
                .build();
    }
}