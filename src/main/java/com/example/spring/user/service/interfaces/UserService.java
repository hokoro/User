package com.example.spring.user.service.interfaces;

import com.example.spring.user.dto.LoginFormDTO;
import com.example.spring.user.dto.SignUpFormDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {  // User Service에 대한 인터페이스
    ResponseEntity signup(SignUpFormDTO formDTO); // 인터페이스 중 회원가입 기능 명시


}
