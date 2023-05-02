package dev.kurumiDisciples.javadex.api.requests.utils;


import java.util.UUID;

import dev.kurumiDisciples.javadex.api.entities.enums.*;

import dev.kurumiDisciples.javadex.api.manga.*;

import java.util.List;

import dev.kurumiDisciples.javadex.api.requests.SearchAction;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class MangaQueryJsonConverter {
    public static JsonObject toJson(Integer limit, Integer offset, String title, UUID authorOrArtist,
        List < UUID > authors, List < UUID > artists, Integer year,
        List < MangaTag > includedTags, String includedTagsMode,
        List < MangaTag > excludedTags, String excludedTagsMode,
        Status status, List < OriginalLanguage > originalLanguages,
        List < OriginalLanguage > excludedLanguages,
        List < TranslatedLanguage > availableTranslatedLanguages,
        Demographic demographic, ContentRating contentRating,
        Boolean hasAvailableChapters) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if (limit != null) builder.add("limit", limit);
        if (offset != null) builder.add("offset", offset);
        if (title != null) builder.add("title", title);
        if (authorOrArtist != null) builder.add("authorOrArtist", authorOrArtist.toString());
        if (authors != null && !authors.isEmpty()) builder.add("authors", buildJsonArrayFromUUIDs(authors));
        if (artists != null && !artists.isEmpty()) builder.add("artists", buildJsonArrayFromUUIDs(artists));
        if (year != null) builder.add("year", year);
        if (includedTags != null && !includedTags.isEmpty()) builder.add("includedTags[]", buildJsonArrayFromMangaTags(includedTags));
        if (includedTagsMode != null) builder.add("includedTagsMode", includedTagsMode);
        if (excludedTags != null && !excludedTags.isEmpty()) builder.add("excludedTags[]", buildJsonArrayFromMangaTags(excludedTags));
        if (excludedTagsMode != null) builder.add("excludedTagsMode", excludedTagsMode);
        if (status != null) builder.add("status", status.toString());
        if (originalLanguages != null && !originalLanguages.isEmpty()) builder.add("originalLanguages[]", buildJsonArrayFromOriginalLanguages(originalLanguages));
        if (excludedLanguages != null && !excludedLanguages.isEmpty()) builder.add("excludedLanguages[]", buildJsonArrayFromOriginalLanguages(excludedLanguages));
        if (availableTranslatedLanguages != null && !availableTranslatedLanguages.isEmpty()) builder.add("availableTranslatedLanguages[]", buildJsonArrayFromTranslatedLanguages(availableTranslatedLanguages));
        if (demographic != null) builder.add("demographic[]", demographic.toString());
        if (contentRating != null) builder.add("contentRating[]", contentRating.toString());
        if (hasAvailableChapters != null) builder.add("hasAvailableChapters", hasAvailableChapters);
        return builder.build();
    }

    public static JsonObject toJson(SearchAction action) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        if (action.getLimit() != null) builder.add("limit", String.valueOf(action.getLimit()));
        if (action.getOffset() != null) builder.add("offset", String.valueOf(action.getOffset()));
        if (action.getQuery() != null) builder.add("title", action.getQuery());
        if (action.getAuthorOrArtist() != null) builder.add("authorOrArtist", action.getAuthorOrArtist().toString());
        if (action.getAuthors() != null && !action.getAuthors().isEmpty()) builder.add("authors", buildJsonArrayFromUUIDs(action.getAuthors()));
        if (action.getArtists() != null && !action.getArtists().isEmpty()) builder.add("artists", buildJsonArrayFromUUIDs(action.getArtists()));
        if (action.getYear() != null) builder.add("year", String.valueOf(action.getYear()));
        if (action.getIncludedTags() != null && !action.getIncludedTags().isEmpty()) builder.add("includedTags[]", buildJsonArrayFromMangaTags(action.getIncludedTags()));
        if (action.getIncludedTagsMode() != null) builder.add("includedTagsMode", action.getIncludedTagsMode().getValue());
        if (action.getExcludedTags() != null && !action.getExcludedTags().isEmpty()) builder.add("excludedTags[]", buildJsonArrayFromMangaTags(action.getExcludedTags()));
        if (action.getExcludedTagsMode() != null) builder.add("excludedTagsMode", action.getExcludedTagsMode().getValue());
        if (action.getStatus() != null) builder.add("status", action.getStatus().getValue());
        if (action.getOriginalLanguages() != null && !action.getOriginalLanguages().isEmpty()) builder.add("originalLanguages[]", buildJsonArrayFromOriginalLanguages(action.getOriginalLanguages()));
        if (action.getExcludedLanguages() != null && !action.getExcludedLanguages().isEmpty()) builder.add("excludedLanguages[]", buildJsonArrayFromOriginalLanguages(action.getExcludedLanguages()));
        if (action.getAvailableTranslatedLanguages() != null && !action.getAvailableTranslatedLanguages().isEmpty()) builder.add("availableTranslatedLanguages[]", buildJsonArrayFromTranslatedLanguages(action.getAvailableTranslatedLanguages()));
        if (action.getDemographic() != null) builder.add("publicationDemographic[]", action.getDemographic().getValue());
        if (action.getContentRatings().size() != 0) builder.add("contentRating[]", buildJsonArrayFromContentRating(action.getContentRatings()));
        if (action.hasAvailableChapters() != null) builder.add("hasAvailableChapters", action.hasAvailableChapters());
        return builder.build();
    }



    private static JsonArrayBuilder buildJsonArrayFromUUIDs(List < UUID > uuids) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (UUID uuid: uuids) {
            builder.add(uuid.toString());
        }
        return builder;
    }

  private static JsonArrayBuilder buildJsonArrayFromContentRating(List < ContentRating > contentRatings) {
    JsonArrayBuilder builder = Json.createArrayBuilder();
    if (contentRatings!= null &&!contentRatings.isEmpty()) {
      contentRatings.forEach(contentRating -> builder.add(contentRating.getValue()));
      return builder;
    }
    return builder;
  }
  
    private static JsonArrayBuilder buildJsonArrayFromMangaTags(List < MangaTag > tags) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (MangaTag tag: tags) {
            builder.add(tag.getId().toString());
        }
        return builder;
    }

    private static JsonArrayBuilder buildJsonArrayFromOriginalLanguages(List < OriginalLanguage > languages) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (OriginalLanguage language: languages) {
            builder.add(language.toString());
        }
        return builder;
    }

    private static JsonArrayBuilder buildJsonArrayFromTranslatedLanguages(List < TranslatedLanguage > languages) {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (TranslatedLanguage language: languages) {
            builder.add(language.getLanguage());
        }
        return builder;
    }
}