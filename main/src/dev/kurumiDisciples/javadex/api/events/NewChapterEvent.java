package dev.kurumiDisciples.javadex.api.events;


import dev.kurumiDisciples.javadex.api.JavaDex;

import dev.kurumiDisciples.javadex.api.entities.Chapter;

import dev.kurumiDisciples.javadex.api.events.*;

import java.time.OffsetDateTime;

/*not ready yet*/
public class NewChapterEvent implements EventImpl {

  private final JavaDex javadex;
  private final OffsetDateTime timestamp;
  private final Chapter chapter;


  public NewChapterEvent(JavaDex javaDex, OffsetDateTime timestamp, Chapter chapter) {
    this.javadex = javaDex;
    this.timestamp = timestamp;
    this.chapter = chapter;
  }
  
  @Override
  public JavaDex getJavaDex(){
    return javadex;
  }  

  @Override
  public OffsetDateTime getTimestamp(){
    return timestamp;
  }

  public Chapter getChapter(){
    return chapter;
  }

}