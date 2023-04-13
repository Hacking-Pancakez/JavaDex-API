package dev.kurumiDisciples.javadex.api.manga;

import dev.kurumiDisciples.javadex.api.manga.MangaTag;

import dev.kurumiDisciples.javadex.api.entities.ISnowflake;
import dev.kurumiDisciples.javadex.api.entities.Chapter;

import java.util.List;

import java.time.OffsetDateTime;

import javax.json.*;
import javax.json.stream.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

import java.io.IOException; 

import dev.kurumiDisciples.javadex.api.requests.utils.GetAction;
import dev.kurumiDisciples.javadex.api.exceptions.ErrorException;

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
    mangaJson = mangaJson.getJsonObject("data");    
    this.id = mangaJson.getString("id");
        this.title = mangaJson.getJsonObject("attributes").getJsonObject("title").getString("en");
        this.author = null; // You can assign a value from mangaJson's "relationships" object if it exists
        this.description = mangaJson.getJsonObject("attributes").getJsonObject("description").getString("en");
        
        this.isLocked = mangaJson.getJsonObject("attributes").getBoolean("isLocked");
        this.originalLanguage = mangaJson.getJsonObject("attributes").getString("originalLanguage");
        this.lastVolume = mangaJson.getJsonObject("attributes").getString("lastVolume");
        this.lastChapter = mangaJson.getJsonObject("attributes").getString("lastChapter");
        this.publicationDemographic = mangaJson.getJsonObject("attributes").getString("publicationDemographic");
        this.status = mangaJson.getJsonObject("attributes").getString("status");
        this.year = Long.parseLong(mangaJson.getJsonObject("attributes").getString("year", "1970"));
        this.imageUrl = null; // You can assign a value from mangaJson's "relationships" object if it exists
        this.contentRating = mangaJson.getJsonObject("attributes").getString("contentRating");
        this.state = mangaJson.getJsonObject("attributes").getString("state");
        this.chapterNumbersResetOnNewVolume = mangaJson.getJsonObject("attributes").getBoolean("chapterNumbersResetOnNewVolume");
        this.createdAt = OffsetDateTime.parse(mangaJson.getJsonObject("attributes").getString("createdAt"));
        this.updatedAt = OffsetDateTime.parse(mangaJson.getJsonObject("attributes").getString("updatedAt"));
        this.version = mangaJson.getJsonObject("attributes").getJsonNumber("version").longValue();
        
        this.latestUploadedChapterId = mangaJson.getJsonObject("attributes").getString("latestUploadedChapter");

        // Parse tags
        

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

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }
  
  public String getDescription() {
    return description;
  }
  
  public String[] getAltTitles() {
    return altTitles;
  }

  public boolean isLocked() {
    return isLocked;
  }

  public String getOriginalLanguage() {
    return originalLanguage;
  }

  public String getLastVolume() {
    return lastVolume;
  }

  public String getLastChapter() {
    return lastChapter;
  }

  public String getContentRating(){
    return contentRating;
  }

  public String getPublicationDemographic() {
    return publicationDemographic;
  }

  public String getStatus() {
    return status;
  }

  public long getYear() {
    return year;
  }

  public String getLatestUploadedChapterId() {
    return latestUploadedChapterId;
  }


  public List<Chapter> retrieveFeed() {
    GetAction getAction = new GetAction("https://api.mangadex.org/manga/" + getId() + "/feed", Json.createObjectBuilder().build(), Json.createObjectBuilder().build());

    try{
    JsonObject response = getAction.execute();
    
    if (isError(response)) throw new RuntimeException("Error retrieving manga feed: " + response.getJsonArray("errors").getJsonObject(0).getString("detail"));
    JsonArray chapters = response.getJsonArray("data");

    List<Chapter> chaptersList = new ArrayList<>();

    for (int i = 0; i < chapters.size(); i++) {
      JsonObject chapter = chapters.getJsonObject(i);
      chaptersList.add(new Chapter(chapter));
    } 
    return chaptersList;
    }
    catch (IOException e){
      e.printStackTrace();
      return null;
    }
    catch (ErrorException e){
      e.printStackTrace();
      return null;
    }
  }

  public List<Chapter> retrieveChaptersByLang(String lang){
    return retrieveChaptersByLang(lang, false);
  }
  
  public List<Chapter> retrieveChaptersByLang(String lang, boolean sort) {
    GetAction getAction = new GetAction("https://api.mangadex.org/manga/" + getId() + "/feed?translatedLanguage[]=" + lang, Json.createObjectBuilder().build(), Json.createObjectBuilder().build());

    try{
    JsonObject response = getAction.execute();
    
    if (isError(response)) throw new RuntimeException("Error retrieving manga feed: " + response.getJsonArray("errors").getJsonObject(0).getString("detail"));
    JsonArray chapters = response.getJsonArray("data");

    List<Chapter> chaptersList = new ArrayList<>();

    for (int i = 0; i < chapters.size(); i++) {
      JsonObject chapter = chapters.getJsonObject(i);
      chaptersList.add(new Chapter(chapter));
    } 
    if (sort) chaptersList = sortChaptersByNumber(chaptersList);
    return chaptersList;
    }
    catch (IOException e){
      e.printStackTrace();
      return null;
    }
    catch (ErrorException e){
      e.printStackTrace();
      return null;
    }
  }


  private static List<Chapter> sortChaptersByNumber(List<Chapter> chapters) {
        List<Chapter> sortedChapters = new ArrayList<>(chapters);
        Collections.sort(sortedChapters, new Comparator<Chapter>() {
            @Override
            public int compare(Chapter c1, Chapter c2) {
                return Double.compare(c1.getChapter(), c2.getChapter());
            }
        });
        return sortedChapters;
    }


  private static boolean isError(JsonObject response) {
    return response.getString("result").equals("error");
  }
}