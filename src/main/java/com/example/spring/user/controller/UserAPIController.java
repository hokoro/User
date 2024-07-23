package com.example.spring.user.controller;

import com.example.spring.user.domain.*;
import com.example.spring.user.dto.*;
import com.example.spring.user.repository.*;
import com.example.spring.user.service.interfaces.UserService;
import lombok.*;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.*;
import java.util.Optional;

@RestController // Restful API 컨트롤러 어노테이션
@RequestMapping("/api") // 이 컨트롤러의 모든 요청은 API를 통해서 요청한다
@RequiredArgsConstructor // final로 선언된 모든 인수로 가지는 생성자를 자동으로 생성  -> 없으면 만들어줘야 함
public class UserAPIController {
    private final UserService userService;

    private final UserRepository userRepository;
    private final UserLoginRepository userLoginRepository;

    private final PasswordEncoder passwordEncoder;


    private static final String HEADER_NULL = "헤더가 존재하지 않습니다.";
    private static final String SESSION_NULL_FAILURE = "세션이 존재하지 않습니다.";

    //private static final String SESSION_LOGIN_INFO_NULL = "로그인 정보가 존재하지 않습니다";

    private static final String NOT_ID_EXIST = "해당 아이디를 가진 회원이 존재하지 않습니다.";


    // 로그인 성공 실패
    private static final String LOGIN_SUCCESS = "로그인 성공";

    private static final String LOGOUT_SUCCESS = "로그아웃 성공";
    private static final String LOGIN_FAILURE = "아이디 혹은 비밀번호가 틀렸습니다.";

    // 회원가입 API URL
    @PostMapping("/users/create")   // 포스트 요청 전용 어노테이션
    public ResponseEntity Create(@RequestBody SignUpFormDTO signUpFormDTO){ // 데이터베이스로 넘길 회원가입 dto
        return userService.signup(signUpFormDTO); // UserService에 있는 인터페이스에 회원가입 함수를 요청
    }

    // 로그인 API URL + LOGIC
    @PostMapping("users/login")
    public ResponseEntity<LoginResponseFormDTO> Login(@RequestBody LoginFormDTO loginFormDTO , HttpSession session){
        Optional<User> member = userRepository.findById(loginFormDTO.getId());
        User memberEntity = member.orElse(null);    // 회원 정보가 없을경우 null로 저장

        if(memberEntity == null){
            return new ResponseEntity<>(new LoginResponseFormDTO(NOT_ID_EXIST,null) , HttpStatus.UNAUTHORIZED);
        }

        if(passwordEncoder.matches(loginFormDTO.getPassword() , memberEntity.getPassword())){   // 비밀번호 일치
            session.setAttribute("user",memberEntity); // session 정보 저장
            String sessionID  = session.getId(); // 세션 ID 가져오기

            // 세션 ID 식별을 위해 전용 DB에 저장
            UserLoginInfo userLoginInfo = UserLoginInfo.builder()
                    .token(sessionID)
                    .id(loginFormDTO.getId())
                    .build();
            userLoginRepository.save(userLoginInfo);
            return new ResponseEntity<>(new LoginResponseFormDTO(LOGIN_SUCCESS , sessionID) , HttpStatus.OK);
        }else{  // 아이디나 비밀번호 틀림
            return new ResponseEntity<>(new LoginResponseFormDTO(LOGIN_FAILURE , null) , HttpStatus.UNAUTHORIZED);
        }
        
    }

    //로그아웃 API URL + LOGIC
    @PostMapping("/users/logout")
    public ResponseEntity<String> logout(HttpServletRequest request , HttpSession session) {
        String authHeader = request.getHeader("Authorization");  // 헤더에서 Authorization 토큰 추출

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 이후의 토큰 추출

            if (session != null && userLoginRepository.existsById(token)) {  // 세션 ID 정보를 가지고 있다면
                session.invalidate(); // 세션 무효화
                userLoginRepository.deleteById(token);  // 로그인 DB 정보에 있는 유저 삭제
                return new ResponseEntity<>(LOGOUT_SUCCESS, HttpStatus.OK);     // 로그아웃 성공
            } else {
                return new ResponseEntity<>(SESSION_NULL_FAILURE, HttpStatus.BAD_REQUEST);  // 세션이 존재하지 않음
            }
        } else {
            return new ResponseEntity<>(HEADER_NULL, HttpStatus.BAD_REQUEST);   // 헤더가 존재하지 않음
        }
    }

    // 사용자 정보 업데이트
    @PostMapping("users/update")
    public ResponseEntity Update(@RequestBody UserUpdateFormDTO userUpdateFormDTO , HttpServletRequest request){
        String authHeader = request.getHeader("Authorization"); // 요청한 유저의 세션 ID

        if(authHeader!=null && authHeader.startsWith("Bearer ")){   // 로그인한 유저의 정보가 있다면
            return userService.update(userUpdateFormDTO , request); // update 로직 수행
        }else{
            return new ResponseEntity<>(HEADER_NULL , HttpStatus.BAD_REQUEST);  // 로그인 정보가 없으면
        }
    }

}
