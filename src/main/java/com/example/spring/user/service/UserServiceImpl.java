package com.example.spring.user.service;


import com.example.spring.user.domain.User;
import com.example.spring.user.dto.LoginFormDTO;
import com.example.spring.user.dto.SignUpFormDTO;
import com.example.spring.user.repository.UserLoginRepository;
import com.example.spring.user.repository.UserRepository;
import com.example.spring.user.service.interfaces.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;



@Service  // 비즈니스 로직을 수행하는 서비스 어노테이션
@Transactional // 트랜잭션 관리를 하기위해 지원하는 어노테이션 -> service 레이어에서 자동으로 사용
@RequiredArgsConstructor // 필드를 초기화하는 생성자를 자동으로 생성(final , notnull)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserLoginRepository userLoginRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();    // 비밀번호 데이터를 암호화 하기 위해 사용

    // 응답 메세지 상수화
    private static final String SIGNUP_SUCCESS = "회원가입 성공";
    private static final String SIGNUP_FAILURE = "회원가입 실패 이미 존재하는 ID입니다.";

    // 유효성 검사
    private static final String NULL_FAILURE = "유효하지 않은 입력입니다.";
    private static final String BLANK_FAILURE = "회원 정보를 입력해주세요";

    // 로그인 성공 실패
    private static final String LOGIN_SUCCESS = "로그인 성공";
    private static final String LOGIN_FAILURE = "아이디 혹은 비밀번호가 틀렸습니다.";


    @Override
    public ResponseEntity signup(SignUpFormDTO signUpFormDTO){

        // 유효성 검사 1.null
        if(signUpFormDTO.getId() == null || signUpFormDTO.getPassword() == null || signUpFormDTO.getName() == null){
            return new ResponseEntity(NULL_FAILURE , HttpStatus.BAD_REQUEST);
        }

        // 유효성 검사 2.blank
        if(signUpFormDTO.getId().equals("") || signUpFormDTO.getPassword().equals("") || signUpFormDTO.getName().equals("")){
            return new ResponseEntity(BLANK_FAILURE , HttpStatus.BAD_REQUEST);
        }

        Optional<User> member = userRepository.findById(signUpFormDTO.getId());     // 주어진 ID로 엔티티를 조회
        if(member.isEmpty()) {      // 엔티티가 비어있다 = 새로운 계정이다.
            User newUser = User.builder()   // 빌더를 이용하여 DTO에서 필요한 데이터를 수집
                    .id(signUpFormDTO.getId()) // ID
                    .password(passwordEncoder.encode(signUpFormDTO.getPassword())) // password + 암호화
                    .name(signUpFormDTO.getName()) // 이름
                    .build();
            userRepository.save(newUser); // repository를 이용한 저장
            return new ResponseEntity(SIGNUP_SUCCESS, HttpStatus.OK); // 회원가입 성공 메세지
        }else{
            return new ResponseEntity(SIGNUP_FAILURE,HttpStatus.OK); // 실패 메세지
        }

    }




}
