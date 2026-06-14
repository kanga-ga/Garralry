package kr.pe.jm.konyang1.controller;

import java.security.Principal;
import java.util.List;

import kr.pe.jm.konyang1.dto.ArticleForm;
import kr.pe.jm.konyang1.entity.Article;
import kr.pe.jm.konyang1.entity.Comment;
import kr.pe.jm.konyang1.entity.Member;
import kr.pe.jm.konyang1.repository.ArticleRepository;
import kr.pe.jm.konyang1.repository.CommentRepository;
import kr.pe.jm.konyang1.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final CommentRepository commentRepository;

    public ArticleController(ArticleRepository articleRepository, MemberRepository memberRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.memberRepository = memberRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/write")
    public String writeForm(@RequestParam(required = false) String category, Model model) {
        model.addAttribute("selectedCategory", category);
        return "write";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form, Principal principal, RedirectAttributes redirectAttributes) {
        Member member = getCurrentMember(principal);

        Article article = form.toEntity(member);
        articleRepository.save(article);

        String category = form.getCategory();

        redirectAttributes.addAttribute("category", category);
        return "redirect:/board/{category}";
    }

    @GetMapping("/board/건의/신고")
    public String suggestionBoard(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        return showBoard("건의/신고", keyword, page, model);
    }

    @GetMapping("/board/{category}")
    public String boardList(
            @PathVariable String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        return showBoard(category, keyword, page, model);
    }

    private String showBoard(String category, String keyword, int page, Model model) {
        int currentPage = Math.max(page, 0);
        Pageable pageable = PageRequest.of(currentPage, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Article> articlePage;

        // 검색어가 있으면 같은 카테고리 안에서 제목 또는 내용에 검색어가 포함된 글만 조회합니다.
        if (keyword != null && !keyword.isBlank()) {
            articlePage = articleRepository.findByCategoryAndTitleContainingIgnoreCaseOrCategoryAndContentContainingIgnoreCase(
                    category,
                    keyword,
                    category,
                    keyword,
                    pageable);
        } else {
            articlePage = articleRepository.findByCategory(category, pageable);
        }

        model.addAttribute("articles", articlePage.getContent());
        model.addAttribute("articlePage", articlePage);
        model.addAttribute("categoryName", category);
        model.addAttribute("keyword", keyword);
        return "board";
    }

    @GetMapping("/articles/{id}")
    public String articleDetail(@PathVariable Long id, Model model, Principal principal) {
        Article article = findArticle(id);
        List<Comment> comments = commentRepository.findByArticleOrderByCreatedAtAsc(article);
        Member currentMember = principal == null ? null : getCurrentMember(principal);
        List<Long> myCommentIds = currentMember == null
                ? List.of()
                : comments.stream()
                .filter(comment -> isCommentAuthor(comment, currentMember))
                .map(Comment::getId)
                .toList();

        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        model.addAttribute("myCommentIds", myCommentIds);

        // 화면에서 수정/삭제 버튼을 보여줄지 결정하기 위해 현재 로그인 사용자가 작성자인지 확인합니다.
        model.addAttribute("isAuthor", currentMember != null && isArticleAuthor(article, currentMember));
        return "article";
    }

    @PostMapping("/articles/{id}/comments")
    public String createComment(@PathVariable Long id, @RequestParam String content, Principal principal) {
        Article article = findArticle(id);
        Member member = getCurrentMember(principal);

        if (content == null || content.isBlank()) {
            return "redirect:/articles/" + id;
        }

        Comment comment = Comment.builder()
                .article(article)
                .member(member)
                .author(member.getNickname())
                .content(content.trim())
                .build();

        commentRepository.save(comment);
        return "redirect:/articles/" + id;
    }

    @PostMapping("/articles/{articleId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long articleId, @PathVariable Long commentId, Principal principal) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));
        Member member = getCurrentMember(principal);

        if (!comment.getArticle().getId().equals(articleId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글과 댓글 정보가 일치하지 않습니다.");
        }

        if (!isCommentAuthor(comment, member)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
        return "redirect:/articles/" + articleId;
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

        Member member = getCurrentMember(principal);
        return isArticleAuthor(article, member);
    }

    private boolean isArticleAuthor(Article article, Member member) {
        // 새로 작성되는 글은 회원 id로 작성자를 비교합니다.
        if (article.getMember() != null) {
            return article.getMember().getId().equals(member.getId());
        }

        // 기존에 저장된 글은 member_id가 없을 수 있으므로 닉네임 비교로 한 번 더 확인합니다.
        return article.getAuthor().equals(member.getNickname());
    }

    private boolean isCommentAuthor(Comment comment, Member member) {
        return comment.getMember() != null && comment.getMember().getId().equals(member.getId());
    }

    private void validateAuthor(Article article, Principal principal) {
        if (!isAuthor(article, principal)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 글만 수정하거나 삭제할 수 있습니다.");
        }
    }

    private Member getCurrentMember(Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        return memberRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 사용자를 찾을 수 없습니다."));
    }
}