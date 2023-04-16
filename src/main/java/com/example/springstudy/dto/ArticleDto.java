package com.example.springstudy.dto;


import com.example.springstudy.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ArticleDto {

  private Long id;

  private String title;
  private String content;
  private String author;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  public static ArticleDto from(Article entity) {
    return new ArticleDto(
        entity.getId(),
        entity.getTitle(),
        entity.getContent(),
        entity.getAuthor(),
        entity.getCreatedAt(),
        entity.getModifiedAt()
    );
  }
}
