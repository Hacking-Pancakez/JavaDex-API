package dev.kurumiDisciples.javadex.api.requests.utils;


import java.util.UUID;

import dev.kurumiDisciples.javadex.api.entities.enums.*;

import dev.kurumiDisciples.javadex.api.manga.*;

import java.util.List;

import dev.kurumiDisciples.javadex.api.requests.GroupAction;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


public class GroupQueryJsonConverter {


  public static JsonObject toJson(GroupAction g){
    JsonObjectBuilder builder = Json.createObjectBuilder();
    if (g.getLimit()!= null) builder.add("limit", g.getLimit());
    if (g.getOffset()!= null) builder.add("offset", g.getOffset());
    if (g.getIds().size()!= 0) builder.add("ids[]", toJson(g.getIds()));
    if (g.getName()!= null) builder.add("name", g.getName());
    if (g.getFocusedLanguage()!= null) builder.add("focusedLanguage", g.getFocusedLanguage());
    return builder.build();
  }


  private static JsonArrayBuilder toJson(List<UUID> ids){
    JsonArrayBuilder builder = Json.createArrayBuilder();
    for (UUID id : ids) builder.add(id.toString());
    return builder;
  }
}