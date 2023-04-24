package com.example.springstudy.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ArticleRequest {

  @NotNull
  private String title;
  @NotNull
  private String content;

}
