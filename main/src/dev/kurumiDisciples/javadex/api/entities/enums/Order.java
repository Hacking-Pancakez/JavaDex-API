package dev.kurumiDisciples.javadex.api.entities.enums;


public enum Order {
  
  ASCENDING("asc"),
  DESCENDING("desc");

  private String value;

  Order(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
  
}