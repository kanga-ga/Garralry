package kr.pe.jm.konyang1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // 글 작성, 수정, 삭제는 로그인한 사용자만 접근할 수 있습니다.
                        .requestMatchers(
                                "/write",
                                "/articles/create",
                                "/articles/*/edit",
                                "/articles/*/update",
                                "/articles/*/delete",
                                "/articles/*/comments",
                                "/articles/*/comments/*/delete").authenticated()
                        .requestMatchers("/", "/login", "/register", "/board/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/articles/*").permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
