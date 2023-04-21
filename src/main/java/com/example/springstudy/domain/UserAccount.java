package com.example.springstudy.domain;

import com.example.springstudy.domain.constant.UserAccountRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  @Setter
  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserAccountRole role;

  private UserAccount(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public static UserAccount of(String username, String password) {
    return new UserAccount(username, password);
  }

}
