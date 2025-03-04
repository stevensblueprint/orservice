package com.sarapis.orservice.validator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UrlValidator implements ConstraintValidator<ValidUrl, String> {

  private static final String URL_REGEX =
      "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

  private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

  private String[] protocols;
  private boolean requireHost;

  @Override
  public void initialize(ValidUrl constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    this.requireHost = constraintAnnotation.requireHost();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.isEmpty()) {
      return true;
    }
    if (!URL_PATTERN.matcher(value).matches()) {
      return false;
    }

    try {
      URL url = new URL(value);
      boolean validProtocol = false;
      for (String protocol : protocols) {
        if (url.getProtocol().equals(protocol)) {
          validProtocol = true;
          break;
        }
      }

      if (!validProtocol) {
        return false;
      }
      // Check host requirement
      return !requireHost || (url.getHost() != null && !url.getHost().isEmpty());

    } catch (MalformedURLException e) {
      return false;
    }
  }
}
