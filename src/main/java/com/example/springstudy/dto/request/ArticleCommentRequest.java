package com.example.springstudy.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ArticleCommentRequest {

  private Long articleId;
  @NotNull
  private String content;

}
