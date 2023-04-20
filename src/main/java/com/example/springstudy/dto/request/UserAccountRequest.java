package com.example.springstudy.dto.request;

import com.example.springstudy.annotation.Password;
import com.example.springstudy.annotation.Username;
import com.example.springstudy.domain.UserAccount;
import lombok.Getter;

@Getter
public class UserAccountRequest {

  @Username
  private String username;
  @Password
  private String password;

  public static UserAccount toEntity(UserAccountRequest userAccountRequest) {
    return UserAccount.of(
        userAccountRequest.getUsername(),
        userAccountRequest.getPassword()
    );
  }
}
