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

@RequiredArgsConstructor
@RestController
public class ArticleCommentController {

  private final ArticleCommentService articleCommentService;

  @PostMapping("/comments")
  public ResponseEntity<ArticleCommentResponse> saveComment(
      @RequestBody ArticleCommentRequest articleCommentRequest,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(articleCommentService.saveComment(ArticleCommentDto.from(articleCommentRequest), request));
  }

  @PutMapping("/comments/{commentId}")
  public ResponseEntity<ArticleCommentResponse> updateComment(
      @PathVariable Long commentId,
      @RequestBody ArticleCommentRequest articleCommentRequest,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(articleCommentService.updateComment(commentId, ArticleCommentDto.from(articleCommentRequest), request));
  }

  @DeleteMapping ("/comments/{commentId}")
  public ResponseEntity<StatusResponse> deleteComment(
      @PathVariable Long commentId,
      HttpServletRequest request
  ) {
    return ResponseEntity.ok(articleCommentService.deleteComment(commentId, request));
  }
}
