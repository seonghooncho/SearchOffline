package com.selfProject.SearchOffline.dto;

import com.selfProject.SearchOffline.entity.FileEntity;
import com.selfProject.SearchOffline.entity.MarketEntity;
import com.selfProject.SearchOffline.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.util.List;

/**
 * User DTO 클래스
 */
public class UserDTO {

    //User 생성과 수정을 처리할 요청(Request) 클래스
    //서버가 사용자에게 요청, 사용자가 서버에 전달
    //회원가입, 개인정보수정
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private Long userID;

        @NotBlank(message = "아이디가 비어있습니다.")
        private String userEmail;

        @NotBlank(message = "비밀번호가 비어있습니다.")
        private String userPassword;

        @NotBlank(message = "닉네임이 비어있습니다.")
        private String userName;

        public UserEntity toEntity() {
            return UserEntity.builder()
                    .userID(userID)
                    .userEmail(userEmail)
                    .userPassword(userPassword)
                    .userName(userName)
                    .build();
        }
    }

    //User 정보를 리턴할 응답(Response) 클래스
    //사용자에게 전달, 화면에 출력될 정보(수정 불필요)
    //개인정보수정에서만 사용

    @Getter
    public static class Response {
        private final Long userID;
        private final String userEmail;
        private final String userPassword;
        private final String userName;
        private final FileDTO.Response userImage;

        /* Entity -> DTO */
        public Response(UserEntity userEntity) {
            this.userID = userEntity.getUserID();
            this.userEmail = userEntity.getUserEmail();
            this.userPassword = userEntity.getUserPassword();
            this.userName = userEntity.getUserName();
            this.userImage = new FileDTO.Response(userEntity.getUserImage());
        }
    }
}
