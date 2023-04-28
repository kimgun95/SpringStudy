package com.example.springstudy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Heart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "USER_ID")
  @ManyToOne(fetch = FetchType.LAZY)
  private UserAccount user;

  @JoinColumn(name = "ARTICLE_ID")
  @ManyToOne(fetch = FetchType.LAZY)
  private Article article;

  @JoinColumn(name = "ARTICLE_COMMENT_ID")
  @ManyToOne(fetch = FetchType.LAZY)
  private ArticleComment comment;

  private Heart(UserAccount userAccount, Article article, ArticleComment articleComment) {
    this.user = userAccount;
    this.article = article;
    this.comment = articleComment;
  }

  public static Heart of(UserAccount userAccount, Article article) {
    return new Heart(userAccount, article, null);
  }

  public static Heart of(UserAccount userAccount, Article article, ArticleComment articleComment) {
    return new Heart(userAccount, article, articleComment);
  }

}
