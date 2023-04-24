package com.example.springstudy.service;

import com.example.springstudy.domain.Article;
import com.example.springstudy.domain.ArticleComment;
import com.example.springstudy.domain.UserAccount;
import com.example.springstudy.domain.constant.UserAccountRole;
import com.example.springstudy.dto.ArticleCommentDto;
import com.example.springstudy.dto.response.ArticleCommentResponse;
import com.example.springstudy.dto.response.StatusResponse;
import com.example.springstudy.exception.ArticleErrorResult;
import com.example.springstudy.exception.ArticleException;
import com.example.springstudy.jwt.JwtUtil;
import com.example.springstudy.repository.ArticleCommentRepository;
import com.example.springstudy.repository.ArticleRepository;
import com.example.springstudy.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ArticleCommentService {

  private final ArticleCommentRepository articleCommentRepository;
  private final UserRepository userRepository;
  private final ArticleRepository articleRepository;
  private final JwtUtil jwtUtil;

  @Transactional
  public ArticleCommentResponse saveComment(final ArticleCommentDto articleCommentDto, final HttpServletRequest request) {
    final UserAccount user = getUserAccount(request);

    final Article article = articleRepository.findById(articleCommentDto.getArticleId()).orElseThrow(
        () -> new ArticleException(ArticleErrorResult.ARTICLE_NOT_FOUND)
    );

    articleCommentDto.setUserAccount(user);
    articleCommentDto.setArticle(article);
    final ArticleComment articleComment = articleCommentRepository.saveAndFlush(ArticleCommentDto.toEntity(articleCommentDto));

    return ArticleCommentResponse.from(articleComment);
  }

  @Transactional
  public ArticleCommentResponse updateComment(final Long commentId, final ArticleCommentDto articleCommentDto, final HttpServletRequest request) {
    final UserAccount user = getUserAccount(request);

    try {
      final ArticleComment articleComment = articleCommentRepository.getReferenceById(commentId);
      final Article article = articleRepository.findById(articleComment.getArticle().getId()).orElseThrow(
          () -> new ArticleException(ArticleErrorResult.ARTICLE_NOT_FOUND)
      );

      if (user.getRole() == UserAccountRole.ADMIN || articleComment.getUserAccount().getUsername().equals(user.getUsername())) {
        articleComment.setContent(articleCommentDto.getContent());
        articleCommentRepository.flush();
        return ArticleCommentResponse.from(articleComment);
      }

    } catch (EntityNotFoundException e) {
      throw new ArticleException(ArticleErrorResult.COMMENT_NOT_FOUND);
    }

    throw new ArticleException(ArticleErrorResult.NOT_ARTICLE_OWNER);
  }

  @Transactional
  public StatusResponse deleteComment(final Long commentId, final HttpServletRequest request) {
    final UserAccount user = getUserAccount(request);

    try {
      final ArticleComment articleComment = articleCommentRepository.getReferenceById(commentId);
      final Article article = articleRepository.findById(articleComment.getArticle().getId()).orElseThrow(
          () -> new ArticleException(ArticleErrorResult.ARTICLE_NOT_FOUND)
      );

      if (user.getRole() == UserAccountRole.ADMIN || articleComment.getUserAccount().getUsername().equals(user.getUsername())) {
        articleCommentRepository.deleteById(commentId);
        articleCommentRepository.flush();
        return new StatusResponse("댓글 삭제 성공", 200);
      }

    } catch (EntityNotFoundException | EmptyResultDataAccessException e) {
      throw new ArticleException(ArticleErrorResult.COMMENT_NOT_FOUND);
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
