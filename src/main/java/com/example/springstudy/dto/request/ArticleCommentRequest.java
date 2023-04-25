package com.example.springstudy.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ArticleCommentRequest {

  @NotNull
  private String content;

}
