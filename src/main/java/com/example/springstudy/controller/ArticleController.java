package com.example.springstudy.controller;

import com.example.springstudy.dto.ArticleDto;
import com.example.springstudy.dto.request.ArticleRequest;
import com.example.springstudy.dto.response.ArticleResponse;
import com.example.springstudy.dto.response.StatusResponse;
import com.example.springstudy.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ArticleController {

  private final ArticleService articleService;

  @GetMapping("/articles")
  public ResponseEntity<List<ArticleResponse>> getArticleList() {
    return ResponseEntity.ok(articleService.searchArticles());
  }

  @GetMapping("/articles/{articleId}")
  public ResponseEntity<ArticleResponse> getArticle(@PathVariable Long articleId) {
    return ResponseEntity.ok(articleService.searchArticle(articleId));
  }

  @PostMapping("/articles")
  public ResponseEntity<ArticleResponse> saveArticle(
      @RequestBody @Valid ArticleRequest articleRequest,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(articleService.saveArticle(ArticleDto.from(articleRequest), request));
  }

  @PutMapping("/articles/{articleId}")
  public ResponseEntity<ArticleResponse> updateArticle(
      @PathVariable Long articleId,
      @RequestBody @Valid ArticleRequest articleRequest,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(articleService.updateArticle(articleId, ArticleDto.from(articleRequest), request));
  }

  @DeleteMapping ("/articles/{articleId}")
  public ResponseEntity<StatusResponse> deleteArticle(
      @PathVariable Long articleId,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(articleService.deleteArticle(articleId, request));
  }

}
