package com.example.springstudy.service;

import com.example.springstudy.domain.UserAccount;
import com.example.springstudy.domain.constant.UserAccountRole;
import com.example.springstudy.dto.response.UserAccountResponse;
import com.example.springstudy.jwt.JwtUtil;
import com.example.springstudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  public UserAccountResponse login(final UserAccount userAccount, HttpServletResponse response) {
    final String username = userAccount.getUsername();
    final String password = userAccount.getPassword();

    final UserAccount getUser = userRepository.findByUsername(username).orElseThrow(
        () -> new EntityNotFoundException("등록된 사용자가 없습니다.")
    );

    if (!getUser.getPassword().equals(password))
      throw new SecurityException("비밀번호가 일치하지 않습니다.");

    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(getUser.getUsername(), getUser.getRole()));

    return new UserAccountResponse("로그인 성공", 200);
  }

  @Transactional
  public UserAccountResponse signup(final UserAccount userAccount) {
    final String username = userAccount.getUsername();
    final String password = userAccount.getPassword();

    if (userRepository.findByUsername(username).isPresent())
      throw new EntityExistsException("중복된 사용자가 존재합니다.");

    userAccount.setRole(UserAccountRole.USER);

    userRepository.save(userAccount);
    return new UserAccountResponse("회원가입 성공", 200);
  }
}
