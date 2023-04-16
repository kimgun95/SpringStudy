package com.example.springstudy.service;

import com.example.springstudy.domain.Article;
import com.example.springstudy.domain.constant.StatusResponse;
import com.example.springstudy.dto.ArticleDto;
import com.example.springstudy.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

  private final ArticleRepository articleRepository;

  @Transactional(readOnly = true)
  public List<ArticleDto> searchArticles() {
    return articleRepository.findAll()
        .stream()
        .map(ArticleDto::from)
        .sorted(Comparator.comparing(ArticleDto::getCreatedAt).reversed())
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public ArticleDto searchArticle(Long articleId) {
    return articleRepository.findById(articleId)
        .map(ArticleDto::from)
        .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
  }

  public ArticleDto saveArticle(Article article) {
    Article savedArticle = articleRepository.save(article);
    return ArticleDto.from(savedArticle);
  }

  public ArticleDto updateArticle(Long articleId, Article article) {
    Article getArticle = null;
    try {
      getArticle = articleRepository.getReferenceById(articleId);

      if (article.getPassword().equals(getArticle.getPassword())) {
        if (article.getTitle() != null) { getArticle.setTitle(article.getTitle()); }
        if (article.getContent() != null) { getArticle.setContent(article.getContent()); }
        if (article.getAuthor() != null) { getArticle.setAuthor(article.getAuthor()); }
        articleRepository.flush();
      } else {
        throw new EntityNotFoundException("비밀번호가 틀렸습니다.");
      }
    } catch (EntityNotFoundException e) {
      log.warn("게시글 업데이트 실패 - {}", e.getLocalizedMessage());
    }
    return ArticleDto.from(getArticle);
  }

  public StatusResponse deleteArticle(Long articleId, String password) {
    try {
      Article article = articleRepository.getReferenceById(articleId);
      System.out.println(article.getPassword());
      System.out.println(password);
      if (article.getPassword().equals(password)) {
        articleRepository.deleteById(article.getId());
        articleRepository.flush();
        return StatusResponse.SUCCESS;
      } else {
        throw new Exception("비밀번호가 틀렸습니다.");
      }
    } catch (Exception e) {
      log.warn("게시글 삭제 실패 - {}", e.getLocalizedMessage());
    }
    return StatusResponse.FAIL;
  }

}
