package com.sarapis.orservice.utils;

public enum Metadata {
  CREATE("create"),
  UPDATE("update"),
  DELETE("delete");

  private final String value;

  Metadata(String value) {
    this.value = value;
  }
  @Override
  public String toString() {
    return value;
  }
}
