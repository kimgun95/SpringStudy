package com.example.springstudy.service;

import com.example.springstudy.domain.Article;
import com.example.springstudy.domain.UserAccount;
import com.example.springstudy.domain.constant.UserAccountRole;
import com.example.springstudy.dto.ArticleDto;
import com.example.springstudy.dto.response.ArticleResponse;
import com.example.springstudy.dto.response.StatusResponse;
import com.example.springstudy.jwt.JwtUtil;
import com.example.springstudy.repository.ArticleRepository;
import com.example.springstudy.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  @Transactional(readOnly = true)
  public List<ArticleResponse> searchArticles() {
    return articleRepository.findAll()
        .stream()
        .map(ArticleResponse::from)
        .sorted(Comparator.comparing(ArticleResponse::getCreatedAt).reversed())
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public ArticleResponse searchArticle(final Long articleId) {
    return articleRepository.findById(articleId)
        .map(ArticleResponse::from)
        .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
  }

  public ArticleResponse saveArticle(final ArticleDto articleDto, final HttpServletRequest request) {
    final String token = jwtUtil.resolveToken(request);

    if (token != null) {
      if (jwtUtil.validateToken(token)) {
        final Claims claims = jwtUtil.getUserInfoFromToken(token);

        final UserAccount user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
            () -> new EntityNotFoundException("사용자가 존재하지 않습니다.")
        );

        if (articleDto.getTitle() == null || articleDto.getContent() == null)
          throw new IllegalArgumentException("게시글 작성 실패, null 값을 받고 있습니다.");

        final Article article = articleRepository.saveAndFlush(ArticleDto.toEntity(articleDto, user));

        return ArticleResponse.from(article);
      }
    }
    throw new IllegalArgumentException("토큰이 null 입니다.");
  }

  public ArticleResponse updateArticle(final Long articleId, final ArticleDto articleDto, final HttpServletRequest request) {
    final String token = jwtUtil.resolveToken(request);

    if (token != null) {
      if (jwtUtil.validateToken(token)) {
        final Claims claims = jwtUtil.getUserInfoFromToken(token);

        final UserAccount user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
            () -> new EntityNotFoundException("사용자가 존재하지 않습니다.")
        );

        try {
          final Article getArticle = articleRepository.getReferenceById(articleId);
          if (user.getRole() == UserAccountRole.ADMIN || getArticle.getUserAccount().getUsername().equals(claims.getSubject())) {
            if (articleDto.getTitle() != null) {
              getArticle.setTitle(articleDto.getTitle());
            }
            if (articleDto.getContent() != null) {
              getArticle.setContent(articleDto.getContent());
            }
            articleRepository.flush();
            return ArticleResponse.from(getArticle);
          }

        } catch (EntityNotFoundException e) {
          log.warn("해당 게시글이 존재하지 않습니다. - {}", e.getLocalizedMessage());
          throw new EntityNotFoundException();
        }
      }
    }
    throw new IllegalArgumentException("권한이 없습니다.");
  }

  public StatusResponse deleteArticle(final Long articleId, final HttpServletRequest request) {
    final String token = jwtUtil.resolveToken(request);

    if (token != null) {
      if (jwtUtil.validateToken(token)) {
        final Claims claims = jwtUtil.getUserInfoFromToken(token);

        final UserAccount user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
            () -> new EntityNotFoundException("사용자가 존재하지 않습니다.")
        );

        try {
          final Article getArticle = articleRepository.getReferenceById(articleId);
          if (user.getRole() == UserAccountRole.ADMIN || getArticle.getUserAccount().getUsername().equals(claims.getSubject())) {
            articleRepository.deleteById(getArticle.getId());
            articleRepository.flush();
            return new StatusResponse("게시글 삭제 성공", 200);
          }

        } catch (EntityNotFoundException | EmptyResultDataAccessException e) {
          log.warn("해당 게시글이 존재하지 않습니다. - {}", e.getLocalizedMessage());
          throw new EntityNotFoundException();
        }
      }
    }
    throw new IllegalArgumentException("권한이 없습니다.");
  }

}
