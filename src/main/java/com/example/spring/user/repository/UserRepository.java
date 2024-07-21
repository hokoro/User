package com.example.spring.user.repository;

import com.example.spring.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 저장,조회,수정,삭제 하는 역할
public interface UserRepository extends JpaRepository<User,String> {    // User 테이블에 있는 데이터를 조작하기 위해 사용

}
