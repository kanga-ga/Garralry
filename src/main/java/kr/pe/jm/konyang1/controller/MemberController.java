package kr.pe.jm.konyang1.controller;

import kr.pe.jm.konyang1.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String passwordConfirm,
            @RequestParam String nickname,
            Model model) {

        if (!password.equals(passwordConfirm)) {
            model.addAttribute("errorMessage", "비밀번호가 서로 다릅니다.");
            return "register";
        }

        try {
            memberService.register(username, password, nickname);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }

        return "redirect:/login?registered";
    }
}
