package dev.kurumiDisciples.javadex.api.entities;

import dev.kurumiDisciples.javadex.api.entities.ISnowflake;
import dev.kurumiDisciples.javadex.api.entities.IPublishable;

import dev.kurumiDisciples.javadex.api.entities.enums.*;

import javax.json.*;
import javax.json.stream.*;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.List;
import java.util.concurrent.*;

import java.io.IOException;

import dev.kurumiDisciples.javadex.api.manga.Manga;
import dev.kurumiDisciples.javadex.api.entities.Relationships;

import dev.kurumiDisciples.javadex.api.exceptions.ErrorException;

import dev.kurumiDisciples.javadex.api.requests.*;
import dev.kurumiDisciples.javadex.api.entities.PageProxy;

import dev.kurumiDisciples.javadex.api.entities.relationship.RelationshipMap;

public class Chapter implements ISnowflake, IPublishable{

  private final UUID id;
  private final long volume;
  private final double chapter;
  private final String title; 
  private final int version;
  private final TranslatedLanguage translatedLanguage;
  private final int pages;
  private final Relationships relationships;
  private final OffsetDateTime createdAt;
  private final OffsetDateTime updatedAt;
  private final OffsetDateTime publishedAt;
  private final OffsetDateTime readableAt;
  private final RelationshipMap relationshipMap;
  private String hash;

  public Chapter(JsonObject data){
    if(!isChapterData(data)) throw new IllegalArgumentException("Invalid chapter data");

    JsonObject attributes = data.getJsonObject("attributes");
    
    this.id = UUID.fromString(data.getString("id"));
    this.volume = Long.parseLong(attributes.getString("volume", "0"));
    this.chapter = Double.parseDouble(attributes.getString("chapter", "0"));
    this.title = attributes.getString("title", "no_title");
    this.version = attributes.getJsonNumber("version").intValue();
    //change to the enum
    this.translatedLanguage = TranslatedLanguage.getByLanguage(attributes.getString("translatedLanguage"));
    this.pages = attributes.getJsonNumber("pages").intValue();
    this.relationships = new Relationships(data.getJsonArray("relationships"));
    this.createdAt = OffsetDateTime.parse(attributes.getString("createdAt","2001-09-09T01:46:40Z"));
    this.updatedAt = OffsetDateTime.parse(attributes.getString("updatedAt", "2001-09-09T01:46:40Z"));
    this.publishedAt = OffsetDateTime.parse(attributes.getString("publishedAt", "2001-09-09T01:46:40Z"));
    this.readableAt = OffsetDateTime.parse(attributes.getString("readableAt", "2001-09-09T01:46:40Z"));
    this.relationshipMap = new RelationshipMap(data.getJsonArray("relationships"));
    this.hash = null;
  }

  /* will be avaliable with relationships support */
  public CompletableFuture<Manga> getAssociatedManga(){
    return SearchAction.getMangaById(getRelationshipMap().get(RelationshipType.MANGA).get(0).toString());
  }

  public RelationshipMap getRelationshipMap() {
    return relationshipMap;
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

  public TranslatedLanguage getTranslatedLanguage() {
    return translatedLanguage;
  }

  public int getPages() {
    return pages;
  }

  public Relationships getRelationships() {
    return relationships;
  }

  
  private static boolean isChapterData(JsonObject data){
    return data.getString("type").equals("chapter");
  }

  private static boolean isError(JsonObject data){
    return data.getString("result").equals("error");
  }

  public List<PageProxy> retrievePages() {
    return PageBuilder.getPages(this);
  }
}