package com.example.spring.user.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@Entity
public class Profile {

    @Id
    @Column(nullable = false , unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 옵션
    private Long id;    // 기본 id(pk)

    @Column(nullable = false , length = 20)
    private String nickname;    // 프로필 닉네임

    @Column(length = 255)
    private String image;   // 프로필 사진 -> Mysql에서는 imageField가 없기 때문에 url로 저장

    @Column(length = 64)
    private String message; // 프로필 상태메시지 저장

    @OneToOne
    @JoinColumn(name = "user_id" ,referencedColumnName = "id" , nullable = false , unique = true)   // name -> profile 테이블에 컬럼 이름 , referenecedColumnName -> User 테이블의 id 컬럼을 참조
    private User user;  // 외래키로 한 게정당 한개의 프로필을 갖을수 있게 외래키를 설정
}
