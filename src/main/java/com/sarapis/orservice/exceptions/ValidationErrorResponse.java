package com.sarapis.orservice.exceptions;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationErrorResponse extends ErrorResponse {
  private Map<String, String> errors;

  public ValidationErrorResponse(int status, String message, LocalDateTime timestamp, Map<String, String> errors) {
    super(status, message, timestamp);
    this.errors = errors;
  }
}