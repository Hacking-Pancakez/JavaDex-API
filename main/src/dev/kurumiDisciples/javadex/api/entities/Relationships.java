package dev.kurumiDisciples.javadex.api.entities;

import javax.json.JsonArray;
import javax.json.JsonObject;

import dev.kurumiDisciples.javadex.api.entities.enums.RelationshipType;

import java.util.HashMap;

import java.util.UUID;

public class Relationships {

  private final HashMap<RelationshipType, UUID> relationships = new HashMap<>();

  public Relationships(JsonArray data) {
    for (int i = 0; i < data.size(); i++) {
      JsonObject relationship = data.getJsonObject(i);
      RelationshipType type = RelationshipType.fromString(relationship.getString("type"));
      relationships.put(type, UUID.fromString(relationship.getString("id")));
    }
  }

  
  public UUID getRelationship(RelationshipType type) {
    return relationships.get(type);
  }

  public HashMap getRawMap(){
    return relationships;
  }

}

