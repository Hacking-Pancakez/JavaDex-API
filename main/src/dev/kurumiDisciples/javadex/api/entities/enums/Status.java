package dev.kurumiDisciples.javadex.api.entities.enums;

public enum Status {
  ONGOING("ongoing"),
  COMPLETED("completed"),
  HIATUS("hiatus"),
  CANCELLED("cancelled");

  private String value;
  
  private Status(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static Status getStatus(String value) {
    for (Status status : Status.values()) {
      if (status.getValue().equals(value)) {
        return status;
      }
    }
    return null;

}
}