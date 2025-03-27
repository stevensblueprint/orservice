package com.sarapis.orservice.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = LanguageCodeValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLanguageCode {
  String message() default "Invalid ISO 639-1, ISO 639-2, or ISO 639-3 language code";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
