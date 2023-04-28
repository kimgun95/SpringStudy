package com.example.springstudy.service;

import com.example.springstudy.domain.Article;
import com.example.springstudy.domain.UserAccount;
import com.example.springstudy.domain.constant.UserAccountRole;
import com.example.springstudy.dto.ArticleDto;
import com.example.springstudy.dto.response.ArticleResponse;
import com.example.springstudy.dto.response.StatusResponse;
import com.example.springstudy.exception.ArticleErrorResult;
import com.example.springstudy.exception.ArticleException;
import com.example.springstudy.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ArticleService {

  private final ArticleRepository articleRepository;

  public List<ArticleResponse> searchArticles() {
    return articleRepository.findAllJoinFetch()
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
  public ArticleResponse saveArticle(final UserAccount user, final ArticleDto articleDto) {
    articleDto.setUserAccount(user);
    final Article article = articleRepository.saveAndFlush(ArticleDto.toEntity(articleDto));

    return ArticleResponse.from(article);
  }

  @Transactional
  public ArticleResponse updateArticle(final UserAccount user, final Long articleId, final ArticleDto articleDto) {
    try {
      final Article getArticle = articleRepository.getReferenceById(articleId);

      if (user.getRole() == UserAccountRole.ADMIN || getArticle.getUserAccount().getUsername().equals(user.getUsername())) {
        getArticle.setTitle(articleDto.getTitle());
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
  public StatusResponse deleteArticle(final UserAccount user, final Long articleId) {
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

}
