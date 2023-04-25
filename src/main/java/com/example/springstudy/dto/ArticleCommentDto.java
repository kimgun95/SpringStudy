package com.example.springstudy.dto;

import com.example.springstudy.domain.Article;
import com.example.springstudy.domain.ArticleComment;
import com.example.springstudy.domain.UserAccount;
import com.example.springstudy.dto.request.ArticleCommentRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ArticleCommentDto {

  private String content;
  @Setter
  private Article article;
  @Setter
  private UserAccount userAccount;

  private ArticleCommentDto(String content) {
    this.content = content;
  }

  public static ArticleCommentDto from (ArticleCommentRequest entity) {
    return new ArticleCommentDto(
        entity.getContent()
    );
  }

  public static ArticleComment toEntity (ArticleCommentDto articleCommentDto) {
    return ArticleComment.of(
        articleCommentDto.getArticle(),
        articleCommentDto.getUserAccount(),
        articleCommentDto.getContent()
    );
  }

}

