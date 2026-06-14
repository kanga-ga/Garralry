package kr.pe.jm.konyang1.controller;

import java.security.Principal;
import java.util.List;

import kr.pe.jm.konyang1.dto.ArticleForm;
import kr.pe.jm.konyang1.entity.Article;
import kr.pe.jm.konyang1.repository.ArticleRepository;
import kr.pe.jm.konyang1.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ArticleController {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public ArticleController(ArticleRepository articleRepository, MemberRepository memberRepository) {
        this.articleRepository = articleRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/write")
    public String writeForm(@RequestParam(required = false) String category, Model model) {
        model.addAttribute("selectedCategory", category);
        return "write";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form, Principal principal, RedirectAttributes redirectAttributes) {
        String nickname = memberRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalStateException("로그인 사용자를 찾을 수 없습니다."))
                .getNickname();

        Article article = form.toEntity(nickname);
        articleRepository.save(article);

        String category = form.getCategory();

        redirectAttributes.addAttribute("category", category);
        return "redirect:/board/{category}";
    }

    @GetMapping("/board/건의/신고")
    public String suggestionBoard(Model model) {
        return showBoard("건의/신고", model);
    }

    @GetMapping("/board/{category}")
    public String boardList(@PathVariable String category, Model model) {
        return showBoard(category, model);
    }

    private String showBoard(String category, Model model) {
        List<Article> articles = articleRepository.findByCategory(category);
        model.addAttribute("articles", articles);
        model.addAttribute("categoryName", category);
        return "board";
    }
    @GetMapping("/articles/{id}")
    public String articleDetail(@PathVariable Long id, Model model, Principal principal) {
        Article article = findArticle(id);

        model.addAttribute("article", article);

        // 화면에서 수정/삭제 버튼을 보여줄지 결정하기 위해 현재 로그인 사용자가 작성자인지 확인합니다.
        model.addAttribute("isAuthor", isAuthor(article, principal));
        return "article";
    }

    @GetMapping("/articles/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, Principal principal) {
        Article article = findArticle(id);

        // 작성자가 아닌 사용자가 주소를 직접 입력해 수정 화면에 들어오는 것을 막습니다.
        validateAuthor(article, principal);

        model.addAttribute("article", article);
        return "edit";
    }

    @PostMapping("/articles/{id}/update")
    public String updateArticle(@PathVariable Long id, ArticleForm form, Principal principal) {
        Article article = findArticle(id);

        // 저장하기 직전에도 다시 작성자를 검사해서 다른 사람 글 수정을 차단합니다.
        validateAuthor(article, principal);

        article.update(form.getCategory(), form.getTitle(), form.getContent());
        articleRepository.save(article);

        return "redirect:/articles/" + id;
    }

    @PostMapping("/articles/{id}/delete")
    public String deleteArticle(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        Article article = findArticle(id);
        String category = article.getCategory();

        // 삭제도 작성자 본인만 가능하도록 서버에서 최종 확인합니다.
        validateAuthor(article, principal);

        articleRepository.delete(article);

        redirectAttributes.addAttribute("category", category);
        return "redirect:/board/{category}";
    }

    private Article findArticle(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
    }

    private boolean isAuthor(Article article, Principal principal) {
        if (principal == null) {
            return false;
        }

        String nickname = memberRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 사용자를 찾을 수 없습니다."))
                .getNickname();

        return article.getAuthor().equals(nickname);
    }

    private void validateAuthor(Article article, Principal principal) {
        if (!isAuthor(article, principal)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 글만 수정하거나 삭제할 수 있습니다.");
        }
    }
}