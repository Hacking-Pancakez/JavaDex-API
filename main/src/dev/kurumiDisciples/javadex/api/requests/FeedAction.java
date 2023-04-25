package dev.kurumiDisciples.javadex.api.requests;

import dev.kurumiDisciples.javadex.api.manga.Manga;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.json.*;
import javax.json.stream.*;
import java.util.Map;
import java.time.OffsetDateTime;

import dev.kurumiDisciples.javadex.api.requests.utils.GetAction;
import dev.kurumiDisciples.javadex.api.manga.*;
import dev.kurumiDisciples.javadex.api.entities.enums.*;
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

  private Integer limit;
  private Integer offset;
  private List<ContentRating> contentRating = new ArrayList<>();
  private JavaDex user;
  private List<TranslatedLanguage> translatedLanguages = new ArrayList<>();
  private OffsetDateTime createdAtSince;
  private OffsetDateTime updateAtSince;
  private OffsetDateTime pubishAtSince;
}