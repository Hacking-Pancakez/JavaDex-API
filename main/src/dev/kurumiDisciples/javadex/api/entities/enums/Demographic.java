package dev.kurumiDisciples.javadex.api.entities.enums;


public enum Demographic {
  
  SHOUNEN("shounen"),
  SHOUJO("shoujo"),
  JOSEI("josei"),
  SEINEN("seinen"),
  NONE("none");

  private String value;

  Demographic(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return value;
  }
  
  public static Demographic getDemographic(String value) {
    for (Demographic demographic : Demographic.values()) {
      if (demographic.getValue().equals(value)) {
        return demographic;
      }
    }
    return Demographic.NONE;
}
}