package com.example.spring.user.controller;

import com.example.spring.user.domain.Profile;
import com.example.spring.user.dto.ProfileCreateFormDTO;
import com.example.spring.user.service.interfaces.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileAPIController {

    private final ProfileService profileService;

    private static final String LOGIN_IN_FAILURE = "로그인 해야지 이용할 수 있는 서비스 입니다.";

    @PostMapping("/profile/create")
    public ResponseEntity Create(@RequestParam("file")MultipartFile multipartFile,
                                 @RequestParam("nickname") String nickname,
                                 @RequestParam("message") String message,
                                 HttpServletRequest request){

        String authHeader = request.getHeader("Authorization"); // 요청한 유저의 세션 ID

        if(authHeader!=null && authHeader.startsWith("Bearer ")){       // 세션 ID가 존재한다면 = 로그인 상태
            ProfileCreateFormDTO profileCreateFormDTO = new ProfileCreateFormDTO();

            profileCreateFormDTO.setNickname(nickname);
            profileCreateFormDTO.setMessage(message);
            return profileService.create(multipartFile , profileCreateFormDTO , request);
        }else{
            return new ResponseEntity<>(LOGIN_IN_FAILURE , HttpStatus.BAD_REQUEST);  // 로그인 정보가 없으면
        }

    }
}
