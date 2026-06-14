package kr.pe.jm.konyang1.repository;

import java.util.Optional;

import kr.pe.jm.konyang1.entity.Article;
import kr.pe.jm.konyang1.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 댓글 삭제 권한 검사에 필요한 게시글과 작성자 회원 정보를 함께 조회합니다.
    @EntityGraph(attributePaths = {"article", "member"})
    Optional<Comment> findById(Long id);

    // 게시글 상세 화면에서 댓글을 오래된 순서대로 보여주기 위해 작성일 오름차순으로 조회합니다.
    @EntityGraph(attributePaths = "member")
    List<Comment> findByArticleOrderByCreatedAtAsc(Article article);
}
