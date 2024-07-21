package com.example.spring.user.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@Builder
@DynamicUpdate
@Entity
@AllArgsConstructor
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class UserLoginInfo {
    @Id
    @Column(nullable = false , unique = true)
    private String token;       // sessionID

    @Column(nullable = false , unique = true)
    private String id;          // 로그인한 회원의 ID
}
