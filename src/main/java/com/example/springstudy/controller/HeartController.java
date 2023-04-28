package com.example.springstudy.controller;

import com.example.springstudy.dto.response.StatusResponse;
import com.example.springstudy.security.UserDetailsImpl;
import com.example.springstudy.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class HeartController {

  private final HeartService heartService;

  @PutMapping("/heart/{articleId}")
  public ResponseEntity<StatusResponse> heartArticle(
      @AuthenticationPrincipal final UserDetailsImpl userDetails,
      @PathVariable final Long articleId
  ) {
    return ResponseEntity.ok(heartService.heartArticle(userDetails.getUser(), articleId));
  }

  @PutMapping("/heart/{articleId}/{commentId}")
  public ResponseEntity<StatusResponse> heartArticleComment(
      @AuthenticationPrincipal final UserDetailsImpl userDetails,
      @PathVariable final Long articleId,
      @PathVariable final Long commentId
  ) {
    return ResponseEntity.ok(heartService.heartArticleComment(userDetails.getUser(), articleId, commentId));
  }

}
