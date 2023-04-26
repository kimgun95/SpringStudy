package com.example.springstudy.controller;

import com.example.springstudy.dto.ArticleDto;
import com.example.springstudy.dto.request.ArticleRequest;
import com.example.springstudy.dto.response.ArticleResponse;
import com.example.springstudy.dto.response.StatusResponse;
import com.example.springstudy.security.UserDetailsImpl;
import com.example.springstudy.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody @Valid ArticleRequest articleRequest
  ) {
    return ResponseEntity.ok(articleService.saveArticle(userDetails.getUser(), ArticleDto.from(articleRequest)));
  }

  @PutMapping("/articles/{articleId}")
  public ResponseEntity<ArticleResponse> updateArticle(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long articleId,
      @RequestBody @Valid ArticleRequest articleRequest
  ) {
    return ResponseEntity.ok(articleService.updateArticle(userDetails.getUser(), articleId, ArticleDto.from(articleRequest)));
  }

  @DeleteMapping ("/articles/{articleId}")
  public ResponseEntity<StatusResponse> deleteArticle(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long articleId
  ) {
    return ResponseEntity.ok(articleService.deleteArticle(userDetails.getUser(), articleId));
  }

}
