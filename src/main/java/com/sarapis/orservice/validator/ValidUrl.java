package com.sarapis.orservice.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = UrlValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUrl {
  String message() default "Invalid URL format";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  String[] protocols() default {"http", "https"};
  boolean requireHost() default true;
}
