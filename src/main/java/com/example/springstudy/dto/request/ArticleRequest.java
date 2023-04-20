package com.example.springstudy.dto.request;

import com.example.springstudy.domain.Article;
import lombok.Getter;

@Getter
public class ArticleRequest {

  private String title;
  private String content;

//  public static Article toEntity(ArticleRequest articleRequest) {
//    return Article.of(
//        articleRequest.getTitle(),
//        articleRequest.getContent()
//    );
//  }
}
