package com.example.springstudy.annotation;

import com.example.springstudy.validator.UsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
@Documented
public @interface Username {

  String message() default "Invalid Username";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };
}
