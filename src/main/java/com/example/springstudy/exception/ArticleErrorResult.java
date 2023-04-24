package com.example.springstudy.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ArticleErrorResult {

  DUPLICATED_USER_REGISTER(HttpStatus.BAD_REQUEST, "중복된 username 입니다."),
  UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Exception"),
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),
  ARTICLE_NOT_FOUND(HttpStatus.BAD_REQUEST, "게시글을 찾을 수 없습니다."),
  COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "댓글을 찾을 수 없습니다."),
  NOT_ARTICLE_OWNER(HttpStatus.BAD_REQUEST, "작성자만 수정/삭제가 가능합니다."),
  INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다.")
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
