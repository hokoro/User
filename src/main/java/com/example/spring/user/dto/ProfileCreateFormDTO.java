package com.example.spring.user.dto;

import lombok.*;


@Getter
@Setter
public class ProfileCreateFormDTO {
    private Long id;
    private String nickname;
    private String message;
}
