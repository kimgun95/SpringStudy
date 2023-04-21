package com.example.springstudy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ArticleComment extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "ARTICLE_ID")
  private Article article;

  @ManyToOne
  @JoinColumn(name = "USER_ID")
  private UserAccount userAccount;

  @Setter
  @Column(nullable = false, length = 500)
  private String content;

  private ArticleComment(Article article, UserAccount userAccount, String content) {
    this.article = article;
    this.userAccount = userAccount;
    this.content = content;
  }

  public static ArticleComment of(Article article, UserAccount userAccount, String content) {
    return new ArticleComment(article, userAccount, content);
  }

}
