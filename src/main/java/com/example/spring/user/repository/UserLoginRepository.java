package com.example.spring.user.repository;

import com.example.spring.user.domain.UserLoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


// 로그인 정보 Repository
@Repository
public interface UserLoginRepository extends JpaRepository<UserLoginInfo , String> {

}

