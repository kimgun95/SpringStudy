package com.example.springstudy.dto;

import com.example.springstudy.domain.Article;
import com.example.springstudy.domain.UserAccount;
import com.example.springstudy.dto.request.ArticleRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ArticleDto {

  private String title;
  private String content;
  @Setter
  private UserAccount userAccount;

  private ArticleDto(String title, String content) {
    this.title = title;
    this.content = content;
  }

    public static ArticleDto from (ArticleRequest entity) {
    return new ArticleDto(
        entity.getTitle(),
        entity.getContent()
    );
  }

  public static Article toEntity (ArticleDto articleDto) {
    return Article.of(
        articleDto.getTitle(),
        articleDto.getContent(),
        articleDto.getUserAccount()
    );
  }

}
