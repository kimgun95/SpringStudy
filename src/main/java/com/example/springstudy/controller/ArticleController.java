package com.example.springstudy.controller;

import com.example.springstudy.dto.ArticleDto;
import com.example.springstudy.dto.request.ArticleRequest;
import com.example.springstudy.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ArticleController {

  private final ArticleService articleService;

  @GetMapping("/articles")
  public ResponseEntity<List<ArticleDto>> getArticleList() {
    return ResponseEntity.ok(articleService.searchArticles());
  }

  @GetMapping("/article/{articleId}")
  public ResponseEntity<ArticleDto> getArticle(@PathVariable Long articleId) {
    return ResponseEntity.ok(articleService.searchArticle(articleId));
  }

  @PostMapping("/article")
  public ResponseEntity<ArticleDto> saveArticle(@RequestBody ArticleRequest articleRequest) {
    return ResponseEntity.ok(articleService.saveArticle(ArticleRequest.toEntity(articleRequest)));
  }

  @PutMapping("/article/{articleId}")
  public ResponseEntity<Optional<ArticleDto>> updateArticle(
      @PathVariable Long articleId,
      @RequestBody ArticleRequest articleRequest)
  {
    return ResponseEntity.ok(articleService.updateArticle(articleId, ArticleRequest.toEntity(articleRequest)));
  }

  @DeleteMapping ("/article/{articleId}")
  public ResponseEntity<Map<String, Boolean>> deleteArticle(
      @PathVariable Long articleId,
      @RequestBody HashMap<String, String> password
  ) {
    return ResponseEntity.ok(
        Map.of("success", articleService.deleteArticle(articleId, password.get("password")).getSuccess())
    );
  }

}
