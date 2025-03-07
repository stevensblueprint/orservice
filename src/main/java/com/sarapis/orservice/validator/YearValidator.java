package com.sarapis.orservice.validator;

import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class YearValidator implements ConstraintValidator<ValidYear, Integer> {

  /*
   * The minimum year allowed for validation.
   */
  private int min;

  @Override
  public void initialize(final ValidYear constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    this.min = constraintAnnotation.min();
  }

  @Override
  public boolean isValid(
      final Integer value, final ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    int currentYear = LocalDate.now().getYear();
    return value >= min && value <= currentYear;
  }
}
