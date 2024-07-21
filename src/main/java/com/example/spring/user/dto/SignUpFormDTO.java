package com.example.spring.user.dto;

import lombok.*;

@Getter // getter 메소드 생성
@Setter // setter 메소드 생성
public class SignUpFormDTO { // user 데이터베이스에 저장할 회원가입 구조 형태
    private String id;
    private String password;
    private String name;
}
