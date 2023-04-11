package dev.kurumiDisciples.javadex.api.manga;

import dev.kurumiDisciples.javadex.api.manga.MangaTag;

import java.util.List;

public class Manga {


  private String id;

  private List<MangaTags> tags;

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
}