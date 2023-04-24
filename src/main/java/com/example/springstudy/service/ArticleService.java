package com.example.springstudy.service;

import com.example.springstudy.domain.Article;
import com.example.springstudy.domain.UserAccount;
import com.example.springstudy.domain.constant.UserAccountRole;
import com.example.springstudy.dto.ArticleDto;
import com.example.springstudy.dto.response.ArticleResponse;
import com.example.springstudy.dto.response.StatusResponse;
import com.example.springstudy.exception.ArticleErrorResult;
import com.example.springstudy.exception.ArticleException;
import com.example.springstudy.jwt.JwtUtil;
import com.example.springstudy.repository.ArticleRepository;
import com.example.springstudy.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  public List<ArticleResponse> searchArticles() {
    return articleRepository.findAll()
        .stream()
        .map(ArticleResponse::from)
        .sorted(Comparator.comparing(ArticleResponse::getCreatedAt).reversed())
        .collect(Collectors.toList());
  }

  public ArticleResponse searchArticle(final Long articleId) {
    return articleRepository.findById(articleId)
        .map(ArticleResponse::from)
        .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
  }

  @Transactional
  public ArticleResponse saveArticle(final ArticleDto articleDto, final HttpServletRequest request) {
    final UserAccount user = getUserAccount(request);

    articleDto.setUserAccount(user);
    final Article article = articleRepository.saveAndFlush(ArticleDto.toEntity(articleDto));

    return ArticleResponse.from(article);
  }

  @Transactional
  public ArticleResponse updateArticle(final Long articleId, final ArticleDto articleDto, final HttpServletRequest request) {
    final UserAccount user = getUserAccount(request);
    try {
      final Article getArticle = articleRepository.getReferenceById(articleId);

      if (user.getRole() == UserAccountRole.ADMIN || getArticle.getUserAccount().getUsername().equals(user.getUsername())) {
        getArticle.setContent(articleDto.getContent());
        articleRepository.flush();
        return ArticleResponse.from(getArticle);
      }

    } catch (EntityNotFoundException e) {
      throw new ArticleException(ArticleErrorResult.ARTICLE_NOT_FOUND);
    }
    throw new ArticleException(ArticleErrorResult.NOT_ARTICLE_OWNER);
  }

  @Transactional
  public StatusResponse deleteArticle(final Long articleId, final HttpServletRequest request) {
    final UserAccount user = getUserAccount(request);
    try {
      final Article getArticle = articleRepository.getReferenceById(articleId);

      if (user.getRole() == UserAccountRole.ADMIN || getArticle.getUserAccount().getUsername().equals(user.getUsername())) {
        articleRepository.deleteById(getArticle.getId());
        articleRepository.flush();
        return new StatusResponse("게시글 삭제 성공", 200);
      }

    } catch (EntityNotFoundException | EmptyResultDataAccessException e) {
      throw new ArticleException(ArticleErrorResult.ARTICLE_NOT_FOUND);
    }
    throw new ArticleException(ArticleErrorResult.NOT_ARTICLE_OWNER);
  }

  UserAccount getUserAccount(final HttpServletRequest request) {
    final String token = jwtUtil.resolveToken(request);

    if (token != null) {
      if (jwtUtil.validateToken(token)) {
        final Claims claims = jwtUtil.getUserInfoFromToken(token);

        final UserAccount user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
            () -> new ArticleException(ArticleErrorResult.USER_NOT_FOUND)
        );

        return user;
      }
    }
    throw new ArticleException(ArticleErrorResult.INVALID_TOKEN);
  }

}
