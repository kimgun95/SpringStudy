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
import com.example.springstudy.repository.ArticleCommentRepository;
import com.example.springstudy.repository.ArticleRepository;
import com.example.springstudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ArticleCommentService {

  private final ArticleCommentRepository articleCommentRepository;
  private final UserRepository userRepository;
  private final ArticleRepository articleRepository;

  @Transactional
  public ArticleCommentResponse saveComment(final UserAccount user, final Long articleId, final ArticleCommentDto articleCommentDto) {
    final Article article = articleRepository.findById(articleId).orElseThrow(
        () -> new ArticleException(ArticleErrorResult.ARTICLE_NOT_FOUND)
    );

    articleCommentDto.setUserAccount(user);
    articleCommentDto.setArticle(article);
    final ArticleComment articleComment = articleCommentRepository.saveAndFlush(ArticleCommentDto.toEntity(articleCommentDto));

    return ArticleCommentResponse.from(articleComment);
  }

  @Transactional
  public ArticleCommentResponse updateComment(final UserAccount user, final Long articleId, final Long commentId, final ArticleCommentDto articleCommentDto) {
    try {
      final ArticleComment articleComment = articleCommentRepository.getReferenceById(commentId);
      final Article article = articleRepository.findById(articleId).orElseThrow(
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
  public StatusResponse deleteComment(final UserAccount user, final Long articleId, final Long commentId) {
    try {
      final ArticleComment articleComment = articleCommentRepository.getReferenceById(commentId);
      final Article article = articleRepository.findById(articleId).orElseThrow(
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

}
