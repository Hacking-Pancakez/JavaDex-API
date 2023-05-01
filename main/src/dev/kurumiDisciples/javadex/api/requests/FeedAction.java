package dev.kurumiDisciples.javadex.api.requests;

import dev.kurumiDisciples.javadex.api.manga.Manga;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.json.*;
import javax.json.stream.*;
import java.util.Map;
import java.time.OffsetDateTime;

import dev.kurumiDisciples.javadex.api.requests.utils.*;
import dev.kurumiDisciples.javadex.api.manga.*;
import dev.kurumiDisciples.javadex.api.entities.enums.*;
import dev.kurumiDisciples.javadex.api.entities.*;
import dev.kurumiDisciples.javadex.api.JavaDex;

import dev.kurumiDisciples.javadex.api.requests.utils.MangaQueryJsonConverter;
import dev.kurumiDisciples.javadex.api.exceptions.*;

import java.util.concurrent.CompletableFuture;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import java.util.ArrayList;

import java.util.concurrent.CompletionException;

public class FeedAction {

  private JsonObject header;
  private Integer limit = 100;
  private Integer offset = 0;
  private List<ContentRating> contentRating = new ArrayList<>();
  private JavaDex user;
  private List<TranslatedLanguage> translatedLanguages = new ArrayList<>();
  private OffsetDateTime createdAtSince;
  private OffsetDateTime updateAtSince;
  private OffsetDateTime pubishAtSince;
  private List<ScanlationGroup> excludedGroups = new ArrayList<>();
  private Integer includeEmptyPages;
  private Integer includeFuturePublishAt;
  private Integer includeExternalUrl;
  private Integer includeFutureUpdates;
  private Order createdAtOrder = Order.DESCENDING;

  public FeedAction(JavaDex user){
    if (!user.isLoggedIn()) throw new IllegalArgumentException("You must be logged in to do this");
    this.user = user;
  }

  public FeedAction(JsonObject header){
    this.header = header;
  }

  public FeedAction setLimit(Integer limit){
    if (limit < 0 || limit > 100) throw new IllegalArgumentException("Limit must be between 0 and 100");
    this.limit = limit;
    return this;
  }

  public FeedAction setOffset(Integer offset){
    if (offset < 0) throw new IllegalArgumentException("Offset must be greater than or equal to 0");
    this.offset = offset;
    return this;
  }
  
  public FeedAction addContentRatings(List<ContentRating> contentRatings){
    /*if the list already contains a value do not add that value again*/
    for (ContentRating contentRating : contentRatings){
      if (!this.contentRating.contains(contentRating)){
        this.contentRating.add(contentRating);
      }
    }
    return this;
  }

  public FeedAction addContentRating(ContentRating contentRating){
    /*if the list already contains a value do not add that value again*/
    if (!this.contentRating.contains(contentRating)){
      this.contentRating.add(contentRating);
    }
    return this;
  }

  public FeedAction addTranslatedLanguages(List<TranslatedLanguage> translatedLanguages){
    /*if the list already contains a value do not add that value again*/
    for (TranslatedLanguage translatedLanguage : translatedLanguages){
      if (!this.translatedLanguages.contains(translatedLanguage)){
        this.translatedLanguages.add(translatedLanguage);
      }
    }
    return this;
  }
  
  public FeedAction addTranslatedLanguage(TranslatedLanguage translatedLanguage){
    /*if the list already contains a value do not add that value again*/
    if (!this.translatedLanguages.contains(translatedLanguage)){
      this.translatedLanguages.add(translatedLanguage);
    }
    return this;
  }

  public FeedAction setCreatedAtSince(OffsetDateTime createdAtSince){
    this.createdAtSince = createdAtSince;
    return this;
  }
  
  public FeedAction setUpdateAtSince(OffsetDateTime updateAtSince){
    this.updateAtSince = updateAtSince;
    return this;
  }

  public FeedAction setPubishAtSince(OffsetDateTime pubishAtSince){
    this.pubishAtSince = pubishAtSince;
    return this;
  }
  
  public FeedAction setExcludedGroups(List<ScanlationGroup> excludedGroups){
    /*if the list already contains a value do not add that value again*/
    for (ScanlationGroup excludedGroup : excludedGroups){
      if (!this.excludedGroups.contains(excludedGroup)){
        this.excludedGroups.add(excludedGroup);
      }
    }
    return this;
  }

  public FeedAction addExcludedGroup(ScanlationGroup excludedGroup){
    /*if the list already contains a value do not add that value again*/
    if (!this.excludedGroups.contains(excludedGroup)){
      this.excludedGroups.add(excludedGroup);
    }
    return this;
  }
  
  public FeedAction setIncludeEmptyPages(boolean includeEmptyPages){
    this.includeEmptyPages = includeEmptyPages? 1 : 0;
    return this;
  }

  public FeedAction setIncludeFuturePublishAt(boolean includeFuturePublishAt){
    this.includeFuturePublishAt = includeFuturePublishAt? 1 : 0;
    return this;
  }
  
  public FeedAction setIncludeExternalUrl(boolean includeExternalUrl){
    this.includeExternalUrl = includeExternalUrl? 1 : 0;
    return this;
  }

  public FeedAction setIncludeFutureUpdates(boolean includeFutureUpdates){
    this.includeFutureUpdates = includeFutureUpdates? 1 : 0;
    return this;
  }

  public Integer getLimit() {
    return limit;
  }

  public Integer getOffset() {
    return offset;
  }

  public List<ContentRating> getContentRating() {
    return contentRating;
  }

  public List<TranslatedLanguage> getTranslatedLanguages() {
    return translatedLanguages;
  }

  public OffsetDateTime getCreatedAtSince() {
    return createdAtSince;
  }

  public Order getCreatedAtOrder() {
    return createdAtOrder;
  }

  public OffsetDateTime getUpdateAtSince() {
    return updateAtSince;
  }

  public OffsetDateTime getPubishAtSince() {
    return pubishAtSince;
  }

  public List<ScanlationGroup> getExcludedGroups() {
    return excludedGroups;
  }

  public Integer getIncludeEmptyPages() {
    return includeEmptyPages;
  }

  public Integer getIncludeFuturePublishAt() {
    return includeFuturePublishAt;
  }
  
  public Integer getIncludeExternalUrl() {
    return includeExternalUrl;
  }

  public Integer getIncludeFutureUpdates() {
    return includeFutureUpdates;
  }

  public CompletableFuture<List<Chapter>> submit() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return retrieve();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
  });
}

  public List<Chapter> retrieve(){

    JsonObject param = FeedQueryJsonConverter.toJson(this);
    JsonObject result = null;
    GetAction getAction = new GetAction(
      "https://api.mangadex.org/user/follows/manga/feed", header, param
    );

    try {
      result = getAction.execute();
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    JsonArray data = result.getJsonArray("data");
    List<Chapter> chapters = new ArrayList<Chapter>();
    for (int i = 0; i < data.size(); i++) {
      JsonObject chapter = data.getJsonObject(i);
      chapters.add(new Chapter(chapter));
    }

    return chapters;
  }
}