package com.example.springstudy.service;

import com.example.springstudy.domain.Article;
import com.example.springstudy.domain.ArticleComment;
import com.example.springstudy.domain.UserAccount;
import com.example.springstudy.domain.constant.UserAccountRole;
import com.example.springstudy.dto.ArticleCommentDto;
import com.example.springstudy.dto.response.ArticleCommentResponse;
import com.example.springstudy.dto.response.StatusResponse;
import com.example.springstudy.jwt.JwtUtil;
import com.example.springstudy.repository.ArticleCommentRepository;
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

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {

  private final ArticleCommentRepository articleCommentRepository;
  private final UserRepository userRepository;
  private final ArticleRepository articleRepository;
  private final JwtUtil jwtUtil;

  public ArticleCommentResponse saveComment(final ArticleCommentDto articleCommentDto, final HttpServletRequest request) {
    final String token = jwtUtil.resolveToken(request);

    if (token != null) {
      if (jwtUtil.validateToken(token)) {
        final Claims claims = jwtUtil.getUserInfoFromToken(token);

        final UserAccount user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
            () -> new EntityNotFoundException("사용자가 존재하지 않습니다.")
        );
        final Article article = articleRepository.findById(articleCommentDto.getArticleId()).orElseThrow(
            () -> new EntityNotFoundException("게시글이 존재하지 않습니다.")
        );

        articleCommentDto.setUserAccount(user);
        articleCommentDto.setArticle(article);
        final ArticleComment articleComment = articleCommentRepository.saveAndFlush(ArticleCommentDto.toEntity(articleCommentDto));

        return ArticleCommentResponse.from(articleComment);
      }
    }
    throw new IllegalArgumentException("토큰이 null 입니다.");
  }

  public ArticleCommentResponse updateComment(final Long commentId, final ArticleCommentDto articleCommentDto, final HttpServletRequest request) {
    final String token = jwtUtil.resolveToken(request);

    if (token != null) {
      if (jwtUtil.validateToken(token)) {
        final Claims claims = jwtUtil.getUserInfoFromToken(token);

        final UserAccount user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
            () -> new EntityNotFoundException("사용자가 존재하지 않습니다.")
        );

        try {
          final ArticleComment articleComment = articleCommentRepository.getReferenceById(commentId);
          final Article article = articleRepository.findById(articleComment.getArticle().getId()).orElseThrow(
              () -> new EntityNotFoundException("게시글이 존재하지 않습니다.")
          );
          if (user.getRole() == UserAccountRole.ADMIN || articleComment.getUserAccount().getUsername().equals(user.getUsername())) {
            articleComment.setContent(articleCommentDto.getContent());
            articleCommentRepository.flush();
            return ArticleCommentResponse.from(articleComment);
          }

        } catch (EntityNotFoundException e) {
          log.warn("해당 댓글이 존재하지 않습니다. - {}", e.getLocalizedMessage());
          throw new EntityNotFoundException();
        }
      }
    }
    throw new IllegalArgumentException("권한이 없습니다.");
  }

  public StatusResponse deleteComment(final Long commentId, final HttpServletRequest request) {
    final String token = jwtUtil.resolveToken(request);

    if (token != null) {
      if (jwtUtil.validateToken(token)) {
        final Claims claims = jwtUtil.getUserInfoFromToken(token);

        final UserAccount user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
            () -> new EntityNotFoundException("사용자가 존재하지 않습니다.")
        );

        try {
          final ArticleComment articleComment = articleCommentRepository.getReferenceById(commentId);
          final Article article = articleRepository.findById(articleComment.getArticle().getId()).orElseThrow(
              () -> new EntityNotFoundException("게시글이 존재하지 않습니다.")
          );
          if (user.getRole() == UserAccountRole.ADMIN || articleComment.getUserAccount().getUsername().equals(user.getUsername())) {
            articleCommentRepository.deleteById(commentId);
            articleCommentRepository.flush();
            return new StatusResponse("댓글 삭제 성공", 200);
          }

        } catch (EntityNotFoundException | EmptyResultDataAccessException e) {
          log.warn("해당 댓글이 존재하지 않습니다. - {}", e.getLocalizedMessage());
          throw new EntityNotFoundException();
        }
      }
    }
    throw new IllegalArgumentException("권한이 없습니다.");
  }
}
