package com.example.spring.user.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class UserDetailFormDTO {
    private String message; // 응답에 쓰일 메세지

    private String id;  // 유저 아이디
    private String name; // 사용자의 이름


}
