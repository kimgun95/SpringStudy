package com.example.springstudy.dto;

import com.example.springstudy.domain.Article;
import com.example.springstudy.dto.request.ArticleRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleDto {

  private String title;
  private String content;
  private String username;
  private String password;

  public static ArticleDto from (ArticleRequest entity) {
    return new ArticleDto(
        entity.getTitle(),
        entity.getContent(),
        entity.getUsername(),
        entity.getPassword()
    );
  }

  public static Article toEntity (ArticleDto articleDto) {
    return Article.of(
        articleDto.getTitle(),
        articleDto.getContent(),
        articleDto.getUsername(),
        articleDto.getPassword()
    );
  }

}
