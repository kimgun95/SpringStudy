package com.example.springstudy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Article extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Column(nullable = false)
  private String title;
  @Setter
  @Column(nullable = false)
  private String content;
  @Setter
  @Column(nullable = false)
  private String author;
  @Column(nullable = false)
  private String password;

  private Article(String title, String content, String author, String password) {
    this.title = title;
    this.content = content;
    this.author = author;
    this.password = password;
  }
  public static Article of(String title, String content, String author, String password) {
    return new Article(title, content, author, password);
  }
}
