package com.example.springstudy.service;

import com.example.springstudy.domain.Article;
import com.example.springstudy.dto.ArticleDto;
import com.example.springstudy.dto.response.ArticleResponse;
import com.example.springstudy.dto.response.StatusResponse;
import com.example.springstudy.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

  private final ArticleRepository articleRepository;

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

  public ArticleResponse saveArticle(final ArticleDto articleDto) {
    if (articleDto.getTitle() == null || articleDto.getContent() == null || articleDto.getUsername() == null || articleDto.getPassword() == null)
      throw new IllegalArgumentException("게시글 작성 실패, null 값을 받고 있습니다.");
    return ArticleResponse.from(articleRepository.save(ArticleDto.toEntity(articleDto)));
  }

  public ArticleResponse updateArticle(final Long articleId, final ArticleDto articleDto) {
    try {
      Article getArticle = articleRepository.getReferenceById(articleId);

      if (articleDto.getPassword().equals(getArticle.getPassword())) {
        if (articleDto.getTitle() != null) { getArticle.setTitle(articleDto.getTitle()); }
        if (articleDto.getContent() != null) { getArticle.setContent(articleDto.getContent()); }
        if (articleDto.getUsername() != null) { getArticle.setUsername(articleDto.getUsername()); }
        articleRepository.flush();
      } else {
        log.warn("게시글 업데이트 실패, 비밀번호가 틀렸습니다.");
        throw new SecurityException();
      }
      return ArticleResponse.from(getArticle);
    } catch (EntityNotFoundException e) {
      log.warn("게시글 업데이트 실패, 해당 게시글이 존재하지 않습니다. - {}", e.getLocalizedMessage());
      throw new EntityNotFoundException();
    }
  }

  public StatusResponse deleteArticle(final Long articleId, final ArticleDto articleDto) {
    try {
      Article getArticle = articleRepository.getReferenceById(articleId);
      if (getArticle.getPassword().equals(articleDto.getPassword())) {
        articleRepository.deleteById(getArticle.getId());
        articleRepository.flush();
        return new StatusResponse(true);
      } else {
        log.warn("게시글 삭제 실패, 비밀번호가 틀렸습니다.");
        throw new SecurityException();
      }
    } catch (EntityNotFoundException e) {
      log.warn("게시글 삭제 실패, 해당 게시글이 존재하지 않습니다. - {}", e.getLocalizedMessage());
      throw new EntityNotFoundException();
    }
  }

}
