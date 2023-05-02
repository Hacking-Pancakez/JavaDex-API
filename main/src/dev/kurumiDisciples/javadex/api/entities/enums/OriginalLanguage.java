package dev.kurumiDisciples.javadex.api.entities.enums;

public enum OriginalLanguage {

  /* fill in the rest of the enum */

  JAPANESE("ja"),
  CHINESE("zh");


  private String value;

  private OriginalLanguage(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static OriginalLanguage getLanguage(String value) {
    for (OriginalLanguage language : OriginalLanguage.values()) {
      if (language.getValue().equals(value)) {
        return language;
      }
    }
    return null;
  }

  
}