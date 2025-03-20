package com.sarapis.orservice.utils;

public enum MetadataType {
  CREATE("create"),
  UPDATE("update"),
  DELETE("delete");

  private final String value;

  MetadataType(String value) {
    this.value = value;
  }
  @Override
  public String toString() {
    return value;
  }
}
