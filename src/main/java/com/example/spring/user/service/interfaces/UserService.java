package com.example.spring.user.service.interfaces;

import com.example.spring.user.dto.SignUpFormDTO;
import com.example.spring.user.dto.UserDeleteFormDTO;
import com.example.spring.user.dto.UserUpdateFormDTO;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.*;
public interface UserService {  // User Service에 대한 인터페이스
    ResponseEntity signup(SignUpFormDTO formDTO); // 인터페이스 중 회원가입 기능 명시

    ResponseEntity update(UserUpdateFormDTO formDTO , HttpServletRequest request);   // 인터페이스 유저 정보 업데이트


    ResponseEntity delete(UserDeleteFormDTO formDTO, HttpServletRequest request);

}
