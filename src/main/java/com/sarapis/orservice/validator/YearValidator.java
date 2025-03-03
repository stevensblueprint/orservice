package com.sarapis.orservice.validator;

import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class YearValidator implements ConstraintValidator<ValidYear, Integer> {

  private int min;

  @Override
  public void initialize(ValidYear constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    this.min = constraintAnnotation.min();
  }

  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    int currentYear = LocalDate.now().getYear();
    return value >= min && value <= currentYear;
  }
}
