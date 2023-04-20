package com.example.springstudy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class UserAccount extends Timestamped{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "USER_ID")
  private Long id;

  private String username;
  private String password;

  private UserAccount(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public static UserAccount of(String username, String password) {
    return new UserAccount(username, password);
  }

}
