package com.example.springstudy.controller;

import com.example.springstudy.dto.ArticleCommentDto;
import com.example.springstudy.dto.request.ArticleCommentRequest;
import com.example.springstudy.dto.response.ArticleCommentResponse;
import com.example.springstudy.dto.response.StatusResponse;
import com.example.springstudy.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ArticleCommentController {

  private final ArticleCommentService articleCommentService;

  @PostMapping("/articles/{articleId}/comments")
  public ResponseEntity<ArticleCommentResponse> saveComment(
      @PathVariable Long articleId,
      @RequestBody @Valid ArticleCommentRequest articleCommentRequest,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(articleCommentService.saveComment(articleId, ArticleCommentDto.from(articleCommentRequest), request));
  }

  @PutMapping("/articles/{articleId}/comments/{commentId}")
  public ResponseEntity<ArticleCommentResponse> updateComment(
      @PathVariable Long articleId,
      @PathVariable Long commentId,
      @RequestBody @Valid ArticleCommentRequest articleCommentRequest,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(articleCommentService.updateComment(articleId, commentId, ArticleCommentDto.from(articleCommentRequest), request));
  }

  @DeleteMapping ("/articles/{articleId}/comments/{commentId}")
  public ResponseEntity<StatusResponse> deleteComment(
      @PathVariable Long articleId,
      @PathVariable Long commentId,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(articleCommentService.deleteComment(articleId, commentId, request));
  }
}
