package com.example.springstudy.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ArticleException extends RuntimeException {

  private final ArticleErrorResult errorResult;
}
