package com.cakeme.backend.dto.user;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class AddUserRequestDTO {
    @NotBlank(message = "유저 이름은 필수입니다.")
    private String username;

    @NotBlank(message = "유저 ID는 필수입니다.")
    private String userId;

    @Size(min = 8, message = "비밀번호는 최소 8자리 이상이어야 합니다.") // 비밀번호는 8자리 이상으로 했어요
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String confirmPassword;
}
