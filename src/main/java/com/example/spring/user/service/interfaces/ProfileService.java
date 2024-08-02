package com.example.spring.user.service.interfaces;


import com.example.spring.user.dto.ProfileCreateFormDTO;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.*;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    ResponseEntity create(MultipartFile multipartFile, ProfileCreateFormDTO profileCreateFormDTO , HttpServletRequest request);

    ResponseEntity detail(HttpServletRequest request);

}
