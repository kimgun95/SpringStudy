package com.example.springstudy.dto.request;

import lombok.Getter;

@Getter
public class ArticleCommentRequest {

  private Long articleId;
  private String content;

}
