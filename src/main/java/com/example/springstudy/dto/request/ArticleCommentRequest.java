package com.example.springstudy.dto.request;

import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
public class ArticleCommentRequest {

  @NotNull
  @Min(0)
  private Long articleId;
  @NotNull
  private String content;

}
