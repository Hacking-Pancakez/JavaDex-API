package dev.kurumiDisciples.javadex.api.requests.utils;


import java.util.UUID;

import dev.kurumiDisciples.javadex.api.entities.enums.*;

import dev.kurumiDisciples.javadex.api.manga.*;

import java.util.List;

import dev.kurumiDisciples.javadex.api.requests.UserAction;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


public class UserQueryJsonConverter {



  public static JsonObject toJson(UserAction action) {
    
    JsonObjectBuilder builder = Json.createObjectBuilder();
    if (action.getLimit() != null) builder.add("limit", String.valueOf(action.getLimit()));
    if (action.getOffset() != null) builder.add("offset", String.valueOf(action.getOffset()));
    if (action.getQuery() != null) builder.add("username", action.getQuery());
    if (action.getIds().size() != 0) builder.add("ids[]", buildJsonArrayFromUUIDs(action.getIds()));

    return builder.build();
  }

   private static JsonArrayBuilder buildJsonArrayFromUUIDs(List < UUID > uuids) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (UUID uuid: uuids) {
            builder.add(uuid.toString());
        }
        return builder;
    }
}