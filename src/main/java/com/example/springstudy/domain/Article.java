package com.example.springstudy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Article extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ARTICLE_ID")
  private Long id;

  @Setter
  @Column(nullable = false)
  private String title;
  @Setter
  @Column(nullable = false)
  private String content;

  @JoinColumn(name = "USER_ID")
  @ManyToOne(fetch = FetchType.LAZY)
  private UserAccount userAccount;

  @OrderBy("createdAt DESC")
  @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
  private final List<ArticleComment> articleComments = new ArrayList<>();

  @Setter
  @ColumnDefault("0")
  private int heartCount;

  private Article(String title, String content, UserAccount userAccount) {
    this.title = title;
    this.content = content;
    this.userAccount = userAccount;
  }
  public static Article of(String title, String content, UserAccount userAccount) {
    return new Article(title, content, userAccount);
  }
}
