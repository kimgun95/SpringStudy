package com.example.springstudy.jwt;

import com.example.springstudy.dto.response.StatusResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  //HttpServletRequest : 요청으로 오는 http 객체
  //FilterChain : filter는 체인으로 연결되어 있고, 메서드가 정상적으로 마지막까지 실행되면 doFilter()를 통해 다음 필터로 넘어간다.
  //              하지만 예외가 발생한다면 그 예외는 이전 필터로 넘어간다.
  //doFilterInternal() : 인증 처리를 하는 메서드
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    String token = jwtUtil.resolveToken(request);
    //분기문이 있는 이유, 토큰이 필요 없는 경우(로그인 전 상황)는 doFilter()를 통해 다음 필터로 넘어간다.
    if(token != null) {
      if(!jwtUtil.validateToken(token)){
        jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value());
        return;
      }
      Claims info = jwtUtil.getUserInfoFromToken(token);
      setAuthentication(info.getSubject());
    }
    //인증 객체가 담긴 SecurityContextHolder는 다음 필터로 넘어가게 되면 요청이 인증되었다는 것을 시큐리티에서 인지한다.
    //그렇게 컨트롤러까지 요청이 넘어가게 된다.
    filterChain.doFilter(request,response);
  }

  public void setAuthentication(String username) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Authentication authentication = jwtUtil.createAuthentication(username);
    context.setAuthentication(authentication);
    //SecurityContextHolder에 인증이 된다.
    SecurityContextHolder.setContext(context);
  }

  //토큰 에러 발생시 커스텀 exception을 만들어 client에게 응답 값을 보내주는 메서드
  public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
    response.setStatus(statusCode);
    response.setContentType("application/json");
    try {
      //dto를 생성해 objectMapper로 변환 후 json 형태로 client에게 응답한다.
      String json = new ObjectMapper().writeValueAsString(new StatusResponse(msg, statusCode));
      response.getWriter().write(json);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

}