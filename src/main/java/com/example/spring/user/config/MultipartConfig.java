package com.example.spring.user.config;


import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
public class MultipartConfig {
    @Bean
    public MultipartResolver multipartResolver(){
        return new StandardServletMultipartResolver();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement(){    // 파일 업로드 설정
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation("/files/"); // 파일 업로드의 위치
        factory.setMaxFileSize(DataSize.ofMegabytes(100L)); // 최대 파일 사이즈
        factory.setMaxRequestSize(DataSize.ofMegabytes(100L));  // 최대 요청 사이즈

        return factory.createMultipartConfig();
    }


}
