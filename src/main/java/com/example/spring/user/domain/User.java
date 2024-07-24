package com.example.spring.user.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;


@Getter  // Getter 메소드 생성
@Setter  // Setter 메소드 생성
@Builder    // 빌더 패턴으로 객체를 생성할 수 있게 해준다
@DynamicUpdate // 엔티티 업데이트 때마다 변경된 필드만 반영하는 기능을 활성화 한다.
@AllArgsConstructor // 모든 필자를 인자로 하는 생성자를 자동으로 생성
@NoArgsConstructor(access=AccessLevel.PROTECTED) // 매개변수가 없는 기본 생성자를 자동으로 생성한다. + PROTEECTED 옵션으로 외부에서 생성 못하게 막는다.
@Entity // JPA 엔티티 클래스
public class User {
    @Id         //pk
    @Column(nullable = false, unique = true) // Notnull + Unique 옵션 적용
    private String id;

    @Column(nullable = false)   // Notnull 옵션 적용
    private String password;

    @Column(nullable = false) // Notnull 옵션 적용
    private String name;

    // mappedBy -> Profile 엔티티가 user 필드의 관계의 주인이다. , cascadeType.ALL -> User가 삭제 되면 프로필도 자동으로 삭제 , FetchType-> 지연로딩 전력 사용
    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL , fetch= FetchType.LAZY)
    private Profile profile;
}
