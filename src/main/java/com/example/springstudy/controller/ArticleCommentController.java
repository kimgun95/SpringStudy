package com.example.springstudy.controller;

import com.example.springstudy.dto.ArticleCommentDto;
import com.example.springstudy.dto.request.ArticleCommentRequest;
import com.example.springstudy.dto.response.ArticleCommentResponse;
import com.example.springstudy.dto.response.StatusResponse;
import com.example.springstudy.security.UserDetailsImpl;
import com.example.springstudy.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ArticleCommentController {

  private final ArticleCommentService articleCommentService;

  @PostMapping("/articles/{articleId}/comments")
  public ResponseEntity<ArticleCommentResponse> saveComment(
      @AuthenticationPrincipal final UserDetailsImpl userDetails,
      @PathVariable final Long articleId,
      @RequestBody @Valid final ArticleCommentRequest articleCommentRequest
  ) {
    return ResponseEntity.ok(articleCommentService.saveComment(userDetails.getUser(), articleId, ArticleCommentDto.from(articleCommentRequest)));
  }

  @PutMapping("/articles/{articleId}/comments/{commentId}")
  public ResponseEntity<ArticleCommentResponse> updateComment(
      @AuthenticationPrincipal final UserDetailsImpl userDetails,
      @PathVariable final Long articleId,
      @PathVariable final Long commentId,
      @RequestBody @Valid final ArticleCommentRequest articleCommentRequest
  ) {
    return ResponseEntity.ok(articleCommentService.updateComment(userDetails.getUser(), articleId, commentId, ArticleCommentDto.from(articleCommentRequest)));
  }

  @DeleteMapping ("/articles/{articleId}/comments/{commentId}")
  public ResponseEntity<StatusResponse> deleteComment(
      @AuthenticationPrincipal final UserDetailsImpl userDetails,
      @PathVariable final Long articleId,
      @PathVariable final Long commentId
  ) {
    return ResponseEntity.ok(articleCommentService.deleteComment(userDetails.getUser(), articleId, commentId));
  }
}
