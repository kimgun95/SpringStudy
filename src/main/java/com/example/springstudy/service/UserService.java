package com.example.springstudy.service;

import com.example.springstudy.domain.UserAccount;
import com.example.springstudy.dto.response.UserAccountResponse;
import com.example.springstudy.jwt.JwtUtil;
import com.example.springstudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  @Transactional(readOnly = true)
  public UserAccountResponse login(final UserAccount userAccount, HttpServletResponse response) {
    final String username = userAccount.getUsername();
    final String password = userAccount.getPassword();
    if (username == null || password == null)
      throw new IllegalArgumentException("로그인 실패, null 값을 받고 있습니다.");

    UserAccount getUser = userRepository.findByUsername(username).orElseThrow(
        () -> new EntityNotFoundException("등록된 사용자가 없습니다.")
    );

    if (!getUser.getPassword().equals(password))
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(getUser.getUsername()));
    return new UserAccountResponse("회원가입 성공", 200);
  }
}
