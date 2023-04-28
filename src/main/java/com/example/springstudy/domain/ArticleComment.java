package com.example.springstudy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ArticleComment extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ARTICLE_COMMENT_ID")
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

  @Setter
  @ColumnDefault("0")
  private int heartCount;

  private ArticleComment(Article article, UserAccount userAccount, String content) {
    this.article = article;
    this.userAccount = userAccount;
    this.content = content;
  }

  public static ArticleComment of(Article article, UserAccount userAccount, String content) {
    return new ArticleComment(article, userAccount, content);
  }

}
