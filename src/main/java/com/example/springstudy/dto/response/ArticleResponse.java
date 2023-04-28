package com.example.springstudy.dto.response;

import com.example.springstudy.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ArticleResponse {

  private Long id;
  private String title;
  private String content;
  private String username;
  private List<ArticleCommentResponse> articleComments = new ArrayList<>();
  private int heartCount;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static ArticleResponse from (Article entity) {
    return new ArticleResponse(
        entity.getId(),
        entity.getTitle(),
        entity.getContent(),
        entity.getUserAccount().getUsername(),
        entity.getArticleComments()
            .stream().map(ArticleCommentResponse::from)
            .toList(),
        entity.getHeartCount(),
        entity.getCreatedAt(),
        entity.getModifiedAt()
    );
  }
}
