package com.example.spring.user.service;


import com.example.spring.user.domain.Profile;
import com.example.spring.user.domain.User;
import com.example.spring.user.dto.ProfileCreateFormDTO;
import com.example.spring.user.repository.ProfileRepository;
import com.example.spring.user.repository.UserLoginRepository;
import com.example.spring.user.repository.UserRepository;
import com.example.spring.user.service.interfaces.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {


    private final ProfileRepository profileRepository;

    private final UserLoginRepository userLoginRepository;

    private final UserRepository userRepository;


    // 응답 메시지 상수화

    private static final String CREATE_SUCCESS = "프로필 생성 성공";
    private static final String CREATE_FAILURE = "프로필 생성 실패";

    // Detail 조회
    private static final String DETAIL_SUCCESS = "프로필 조회 성공";

    private static final String DETAIL_FAILURE = "프로필이 존재하지 않습니다.";

    private static final String IMAGE_UPLOAD_FAILURE = "이미지 업로드 실패";

    private static final String ID_FAILURE = "존재하는 아이디가 없습니다.";

    @Override
    public ResponseEntity create(MultipartFile multipartFile , ProfileCreateFormDTO profileCreateFormDTO , HttpServletRequest request){
        String authHeader = request.getHeader("Authorization"); // 로그인한 회원의 session id

        String token = authHeader.substring(7);  // Bearer 을 제외한 나머지 부분

        String LoginId = userLoginRepository.findUserIdByToken(token).toString();   // 토큰으로 로그인한 아이디 정보 찾기

        LoginId = LoginId.replaceAll("[\\[\\]]", "");   // 대괄호 제거 -> 불일치 에러가 발생함



        User user = userRepository.findById(LoginId).orElse(null);

        if(user == null){
            return new ResponseEntity<>(ID_FAILURE , HttpStatus.NOT_FOUND);
        }

        Profile profile = profileRepository.findByProfileCustom(user);   // 유저 아이디를 통한 정보 조회


        if(profile == null){
            String imagePath = null;
            if(!multipartFile.isEmpty()){
                try{
                    String uploadDir = "src/main/files/profileImages";
                    String originName = multipartFile.getOriginalFilename();
                    //String contentType = multipartFile.getContentType();
                    //long size = multipartFile.getSize();

                    String fileName = UUID.randomUUID().toString() + "_" + originName; // 파일 중복 이름 방지

                    File uploadFile = new File(uploadDir , fileName);
                    multipartFile.transferTo(uploadFile);   // 파일저장

                    imagePath = Paths.get(uploadDir,fileName).toString();   // 최종 파일 경로

                }catch (IOException e){
                    e.printStackTrace();
                    return new ResponseEntity<>(IMAGE_UPLOAD_FAILURE , HttpStatus.INTERNAL_SERVER_ERROR);
                }

            }
            // 프로필 생성
            Profile newProfile = Profile.builder()
                    .image(imagePath)
                    .nickname(profileCreateFormDTO.getNickname())
                    .message(profileCreateFormDTO.getMessage())
                    .user(user)
                    .build();

            profileRepository.save(newProfile); // 새로운 프로필 저장


            return new ResponseEntity<>(CREATE_SUCCESS , HttpStatus.OK);
        }else{
            return new ResponseEntity<>(CREATE_FAILURE , HttpStatus.OK);
        }
    }


    @Override
    public ResponseEntity detail(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");

        String token = authHeader.substring(7);

        String LoginId = userLoginRepository.findUserIdByToken(token).toString();

        LoginId = LoginId.replaceAll("[\\[\\]]", "");

        User user = userRepository.findById(LoginId).orElse(null);


        if(user == null){
            return new ResponseEntity<>(ID_FAILURE , HttpStatus.NOT_FOUND);
        }

        Profile profile = profileRepository.findByProfileCustom(user);  // 프로필 정보 조회

        if(profile != null){    // 프로필이 존재할경우
            return new ResponseEntity<>(DETAIL_SUCCESS , HttpStatus.OK);
        }else{
            return new ResponseEntity<>(DETAIL_FAILURE , HttpStatus.OK);
        }



    }

}
