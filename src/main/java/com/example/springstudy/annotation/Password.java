package com.example.springstudy.annotation;

import com.example.springstudy.validator.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface Password {

  String message() default "Invalid Password";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };
}
