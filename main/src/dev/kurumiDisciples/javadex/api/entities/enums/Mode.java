package dev.kurumiDisciples.javadex.api.entities.enums;



public enum Mode {

  AND("AND"),
  OR("OR");

  private String value;

  Mode(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static Mode fromValue(String value) {
    if (value == null) {
      return null;
    }
    for (Mode e : Mode.values()) {
      if (e.getValue().equals(value)) {
        return e;
      }
    }
    return null;
  }
  
}