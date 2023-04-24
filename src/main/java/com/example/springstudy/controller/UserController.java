package com.example.springstudy.controller;

import com.example.springstudy.dto.request.UserAccountRequest;
import com.example.springstudy.dto.response.UserAccountResponse;
import com.example.springstudy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {

  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<UserAccountResponse> login(
      @RequestBody @Valid UserAccountRequest userAccountRequest,
      HttpServletResponse response
  ) {
    return ResponseEntity.ok(userService.login(UserAccountRequest.toEntity(userAccountRequest), response));
  }

  @PostMapping("/signup")
  public ResponseEntity<UserAccountResponse> signup(@RequestBody @Valid UserAccountRequest userAccountRequest) {
    return ResponseEntity.ok(userService.signup(UserAccountRequest.toEntity(userAccountRequest)));
  }

}
