package dev.kurumiDisciples.javadex.api.requests;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import javax.json.*;
import javax.json.stream.*;

import dev.kurumiDisciples.javadex.api.entities.ScanlationGroup;

import dev.kurumiDisciples.javadex.api.requests.utils.*;

public class GroupAction {

  public static final Integer DEFAULT_LIMIT = 10;

  private static final String API_BASE_PATH = "https://api.mangadex.org/group";

  private Integer limit; // 0-100
  private Integer offset;
  private List<UUID> ids = new ArrayList<>(); //limit 100
  private String name; 
  private String focusedLanguage;


  public GroupAction() {
    this.limit = DEFAULT_LIMIT;
  }

  
  public GroupAction(String name){
    this.name = name;
    this.limit = DEFAULT_LIMIT;
  }


  public GroupAction setLimit(Integer limit) {
    if (limit < 0 || limit > 100) {
      throw new IllegalArgumentException("Limit must be between 0 and 100");
    }
    this.limit = limit;
    return this;
  }
  
  public GroupAction setOffset(Integer offset) {
    if (offset < 0) {
      throw new IllegalArgumentException("Offset must be greater than or equal to 0");
    }
    this.offset = offset;
    return this;
  }

  public GroupAction addId(UUID id) {
    if (ids.size() == 100) {
      throw new IllegalArgumentException("Queryable Id limit is 100");
    }
    this.ids.add(id);
    return this;
  }

  public GroupAction addIds(List<UUID> idsList) {
    if (ids.size() == 100) {
      throw new IllegalArgumentException("Queryable Id limit is 100");
    }
    else if (this.ids.size() + idsList.size() > 100) {
      throw new IllegalArgumentException("Could not add list because limit would be exceeded");
    }
    this.ids.addAll(idsList);
    return this;
  }

  public GroupAction setName(String name) {
    this.name = name;
    return this;
  }
  
  public GroupAction setFocusedLanguage(String focusedLanguage) {
    this.focusedLanguage = focusedLanguage;
    return this;
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

  public String getName() {
    return name;
  }

  public String getFocusedLanguage() {
    return focusedLanguage;
  }
  

  public List<ScanlationGroup> search() throws Exception{
    GetAction getAction = new GetAction(API_BASE_PATH, Json.createObjectBuilder().build(), GroupQueryJsonConverter.toJson(this));

    JsonObject jsonObject = getAction.execute();

    List<ScanlationGroup> groups = new ArrayList<>();

    for (int i = 0; i < jsonObject.getJsonArray("data").size(); i++) {
      groups.add(new ScanlationGroup(jsonObject.getJsonArray("data").getJsonObject(i)));
    }
    return groups;
  }
}