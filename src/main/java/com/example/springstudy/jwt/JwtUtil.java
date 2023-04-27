package com.example.springstudy.jwt;

import com.example.springstudy.domain.constant.UserAccountRole;
import com.example.springstudy.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String AUTHORIZATION_KEY = "auth";
  private static final String BEARER_PREFIX = "Bearer ";
  private static final long TOKEN_TIME = 60 * 60 * 1000L;

  @Value("${jwt.secret.key}")
  private String secretKey;
  private Key key;
  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
  private final UserDetailsServiceImpl userDetailsService;

  @PostConstruct
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    key = Keys.hmacShaKeyFor(bytes);
  }

  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    //StringUtils.hasText : 해당 문자열이 공백(null)인지 확인
    //startsWith : 해당 문자열로 시작하고 있는지 확인
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(BEARER_PREFIX.length());
    }
    return null;
  }

  // 토큰 생성
  public String createToken(String username, UserAccountRole role) {
    Date date = new Date();

    return BEARER_PREFIX +
        Jwts.builder()
            .setSubject(username)
            .claim(AUTHORIZATION_KEY, role)
            .setExpiration(new Date(date.getTime() + TOKEN_TIME))
            .setIssuedAt(date)
            .signWith(key, signatureAlgorithm)
            .compact();
  }

  public boolean validateToken(String token) {
    try {
      //1. JwtParseBuilder 인스턴스 생성
      //2. JWS 서명 검증을 위한 SecretKey(혹은 비대칭 PublicKey)를 지정
      //3. thread safe한 JwtParser를 리턴하기 위해 JwtParseBuilder의 build() 메서드 호출
      //4. 파라미터 값이 token(jws)인 parseClaimsJws() 메서드를 호출한다. 원본 JWS를 만들기 위해
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException | SignatureException e) {
      log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT token, 만료된 JWT token 입니다.");
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
    }
    return false;
  }

  public Claims getUserInfoFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }

  //인증 객체를 생성, 사실 JwtAuthFilter에서 바로 처리할 수 있는 코드인데 이렇게 분리한 이유는 책임을 분리하기 위함이다.
  //인증 처리 후 인증된 토큰을 AuthenticationManager에게 반환하는 것이 정석이다.
  //CustomAuthenticationProvider에서 UserDetailsService를 통해 조회한 정보와 입력받은 비밀번호가 일치하는지 확인한다.
  //DB에 저장된 비밀번호는 암호화가 되어있기에, 입력 비밀번호는 PasswordEncoder를 통해 암호화하여 DB 비밀번호와 매칭한다.
  //매칭되지 않는다면 BadCredentialsException 처리한다.
  //하지만 이 모든 얘기는 AuthenticationProvider를 통한 필터 로직이기 때문에 아래 구현과는 상관이 없다.
  //https://www.toptal.com/spring/spring-security-tutorial 이 글이 코드를 작성하면서 조금의 확신을 갖게 만들었다.
  public Authentication createAuthentication(String username) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

}
