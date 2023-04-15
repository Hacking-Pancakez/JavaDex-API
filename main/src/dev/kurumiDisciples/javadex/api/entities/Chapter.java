package dev.kurumiDisciples.javadex.api.entities;

import dev.kurumiDisciples.javadex.api.entities.ISnowflake;
import dev.kurumiDisciples.javadex.api.entities.IPublishable;

import dev.kurumiDisciples.javadex.api.entities.enums.*;

import javax.json.*;
import javax.json.stream.*;

import java.time.OffsetDateTime;
import java.util.UUID;

import dev.kurumiDisciples.javadex.api.manga.Manga;
import dev.kurumiDisciples.javadex.api.entities.Relationships;

import dev.kurumiDisciples.javadex.api.exceptions.ErrorException;

import dev.kurumiDisciples.javadex.api.requests.HashAction;

public class Chapter implements ISnowflake, IPublishable{

  private final UUID id;
  private final long volume;
  private final double chapter;
  private final String title; 
  private final int version;
  private final String translatedLanguage;
  private final int pages;
  private final Relationships relationships;
  private final OffsetDateTime createdAt;
  private final OffsetDateTime updatedAt;
  private final OffsetDateTime publishedAt;
  private final OffsetDateTime readableAt;
  private String hash;

  public Chapter(JsonObject data){
    if(!isChapterData(data)) throw new IllegalArgumentException("Invalid chapter data");

    JsonObject attributes = data.getJsonObject("attributes");
    
    this.id = UUID.fromString(data.getString("id"));
    this.volume = Long.parseLong(attributes.getString("volume", "0"));
    this.chapter = Double.parseDouble(attributes.getString("chapter"));
    this.title = attributes.getString("title", "no_title");
    this.version = attributes.getJsonNumber("version").intValue();
    this.translatedLanguage = attributes.getString("translatedLanguage");
    this.pages = attributes.getJsonNumber("pages").intValue();
    this.relationships = new Relationships(data.getJsonArray("relationships"));
    this.createdAt = OffsetDateTime.parse(attributes.getString("createdAt","2001-09-09T01:46:40Z"));
    this.updatedAt = OffsetDateTime.parse(attributes.getString("updatedAt", "2001-09-09T01:46:40Z"));
    this.publishedAt = OffsetDateTime.parse(attributes.getString("publishedAt", "2001-09-09T01:46:40Z"));
    this.readableAt = OffsetDateTime.parse(attributes.getString("readableAt", "2001-09-09T01:46:40Z"));
    this.hash = null;
  }

  public Chapter(JsonObject data, boolean retrieveHash){
    if(!isChapterData(data)) throw new IllegalArgumentException("Invalid chapter data");

    JsonObject attributes = data.getJsonObject("attributes");
    
    this.id = UUID.fromString(data.getString("id"));
    this.volume = Long.parseLong(attributes.getString("volume", "0"));
    this.chapter = Double.parseDouble(attributes.getString("chapter"));
    this.title = attributes.getString("title", "no_title");
    this.version = attributes.getJsonNumber("version").intValue();
    this.translatedLanguage = attributes.getString("translatedLanguage");
    this.pages = attributes.getJsonNumber("pages").intValue();
    this.relationships = new Relationships(data.getJsonArray("relationships"));
    this.createdAt = OffsetDateTime.parse(attributes.getString("createdAt","2001-09-09T01:46:40Z"));
    this.updatedAt = OffsetDateTime.parse(attributes.getString("updatedAt", "2001-09-09T01:46:40Z"));
    this.publishedAt = OffsetDateTime.parse(attributes.getString("publishedAt", "2001-09-09T01:46:40Z"));
    this.readableAt = OffsetDateTime.parse(attributes.getString("readableAt", "2001-09-09T01:46:40Z"));
    this.hash = null;

    if(retrieveHash){
      
    }
  }

  /* will be avaliable with relationships support */
  private Manga retrieveOriginManga(){
    return null;
  }

  public long getVolume() {
    return volume;
  }

  @Override
  public String getId(){
    return id.toString();
  }

  public UUID getUUID(){
    return id;
  }
  
  @Override
  public OffsetDateTime getCreatedAt(){
    return createdAt;
  }
  
  @Override
  public OffsetDateTime getUpdatedAt(){
    return updatedAt;
  }

  @Override
  public OffsetDateTime getPublishAt(){
    return publishedAt;
  }

  @Override
  public OffsetDateTime getReadableAt(){
    return readableAt;
  }

  public double getChapter() {
    return chapter;
  }

  public String getTitle() {
    return title;
  }

  public int getVersion() {
    return version;
  }

  public String getTranslatedLanguage() {
    return translatedLanguage;
  }

  public int getPages() {
    return pages;
  }

  public Relationships getRelationships() {
    return relationships;
  }



  public void retrieveHash(){
    if (hash == null) {
      try{
      hash = HashAction.getHash(this);
      }
      catch (Exception e){
        System.out.println("Failure to retrieve hash");
        e.printStackTrace();
      }
    }
  }


  public String getHash(){
    return hash;
  }

  public void retrieveHashAsync(){
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        retrieveHash();
      }
    });
  }
  
  private static boolean isChapterData(JsonObject data){
    return data.getString("type").equals("chapter");
  }

  private static boolean isError(JsonObject data){
    return data.getString("result").equals("error");
  }
}