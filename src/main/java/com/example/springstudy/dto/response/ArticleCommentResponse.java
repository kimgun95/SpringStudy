package com.example.springstudy.dto.response;

import com.example.springstudy.domain.ArticleComment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ArticleCommentResponse {

  private Long id;
  private String content;
  private String username;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static ArticleCommentResponse from (ArticleComment entity) {
    return new ArticleCommentResponse(
        entity.getId(),
        entity.getContent(),
        entity.getUserAccount().getUsername(),
        entity.getCreatedAt(),
        entity.getModifiedAt()
    );
  }

}
