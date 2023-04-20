package com.example.springstudy.dto.response;

import com.example.springstudy.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ArticleResponse {

  private Long id;
  private String title;
  private String content;
  private String username;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static ArticleResponse from (Article entity) {
    return new ArticleResponse(
        entity.getId(),
        entity.getTitle(),
        entity.getContent(),
        entity.getUserAccount().getUsername(),
        entity.getCreatedAt(),
        entity.getModifiedAt()
    );
  }
}
