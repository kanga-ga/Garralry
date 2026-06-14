package kr.pe.jm.konyang1.controller;

import java.security.Principal;
import java.util.List;

import kr.pe.jm.konyang1.dto.ArticleForm;
import kr.pe.jm.konyang1.entity.Article;
import kr.pe.jm.konyang1.repository.ArticleRepository;
import kr.pe.jm.konyang1.repository.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ArticleController {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public ArticleController(ArticleRepository articleRepository, MemberRepository memberRepository) {
        this.articleRepository = articleRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/write")
    public String writeForm() {
        return "write";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form, Principal principal) {
        String nickname = memberRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalStateException("로그인 사용자를 찾을 수 없습니다."))
                .getNickname();

        Article article = form.toEntity(nickname);
        articleRepository.save(article);

        // 글을 쓴 직후, 방금 글을 남긴 해당 카테고리 게시판으로 이동시킵니다.
        return "redirect:/board/" + form.getCategory();
    }

    // ★ 새롭게 추가된 카테고리별 게시판 보기 기능!
    // 주소창에 /board/축구 라고 들어오면 {category} 자리에 '축구'가 들어옵니다.
    @GetMapping("/board/{category}")
    public String boardList(@PathVariable String category, Model model) {

        // 1. 리포지토리에게 해당 카테고리의 글만 달라고 요청합니다.
        List<Article> articles = articleRepository.findByCategory(category);

        // 2. 찾아온 글 목록과 카테고리 이름을 화면(HTML)에 전달할 Model 상자에 담습니다.
        model.addAttribute("articles", articles);
        model.addAttribute("categoryName", category);

        // 3. board.html 화면을 보여줍니다.
        return "board";
    }

    @GetMapping("/articles/{id}")
    public String articleDetail(@PathVariable Long id, Model model) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        model.addAttribute("article", article);
        return "article";
    }
}