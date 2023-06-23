package dev.kurumiDisciples.javadex.api.entities;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import javax.json.*;
import javax.json.stream.*;

import dev.kurumiDisciples.javadex.api.entities.enums.Role;

import dev.kurumiDisciples.javadex.api.entities.relationship.RelationshipMap;

public class User {


 private final List<Role> roles;
 private UUID id;
 private final String username;
 private final RelationshipMap relationshipMap;

  public User(JsonObject user) {
  JsonObject original = user;
  try{
  user = user.getJsonObject("data");
  this.id = UUID.fromString(user.getString("id"));
  } catch (Exception e){  
    user = original;
    this.id = UUID.fromString(user.getString("id"));
  }
    
  JsonObject attributes = user.getJsonObject("attributes");
  
  this.username = attributes.getString("username");
  this.roles = getRoles(attributes.getJsonArray("roles"));
  this.relationshipMap = new RelationshipMap(user.getJsonArray("relationships"));
  }

  private static List<Role> getRoles(JsonArray jsonArray) {
    List<Role> roles = new ArrayList<>();
    for (int i = 0; i < jsonArray.size(); i++) {
      roles.add(Role.getRole(jsonArray.getJsonString(i).getString()));
    }
    return roles;
  }

  public List<Role> getRoles(){
    return roles;
  }

  public UUID getId(){
    return id;
  }

  public String getUsername(){
    return username;
  }

  public RelationshipMap getRelationshipMap(){
    return relationshipMap;
  }
}