package com.example.springstudy.config;

import com.example.springstudy.jwt.JwtAuthFilter;
import com.example.springstudy.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private final JwtUtil jwtUtil;

  //정적 자원들에 대해서는 시큐리티를 적용하지 않는다.
  //어떤 요청에 대해서는 로그인을 요구하고, 어떤 요청에 대해선 로그인을 요구하지 않을지 설정한다.
  //addFilterBefore() : Custom Filter 등록, 이곳에선 JWT 인증/인가를 위한 필터를 등록한다.
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

    return httpSecurity
        .csrf().disable()
        .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(PathRequest.toH2Console()).permitAll()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
            .antMatchers( "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
            .antMatchers("/login", "/signup").permitAll()
            .antMatchers(HttpMethod.GET, "/articles/**").permitAll()
            .anyRequest().authenticated()
              .and()
            .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
        )
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
