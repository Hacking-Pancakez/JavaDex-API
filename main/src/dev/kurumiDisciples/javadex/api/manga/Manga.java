package dev.kurumiDisciples.javadex.api.manga;

import dev.kurumiDisciples.javadex.api.manga.MangaTag;

import dev.kurumiDisciples.javadex.api.entities.ISnowflake;

import java.util.List;

import java.time.OffsetDateTime;

import javax.json.*;
import javax.json.stream.*;

import java.util.ArrayList;

public class Manga implements ISnowflake{


  private String id;

  private List<MangaTag> tags;

  private String title;

  private String author;
  
  private String description;
  
  private String[] altTitles;

  private boolean isLocked;

  /* change to a independent class for original langauge */
  private String originalLanguage;

  private String lastVolume;
  
  private String lastChapter;

  /* change to a independent class for demographic */
  private String publicationDemographic;

  /* change to a class for status */
  private String status;

  private long year;

  private String imageUrl;
  
  /* change to a class for content rating */
  private String contentRating;

  private String state;
  
  private boolean chapterNumbersResetOnNewVolume;

  private OffsetDateTime createdAt;

  private OffsetDateTime updatedAt;

  private long version;

  private String[] availableTranslatedLanguages;
  
  private String latestUploadedChapterId;

  public Manga(JsonObject mangaJson) {
        this.id = mangaJson.getString("id");
        this.title = mangaJson.getJsonObject("attributes").getJsonObject("title").getString("en");
        this.author = null; // You can assign a value from mangaJson's "relationships" object if it exists
        this.description = mangaJson.getJsonObject("attributes").getJsonObject("description").getString("en");
        this.altTitles = mangaJson.getJsonObject("attributes").getJsonArray("altTitles")
            .getValuesAs(JsonObject.class)
            .stream()
            .map(json -> json.getString("en"))
            .toArray(String[]::new);
        this.isLocked = mangaJson.getJsonObject("attributes").getBoolean("isLocked");
        this.originalLanguage = mangaJson.getJsonObject("attributes").getString("originalLanguage");
        this.lastVolume = mangaJson.getJsonObject("attributes").getString("lastVolume");
        this.lastChapter = mangaJson.getJsonObject("attributes").getString("lastChapter");
        this.publicationDemographic = mangaJson.getJsonObject("attributes").getString("publicationDemographic");
        this.status = mangaJson.getJsonObject("attributes").getString("status");
        this.year = mangaJson.getJsonObject("attributes").getJsonNumber("year").longValue();
        this.imageUrl = null; // You can assign a value from mangaJson's "relationships" object if it exists
        this.contentRating = mangaJson.getJsonObject("attributes").getString("contentRating");
        this.state = mangaJson.getJsonObject("attributes").getString("state");
        this.chapterNumbersResetOnNewVolume = mangaJson.getJsonObject("attributes").getBoolean("chapterNumbersResetOnNewVolume");
        this.createdAt = OffsetDateTime.parse(mangaJson.getJsonObject("attributes").getString("createdAt"));
        this.updatedAt = OffsetDateTime.parse(mangaJson.getJsonObject("attributes").getString("updatedAt"));
        this.version = mangaJson.getJsonObject("attributes").getJsonNumber("version").longValue();
        this.availableTranslatedLanguages = mangaJson.getJsonObject("attributes").getJsonArray("availableTranslatedLanguages")
            .getValuesAs(JsonObject.class)
            .stream()
            .map(json -> json.getString("en"))
            .toArray(String[]::new);
        this.latestUploadedChapterId = mangaJson.getJsonObject("attributes").getString("latestUploadedChapter");

        // Parse tags
        this.tags = new ArrayList<>();
        JsonArray tagsJsonArray = mangaJson.getJsonObject("attributes").getJsonArray("tags");
        for (JsonObject tagJson : tagsJsonArray.getValuesAs(JsonObject.class)) {
            MangaTag tag = MangaTag.fromTag(tagJson.getJsonObject("attributes").getJsonObject("name").getString("en"));
            this.tags.add(tag);

        }
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  
}