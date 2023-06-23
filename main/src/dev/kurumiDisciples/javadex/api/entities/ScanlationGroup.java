package dev.kurumiDisciples.javadex.api.entities;


import dev.kurumiDisciples.javadex.api.entities.ISnowflake;
import dev.kurumiDisciples.javadex.api.entities.relationship.RelationshipMap;
import dev.kurumiDisciples.javadex.api.exceptions.ErrorException;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.time.OffsetDateTime;

import javax.json.*;
import javax.json.stream.*;


public class ScanlationGroup implements ISnowflake {

  private UUID id;
  private String name;
  private String website;
  private String ircServer;
  private String ircChannel;
  private String discord;
  private String contactEmail;
  private String description;
  private String twitter;
  private String mangaUpdates;
  private List<String> focusedLanguage = new ArrayList<>();
  private boolean locked;
  private boolean official;
  private boolean inactive;
  private Integer version;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  private RelationshipMap relationshipMap;



  public ScanlationGroup(JsonObject jsonObject) {
    //if (isScanlationGroup(jsonObject)) throw new IllegalArgumentException("Object is not valid");

    this.id = UUID.fromString(jsonObject.getString("id"));
    JsonObject attributes = jsonObject.getJsonObject("attributes");
    this.name = attributes.getString("name");
    this.website = attributes.getString("website", null);
    this.ircServer = attributes.getString("ircServer", null);
    this.ircChannel = attributes.getString("ircChannel", null);
    this.discord = attributes.getString("discord", null);
    this.contactEmail = attributes.getString("contactEmail", null);
    this.description = attributes.getString("description", null);
    this.twitter = attributes.getString("twitter", null);
    this.mangaUpdates = attributes.getString("mangaUpdates", null);
    this.focusedLanguage = null; //will do later
    this.locked = attributes.getBoolean("locked");
    this.official = attributes.getBoolean("official");
    this.inactive = attributes.getBoolean("inactive");
    this.version = attributes.getInt("version");
    this.createdAt = OffsetDateTime.parse(attributes.getString("createdAt"));
    this.updatedAt = OffsetDateTime.parse(attributes.getString("updatedAt"));
    this.relationshipMap = new RelationshipMap(jsonObject.getJsonArray("relationships"));
  }

  public RelationshipMap getRelationshipMap(){
    return relationshipMap;
  }
  
  @Override
  public String getId(){
    return id.toString();
  }

  @Override
  public OffsetDateTime getCreatedAt(){
    return createdAt;
  }

  @Override
  public OffsetDateTime getUpdatedAt(){
    return updatedAt;
  }
  
  public UUID getUUID() {
    return id;
  }
  
  public String getWebsite(){
    return website;
  }

  public String getIrcServer(){
    return ircServer;
  }

  public String getIrcChannel(){
    return ircChannel;
  }

  public String getDiscord(){
    return discord;
  }

  public String getName(){
    return name;
  }

  public String getContactEmail(){
    return contactEmail;
  }

  public String getDescription(){
    return description;
  }

  public String getTwitter(){
    return twitter;
  }

  public String getMangaUpdates(){
    return mangaUpdates;
  }

  public List<String> getFocusedLanguage(){
    return focusedLanguage;
  }

  public boolean isLocked(){
    return locked;
  }

  public boolean isOfficial(){
    return official;
  }

  public boolean isInactive(){
    return inactive;
  }

  public Integer getVersion(){
    return version;
  }


  private boolean isError(JsonObject jsonObject) {
    return jsonObject.getString("result").equals("error");
  }
  
  private boolean isScanlationGroup(JsonObject jsonObject) {
    return jsonObject.getString("type").equals("scanlation_group");
  }
}