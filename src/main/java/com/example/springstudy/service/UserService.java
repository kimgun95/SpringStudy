package com.example.springstudy.service;

import com.example.springstudy.domain.UserAccount;
import com.example.springstudy.domain.constant.UserAccountRole;
import com.example.springstudy.dto.response.UserAccountResponse;
import com.example.springstudy.exception.ArticleErrorResult;
import com.example.springstudy.exception.ArticleException;
import com.example.springstudy.jwt.JwtUtil;
import com.example.springstudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
  private final PasswordEncoder passwordEncoder;

  public UserAccountResponse login(final UserAccount userAccount, HttpServletResponse response) {
    final String username = userAccount.getUsername();
    final String password = userAccount.getPassword();

    final UserAccount getUser = userRepository.findByUsername(username).orElseThrow(
        () -> new ArticleException(ArticleErrorResult.USER_NOT_FOUND)
    );

    if (!passwordEncoder.matches(password, getUser.getPassword()))
      throw new ArticleException(ArticleErrorResult.USER_NOT_FOUND);

    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(getUser.getUsername(), getUser.getRole()));

    return new UserAccountResponse("로그인 성공", 200);
  }

  @Transactional
  public UserAccountResponse signup(final UserAccount userAccount) {
    final String username = userAccount.getUsername();
    final String password = passwordEncoder.encode(userAccount.getPassword());

    if (userRepository.findByUsername(username).isPresent())
      throw new ArticleException(ArticleErrorResult.DUPLICATED_USER_REGISTER);

    userAccount.setPassword(password);
    userAccount.setRole(UserAccountRole.USER);

    userRepository.save(userAccount);
    return new UserAccountResponse("회원가입 성공", 200);
  }
}
