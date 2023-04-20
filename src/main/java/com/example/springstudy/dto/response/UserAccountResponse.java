package com.example.springstudy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAccountResponse {

  private String msg;
  private int statusCode;
}
