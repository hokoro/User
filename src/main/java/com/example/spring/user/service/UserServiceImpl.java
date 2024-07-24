package com.example.spring.user.service;


import com.example.spring.user.domain.User;
import com.example.spring.user.dto.SignUpFormDTO;
import com.example.spring.user.dto.UserDeleteFormDTO;
import com.example.spring.user.dto.UserUpdateFormDTO;
import com.example.spring.user.repository.UserLoginRepository;
import com.example.spring.user.repository.UserRepository;
import com.example.spring.user.service.interfaces.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import jakarta.servlet.http.*;


@Service  // 비즈니스 로직을 수행하는 서비스 어노테이션
@Transactional // 트랜잭션 관리를 하기위해 지원하는 어노테이션 -> service 레이어에서 자동으로 사용
@RequiredArgsConstructor // 필드를 초기화하는 생성자를 자동으로 생성(final , notnull)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserLoginRepository userLoginRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();    // 비밀번호 데이터를 암호화 하기 위해 사용

    // 응답 메세지 상수화
    private static final String SIGNUP_SUCCESS = "회원가입 성공";
    private static final String SIGNUP_FAILURE = "회원가입 실패 이미 존재하는 ID입니다.";

    private static final String UPDATE_SUCCESS = "회원 정보가 성공적으로 업데이트 되었습니다";

    private static final String DELETE_SUCCESS = "정상 탈퇴";

    private static final String DELETE_FAILURE = "회원 정보가 없습니다";
    // 유효성 검사
    private static final String NULL_FAILURE = "유효하지 않은 입력입니다.";
    private static final String BLANK_FAILURE = "회원 정보를 입력해주세요";

    private static final String ID_NOT_EQUAL = "로그인한 아이디와 일치하지 않습니다.";

    private static final String NOT_FIND_USER = "사용자를 찾을수 없습니다.";



    @Override
    public ResponseEntity signup(SignUpFormDTO signUpFormDTO){

        // 유효성 검사 1.null
        if(signUpFormDTO.getId() == null || signUpFormDTO.getPassword() == null || signUpFormDTO.getName() == null){
            return new ResponseEntity<>(NULL_FAILURE , HttpStatus.BAD_REQUEST);
        }

        // 유효성 검사 2.blank
        if(signUpFormDTO.getId().equals("") || signUpFormDTO.getPassword().equals("") || signUpFormDTO.getName().equals("")){
            return new ResponseEntity<>(BLANK_FAILURE , HttpStatus.BAD_REQUEST);
        }

        Optional<User> member = userRepository.findById(signUpFormDTO.getId());     // 주어진 ID로 엔티티를 조회
        if(member.isEmpty()) {      // 엔티티가 비어있다 = 새로운 계정이다.
            User newUser = User.builder()   // 빌더를 이용하여 DTO에서 필요한 데이터를 수집
                    .id(signUpFormDTO.getId()) // ID
                    .password(passwordEncoder.encode(signUpFormDTO.getPassword())) // password + 암호화
                    .name(signUpFormDTO.getName()) // 이름
                    .build();
            userRepository.save(newUser); // repository를 이용한 저장
            return new ResponseEntity<>(SIGNUP_SUCCESS, HttpStatus.OK); // 회원가입 성공 메세지
        }else{
            return new ResponseEntity<>(SIGNUP_FAILURE,HttpStatus.OK); // 실패 메세지
        }

    }


    @Override
    public ResponseEntity update(UserUpdateFormDTO userUpdateFormDTO ,HttpServletRequest request){
        
        String authHeader = request.getHeader("Authorization"); // 요청한 유저의 세션 ID

        String token = authHeader.substring(7);  // "Bearer  을 제외한 나머지 토큰값"

        String LoginId = userLoginRepository.findUserIdByToken(token).toString();   // 토큰값과 일치한 유저 아이디 조회

        LoginId = LoginId.replaceAll("[\\[\\]]", "");   // 대괄호 제거 -> 불일치 에러가 발생함

        if(!Objects.equals(LoginId, userUpdateFormDTO.getId())){    // 로그인한 ID와 DTO로 받은 ID가 일치하지 않을 경우
            return new ResponseEntity<>(ID_NOT_EQUAL , HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.findById(userUpdateFormDTO.getId())  // 사용자 정보 조회
                        .orElse(null);

        if(user != null){   // 조회가 될경우
            user.setPassword(passwordEncoder.encode(userUpdateFormDTO.getPassword()));
            user.setName(userUpdateFormDTO.getName());
            userRepository.save(user);
            return new ResponseEntity<>(UPDATE_SUCCESS , HttpStatus.OK);
        }else{  // 사용자가 존재하지 않을경우
            return new ResponseEntity<>(NOT_FIND_USER , HttpStatus.NOT_FOUND);
        }
    }


    @Override
    public ResponseEntity delete(UserDeleteFormDTO userDeleteFormDTO , HttpServletRequest request){
        String authHeader = request.getHeader("Authorization"); // 요청한 유저의 세션 ID

        String token = authHeader.substring(7);  // "Bearer  을 제외한 나머지 토큰값"

        String LoginId = userLoginRepository.findUserIdByToken(token).toString();   // 토큰값과 일치한 유저 아이디 조회

        LoginId = LoginId.replaceAll("[\\[\\]]", "");   // 대괄호 제거 -> 불일치 에러가 발생함

        if(!Objects.equals(LoginId, userDeleteFormDTO.getId())){    // 로그인한 ID와 DTO로 받은 ID가 일치하지 않을 경우
            return new ResponseEntity<>(ID_NOT_EQUAL , HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.findById(userDeleteFormDTO.getId()).orElse(null);

        if(user != null){
            userRepository.delete(user);
            return new ResponseEntity<>(DELETE_SUCCESS , HttpStatus.OK);
        }else{
            return new ResponseEntity<>(DELETE_FAILURE , HttpStatus.UNAUTHORIZED);
        }

    }



}
