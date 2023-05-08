package dev.kurumiDisciples.javadex.api.manga;

import dev.kurumiDisciples.javadex.api.manga.MangaTag;

import dev.kurumiDisciples.javadex.api.entities.ISnowflake;
import dev.kurumiDisciples.javadex.api.entities.Chapter;

import dev.kurumiDisciples.javadex.api.entities.enums.*;

import java.util.List;
import java.util.UUID;

import java.time.OffsetDateTime;

import javax.json.*;
import javax.json.stream.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.*;
import java.util.Comparator;

import java.io.IOException; 

import dev.kurumiDisciples.javadex.api.requests.utils.GetAction;
import dev.kurumiDisciples.javadex.api.exceptions.ErrorException;

public class Manga implements ISnowflake{


  private UUID id;

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
  private Demographic publicationDemographic;

  /* change to a class for status */
  private Status status;

  private long year;

  private String imageUrl;
  
  /* change to a class for content rating */
  private ContentRating contentRating;

  private String state;
  
  private boolean chapterNumbersResetOnNewVolume;

  private OffsetDateTime createdAt;

  private OffsetDateTime updatedAt;

  private long version;

  private String[] availableTranslatedLanguages;
  
  private String latestUploadedChapterId;

  public Manga(JsonObject mangaJson) {
    JsonObject original = mangaJson;
    try {
    mangaJson = mangaJson.getJsonObject("data");
    this.id = UUID.fromString(mangaJson.getString("id"));
    } catch (Exception e) {
      mangaJson = original;
      this.id = UUID.fromString(mangaJson.getString("id"));
    }
        this.title = mangaJson.getJsonObject("attributes").getJsonObject("title").getString("en", "no_title");
        this.author = null; // You can assign a value from mangaJson's "relationships" object if it exists
        this.description = mangaJson.getJsonObject("attributes").getJsonObject("description").getString("en", "not_available");
        
        this.isLocked = mangaJson.getJsonObject("attributes").getBoolean("isLocked");
        this.originalLanguage = mangaJson.getJsonObject("attributes").getString("originalLanguage");
        this.lastVolume = mangaJson.getJsonObject("attributes").getString("lastVolume", "no_value");
        this.lastChapter = mangaJson.getJsonObject("attributes").getString("lastChapter", "no_value");
        this.publicationDemographic = Demographic.getDemographic(mangaJson.getJsonObject("attributes").getString("publicationDemographic", "none"));
        this.status = Status.getStatus(mangaJson.getJsonObject("attributes").getString("status"));
        this.year = Long.parseLong(mangaJson.getJsonObject("attributes").getString("year", "1970"));
        this.imageUrl = null; // You can assign a value from mangaJson's "relationships" object if it exists
        this.contentRating = ContentRating.getContentRating(mangaJson.getJsonObject("attributes").getString("contentRating"));
        this.state = mangaJson.getJsonObject("attributes").getString("state");
        this.chapterNumbersResetOnNewVolume = mangaJson.getJsonObject("attributes").getBoolean("chapterNumbersResetOnNewVolume");
        this.createdAt = OffsetDateTime.parse(mangaJson.getJsonObject("attributes").getString("createdAt"));
        this.updatedAt = OffsetDateTime.parse(mangaJson.getJsonObject("attributes").getString("updatedAt"));
        this.version = mangaJson.getJsonObject("attributes").getJsonNumber("version").longValue();
        
        this.latestUploadedChapterId = mangaJson.getJsonObject("attributes").getString("latestUploadedChapter", "none");

        // Parse tags
        

  }

  

  @Override
  public String getId() {
    return id.toString();
  }

  public UUID getUUID(){
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

  public ContentRating getContentRating(){
    return contentRating;
  }

  public Demographic getPublicationDemographic() {
    return publicationDemographic;
  }

  public Status getStatus() {
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
  
  public List<UUID> retrieveChaptersIds(TranslatedLanguage lang){
    GetAction getAction = new GetAction("https://api.mangadex.org/manga/" + getId() + "/aggregate?translatedLanguage[]=" + lang.getLanguage());

    try{
    JsonObject s = getAction.execute();  
      
      List<UUID> chaptersList = parseIds(s);
      Collections.reverse(chaptersList);
      return chaptersList;
    }

    catch (Exception e){
      e.printStackTrace();
      return null;
    }
  } 
  

  public CompletableFuture<List<Chapter>> retrieveChaptersOrdered(TranslatedLanguage language){
    return CompletableFuture.supplyAsync(() -> {
      List<UUID> ids = retrieveChaptersIds(language);
      List<Chapter> chapters = new ArrayList<>();
      if (ids == null) return null;

      for (UUID id : ids) {
        try{
        GetAction getAction = new GetAction("https://api.mangadex.org/chapter/" + id);
        chapters.add(new Chapter(getAction.execute().getJsonObject("data")));
        }
        catch (Exception e){
          e.printStackTrace();
        }
      }

      return chapters;
    });
  }

  public CompletableFuture<Chapter> retrieveChapterByNumber(TranslatedLanguage lang, int number) {
  if (lang == null || number <= 0) {
    return CompletableFuture.completedFuture(null);
  }

  CompletableFuture<List<UUID>> idsFuture = CompletableFuture.supplyAsync(() -> retrieveChaptersIds(lang));

  return idsFuture.thenComposeAsync(ids -> {
    if (ids == null || ids.isEmpty() || number > ids.size()) {
      return CompletableFuture.completedFuture(null);
    }

    UUID chapterId = ids.get(number - 1);
    GetAction getAction = new GetAction("https://api.mangadex.org/chapter/" + chapterId);
    CompletableFuture<JsonObject> responseFuture = CompletableFuture.supplyAsync(() -> {
      try {
        return getAction.execute().getJsonObject("data");
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });

    return responseFuture.handle((jsonObject, throwable) -> {
      if (throwable != null) {
        throwable.printStackTrace();
        return null;
      }

      return new Chapter(jsonObject);
    });
  }, Executors.newCachedThreadPool()).exceptionally(throwable -> {
    throwable.printStackTrace();
    return null;
  });
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

  private static List<UUID> parseIds(JsonObject jsonObject) {
        List<UUID> uuids = new ArrayList<>();

        JsonObject volumes = jsonObject.getJsonObject("volumes");

        volumes.values().forEach(volume -> {
            JsonObject chapters = volume.asJsonObject().getJsonObject("chapters");

            chapters.values().forEach(chapter -> {
                String id = chapter.asJsonObject().getString("id");

                if (id != null) {
                    uuids.add(UUID.fromString(id));
                }
            });
        });

        return uuids;
    }
}