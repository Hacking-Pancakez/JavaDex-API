package dev.kurumiDisciples.javadex.api.entities.enums;

public enum RelationshipType{
  ARTIST("artist"),
  AUTHOR("author"),
  GROUP("scanlation_group"),
  MANGA("manga"),
  LEADER("leader"),
  MEMBER("member"),
  USER("user");

  private final String type;

  RelationshipType(String type) {
    this.type = type;
  }
  
  public String getType() {
    return type;
  }

  public static RelationshipType fromString(String type) {
    for (RelationshipType t : values()) {
      if (t.getType().equals(type)) {
        return t;
      }
    }
    return null;
}

  @Override
  public String toString() {
    return type;
  }
}