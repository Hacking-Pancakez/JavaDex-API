package dev.kurumiDisciples.javadex.api.requests;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.json.*;
import javax.json.stream.*;
import java.util.Map;

import dev.kurumiDisciples.javadex.api.exceptions.*;

import dev.kurumiDisciples.javadex.api.entities.User;
import dev.kurumiDisciples.javadex.api.requests.utils.UserQueryJsonConverter;
import dev.kurumiDisciples.javadex.api.requests.utils.GetAction;


import java.util.concurrent.CompletableFuture;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import java.util.concurrent.CompletionException;


public class UserAction {


  private String query = null;
  private Integer limit = null;
  private Integer offset = null;
  private List<UUID> ids = new ArrayList<>();
  private JsonObject header;

  
  public UserAction(String query, JsonObject header){
    this.query = query;
    this.header = header;
  }

  public UserAction(JsonObject header){
    this.query = "";
    this.header = header;
  }


  public UserAction setLimit(int limit) {
    if (limit < 0 || limit > 100) throw new IllegalArgumentException("Limit must be between 0 and 100");
    this.limit = limit;
    return this;
  }

  public UserAction setOffset(int offset) {
     if (offset < 0){
      throw new IllegalArgumentException("Offset must be greater than or equal to 0");
    }
    this.offset = offset;
    return this;
  }

  public UserAction addId(UUID id) {
    if (ids.contains(id)) throw new IllegalArgumentException("Id must be unique");
    ids.add(id);
    return this;
  }

  public UserAction addIds(List<UUID> ids) {
   ids.forEach(this::addId);
   return this;
  }

  public UserAction setQuery(String query) {
    this.query = query;
    return this;
  }

  public String getQuery() {
    return query;
  }

  public Integer getLimit() {
    return limit;
  }

  public Integer getOffset() {
    return offset;
  }

  public List<UUID> getIds() {
    return ids;
  }

  public CompletableFuture<List<User>> submit() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return complete();
      } catch (ErrorException | IOException | RateLimitException e) {
        throw new CompletionException(e);
      }
    });
  }


  public List<User> complete() throws ErrorException, IOException, RateLimitException {
    JsonObject param = UserQueryJsonConverter.toJson(this);
    GetAction action = new GetAction("https://api.mangadex.org/user", header, param);
    JsonObject response = action.execute();

    JsonArray data = response.getJsonArray("data");
    List<User> users = new ArrayList<>();

    for (int i = 0; i < data.size(); i++) {
          users.add(new User(data.getJsonObject(i)));
      }

    return users;
  }

  
}