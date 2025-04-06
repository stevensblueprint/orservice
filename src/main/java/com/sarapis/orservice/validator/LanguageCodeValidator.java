package com.sarapis.orservice.validator;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LanguageCodeValidator implements ConstraintValidator<ValidLanguageCode, String> {

  // Regex patterns for ISO 639-1 (2-letter) and ISO 639-2/3 (3-letter)
  private static final Pattern ISO_639_1_PATTERN = Pattern.compile("^[a-z]{2}$");
  private static final Pattern ISO_639_2_3_PATTERN = Pattern.compile("^[a-z]{3}$");

  // Set of valid language codes (a subset of common ones for efficiency)
  private static final Set<String> VALID_ISO_639_CODES = new HashSet<>();

  static {
    // Common ISO 639-1 language codes
    VALID_ISO_639_CODES.add("en");
    VALID_ISO_639_CODES.add("es");
    VALID_ISO_639_CODES.add("fr");
    VALID_ISO_639_CODES.add("de");
    VALID_ISO_639_CODES.add("zh");
    VALID_ISO_639_CODES.add("ar");
    VALID_ISO_639_CODES.add("ru");
    VALID_ISO_639_CODES.add("pt");
    VALID_ISO_639_CODES.add("hi");
    VALID_ISO_639_CODES.add("ja");

    // Common ISO 639-2 / 639-3 language codes
    VALID_ISO_639_CODES.add("eng");
    VALID_ISO_639_CODES.add("spa");
    VALID_ISO_639_CODES.add("fra");
    VALID_ISO_639_CODES.add("deu");
    VALID_ISO_639_CODES.add("zho");
    VALID_ISO_639_CODES.add("ara");
    VALID_ISO_639_CODES.add("rus");
    VALID_ISO_639_CODES.add("por");
    VALID_ISO_639_CODES.add("hin");
    VALID_ISO_639_CODES.add("jpn");
  }

  @Override
  public void initialize(ValidLanguageCode constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String languageCode, ConstraintValidatorContext context) {
    if (languageCode == null || languageCode.isEmpty()) {
      return true; // Allow empty values; use @NotNull separately if required
    }

    String lowerCaseCode = languageCode.toLowerCase();

    // Validate pattern and existence in the predefined set
    return (ISO_639_1_PATTERN.matcher(lowerCaseCode).matches() || ISO_639_2_3_PATTERN.matcher(lowerCaseCode).matches())
        && VALID_ISO_639_CODES.contains(lowerCaseCode);
  }
}
