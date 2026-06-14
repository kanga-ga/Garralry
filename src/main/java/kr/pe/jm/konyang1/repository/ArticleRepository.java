package kr.pe.jm.konyang1.repository;

import kr.pe.jm.konyang1.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    // ★ "category 필드 값이 일치하는 데이터들만 전부(List) 찾아줘!" 라는 마법의 주문입니다.
    List<Article> findByCategory(String category);
}