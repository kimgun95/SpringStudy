package com.example.springstudy.service;

import com.example.springstudy.jwt.JwtUtil;
import com.example.springstudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
}
