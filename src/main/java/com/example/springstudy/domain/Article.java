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
  private String username;
  @Column(nullable = false)
  private String password;

  private Article(String title, String content, String username, String password) {
    this.title = title;
    this.content = content;
    this.username = username;
    this.password = password;
  }
  public static Article of(String title, String content, String username, String password) {
    return new Article(title, content, username, password);
  }
}
