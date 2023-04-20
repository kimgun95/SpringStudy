package com.example.springstudy.dto.request;

import com.example.springstudy.domain.UserAccount;
import lombok.Getter;

@Getter
public class UserAccountRequest {

  private String username;
  private String password;

  public static UserAccount toEntity(UserAccountRequest userAccountRequest) {
    return UserAccount.of(
        userAccountRequest.getUsername(),
        userAccountRequest.getPassword()
    );
  }
}
