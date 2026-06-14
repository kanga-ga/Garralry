package kr.pe.jm.konyang1.repository;

import java.util.Optional;

import kr.pe.jm.konyang1.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // 상세/수정/삭제 권한 검사에서 작성자 회원 정보를 바로 사용할 수 있도록 함께 조회합니다.
    @EntityGraph(attributePaths = "member")
    Optional<Article> findById(Long id);

    // ★ "category 필드 값이 일치하는 데이터들만 전부(List) 찾아줘!" 라는 마법의 주문입니다.
    Page<Article> findByCategory(String category, Pageable pageable);

    // 같은 카테고리 안에서 제목 또는 내용에 검색어가 포함된 게시글을 페이지 단위로 조회합니다.
    Page<Article> findByCategoryAndTitleContainingIgnoreCaseOrCategoryAndContentContainingIgnoreCase(
            String titleCategory,
            String titleKeyword,
            String contentCategory,
            String contentKeyword,
            Pageable pageable);
}