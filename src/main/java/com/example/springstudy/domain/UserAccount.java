package com.example.springstudy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class UserAccount extends Timestamped{

  @Id
  private Long userId;

  private String password;


}
