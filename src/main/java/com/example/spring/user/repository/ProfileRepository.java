package com.example.spring.user.repository;

import com.example.spring.user.domain.Profile;
import com.example.spring.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("SELECT u FROM Profile u WHERE u.user = :user")
    Profile findByProfileCustom(@Param("user") User user);

}
