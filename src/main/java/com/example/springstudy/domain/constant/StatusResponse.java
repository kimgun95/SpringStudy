package com.example.springstudy.domain.constant;

import lombok.Getter;

public enum StatusResponse {
  SUCCESS(true), FAIL(false);
  @Getter private final Boolean success;

  StatusResponse(Boolean success) {
    this.success = success;
  }
}
