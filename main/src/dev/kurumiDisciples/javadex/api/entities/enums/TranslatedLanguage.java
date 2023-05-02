package dev.kurumiDisciples.javadex.api.entities.enums;

public enum TranslatedLanguage {
  ENGLISH("en"),
  SPANISH("es"), 
  GERMAN("de"),
  GREEK("el"),
  FRENCH("fr"),
  JAPANESE("ja"),
  HINDI("hi");

  private final String language;
  
  TranslatedLanguage(String language) {
    this.language = language;
  }
  
  public String getLanguage() {
    return language;
  }

  public static TranslatedLanguage getByLanguage(String language) {
    for (TranslatedLanguage translatedLanguage : TranslatedLanguage.values()) {
      if (translatedLanguage.getLanguage().equals(language)) {
        return translatedLanguage;
      }
    }
    return null;
  }
  

}