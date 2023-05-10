package dev.kurumiDisciples.javadex.api.events;


import dev.kurumiDisciples.javadex.api.JavaDex;
import dev.kurumiDisciples.javadex.api.entities.Chapter;
import java.util.List;
import java.time.OffsetDateTime;

public class FeedUpdateEvent implements EventImpl {

  private final JavaDex javadex;
  private final List<Chapter> feed;
  private final OffsetDateTime timestamp;

  public FeedUpdateEvent(JavaDex javadex, List<Chapter> feed, OffsetDateTime timestamp){
    this.javadex = javadex;
    this.feed = feed;
    this.timestamp = timestamp;
  }
  
  
  @Override
  public JavaDex getJavaDex(){
    return javadex;
  }

  @Override
  public OffsetDateTime getTimestamp(){
    return timestamp;
  }
  public List<Chapter> getFeed(){
    return feed;
  }
  
}