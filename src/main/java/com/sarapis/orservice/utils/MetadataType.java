package com.sarapis.orservice.utils;

public enum MetadataType {
  CREATE("CREATE"),
  UPDATE("UPDATE"),
  DELETE("DELETE");

  private final String value;

  MetadataType(String value) {
    this.value = value;
  }
  @Override
  public String toString() {
    return value;
  }
}
