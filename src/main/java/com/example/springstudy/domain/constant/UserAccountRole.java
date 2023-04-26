package com.example.springstudy.domain.constant;

public enum UserAccountRole {
  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN")
  ;

  private final String authority;

  UserAccountRole(String authority) {
    this.authority = authority;
  }

  public String getAuthority() {
    return this.authority;
  }

}
