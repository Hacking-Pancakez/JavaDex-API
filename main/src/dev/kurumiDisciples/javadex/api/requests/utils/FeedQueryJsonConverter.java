package dev.kurumiDisciples.javadex.api.requests.utils;

import javax.json.*;
import javax.json.stream.*;

import dev.kurumiDisciples.javadex.api.requests.FeedAction;

import dev.kurumiDisciples.javadex.api.entities.*;
import dev.kurumiDisciples.javadex.api.entities.enums.*;

import java.util.List;

public class FeedQueryJsonConverter {


  public static JsonObject toJson(FeedAction f) {
    JsonObjectBuilder json = Json.createObjectBuilder();
    if (f.getLimit() != null) json.add("limit", f.getLimit());
    if (f.getOffset() != null) json.add("offset", f.getOffset());
    if (f.getContentRating().size() != 0) json.add("contentRating[]", contentRatingToJson(f.getContentRating()));
    if (f.getTranslatedLanguages().size() != 0) json.add("translatedLanguage[]", translatedLanguageToJson(f.getTranslatedLanguages()));
    if (f.getCreatedAtSince()!= null) json.add("createdAtSince", f.getCreatedAtSince().toString());
    if (f.getUpdateAtSince()!= null) json.add("updateAtSince", f.getUpdateAtSince().toString());
    json.add("order[createdAt]", f.getCreatedAtOrder().getValue());
    /*add the rest of the avaliable fields*/
    return json.build();
  }

  private static JsonArrayBuilder contentRatingToJson(List<ContentRating> contentRating) {
    JsonArrayBuilder json = Json.createArrayBuilder();
    for (ContentRating cr : contentRating) {
      json.add(cr.getValue());
    }
    return json;
  }

  private static JsonArrayBuilder translatedLanguageToJson(List<TranslatedLanguage> translatedLanguages) {
    JsonArrayBuilder json = Json.createArrayBuilder();
    for (TranslatedLanguage tl : translatedLanguages) {
      json.add(tl.getLanguage());
    }
    return json;
  }
}