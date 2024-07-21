package com.example.spring.user.config;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration  // 스프링 설정 어노테이션
@EnableWebSecurity // Spring Security 활성화
public class SecurityConfig{


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .authorizeHttpRequests(authorize -> authorize   // Http에 대한 인가규칙 설정
                        .requestMatchers("/api/users/create" , "api/users/login" , "api/users/logout").permitAll() //경로에 대한 접근을 모든 사용자에게 허용한다.
                        .anyRequest().authenticated() // 나머지 모든 요청은 인증이 필요하다
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {  // 비밀번호 보안을 위한 빈 컨텍스트 등록
        return new BCryptPasswordEncoder();
    }
}