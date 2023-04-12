package dev.kurumiDisciples.javadex.api.entities;

import javax.json.JsonArray;
import javax.json.JsonObject;

import java.util.HashMap;

public class Relationships {

  private final HashMap<Type, String> relationships = new HashMap<>();

  public Relationships(JsonArray data) {
    for (int i = 0; i < data.size(); i++) {
      JsonObject relationship = data.getJsonObject(i);
      Type type = Type.fromString(relationship.getString("type"));
      relationships.put(type, relationship.getString("id"));
    }
  }

  
  public String getRelationship(Type type) {
    return relationships.get(type);
  }

  public HashMap getRawMap(){
    return relationships;
  }

}

enum Type{
  GROUP("scanlation_group"),
  MANGA("manga"),
  USER("user");

  private final String type;

  Type(String type) {
    this.type = type;
  }
  
  public String getType() {
    return type;
  }

  public static Type fromString(String type) {
    for (Type t : values()) {
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