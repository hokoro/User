package com.example.spring.user.repository;

import com.example.spring.user.domain.UserLoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

// 로그인 정보 Repository
@Repository
public interface UserLoginRepository extends JpaRepository<UserLoginInfo , String> {

    @Query("SELECT u.id FROM UserLoginInfo u where u.token = :token")   // 쿼리문
    List<String> findUserIdByToken(@Param("token") String token);   // token 인자를 받아 사용자의 id를 가져오는 쿼리문




}

