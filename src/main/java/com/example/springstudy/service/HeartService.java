package com.example.springstudy.service;

import com.example.springstudy.domain.Article;
import com.example.springstudy.domain.ArticleComment;
import com.example.springstudy.domain.Heart;
import com.example.springstudy.domain.UserAccount;
import com.example.springstudy.dto.response.StatusResponse;
import com.example.springstudy.exception.ArticleErrorResult;
import com.example.springstudy.exception.ArticleException;
import com.example.springstudy.repository.ArticleCommentRepository;
import com.example.springstudy.repository.ArticleRepository;
import com.example.springstudy.repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class HeartService {

  private final HeartRepository heartRepository;
  private final ArticleRepository articleRepository;
  private final ArticleCommentRepository articleCommentRepository;

  @Transactional
  public StatusResponse heartArticle(final UserAccount user, final Long articleId) {
    try {
      final Article article = articleRepository.getReferenceById(articleId);
      final Optional<Heart> getHeart = heartRepository.findByUserIdAndArticleId(user.getId(), article.getId());
      if (getHeart.isEmpty()) {
        article.setHeartCount(article.getHeartCount() + 1);
        heartRepository.saveAndFlush(Heart.of(user, article));
      } else {
        article.setHeartCount(article.getHeartCount() - 1);
        heartRepository.deleteByUserIdAndArticleId(user.getId(), article.getId());
      }
      return new StatusResponse("게시글 좋아요 성공", 200);
    } catch (EntityNotFoundException e) {
      throw new ArticleException(ArticleErrorResult.ARTICLE_NOT_FOUND);
    }
  }

  @Transactional
  public StatusResponse heartArticleComment(final UserAccount user, final Long articleId, final Long commentId) {
    try {
      final ArticleComment comment = articleCommentRepository.getReferenceById(commentId);
      final Article article = articleRepository.findById(articleId).orElseThrow(
          () -> new ArticleException(ArticleErrorResult.ARTICLE_NOT_FOUND)
      );
      final Optional<Heart> getHeart = heartRepository.findByUserIdAndArticleIdAndCommentId(user.getId(), article.getId(), comment.getId());
      if (getHeart.isEmpty()) {
        comment.setHeartCount(comment.getHeartCount() + 1);
        heartRepository.saveAndFlush(Heart.of(user, article, comment));
      } else {
        comment.setHeartCount(comment.getHeartCount() - 1);
        heartRepository.deleteByUserIdAndArticleIdAndCommentId(user.getId(), article.getId(), comment.getId());
      }
      return new StatusResponse("댓글 좋아요 성공", 200);
    } catch (EntityNotFoundException e) {
      throw new ArticleException(ArticleErrorResult.COMMENT_NOT_FOUND);
    }
  }

}
