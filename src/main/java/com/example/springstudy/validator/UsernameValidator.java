package com.example.springstudy.validator;

import com.example.springstudy.annotation.Username;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<Username, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[0-9]).{4,10}$");
    Matcher matcher = pattern.matcher(value);
    return matcher.matches();
  }
}
