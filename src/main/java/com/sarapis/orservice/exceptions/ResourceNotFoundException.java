package com.sarapis.orservice.exceptions;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(String resource, String id) {
    super(String.format("%s with id %s not found", resource, id));
  }
}
