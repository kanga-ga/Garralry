package kr.pe.jm.konyang1.controller;

import java.security.Principal;

import kr.pe.jm.konyang1.repository.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 이 클래스가 화면을 반환하는 컨트롤러임을 스프링에 알립니다.
public class ViewController {

    private final MemberRepository memberRepository;

    public ViewController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 브라우저에 'localhost:8080/' 이라고 입력하면 이 메서드가 실행됩니다.
    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            memberRepository.findByUsername(principal.getName())
                    .ifPresent(member -> model.addAttribute("nickname", member.getNickname()));
        }

        return "home"; // templates 폴더 안의 home.html을 찾아서 보여줍니다.
    }

    // 'localhost:8080/login' 이라고 입력하면 실행됩니다.
    @GetMapping("/login")
    public String login() {
        return "login"; // login.html을 보여줍니다.
    }

    // 'localhost:8080/register' 라고 입력하면 실행됩니다.
    @GetMapping("/register")
    public String register() {
        return "register"; // register.html을 보여줍니다.
    }
}