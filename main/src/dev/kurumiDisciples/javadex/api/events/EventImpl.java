package dev.kurumiDisciples.javadex.api.events;


import dev.kurumiDisciples.javadex.api.JavaDex;

import java.time.OffsetDateTime;

public interface EventImpl {
  
  public JavaDex getJavaDex();
  public OffsetDateTime getTimestamp();
  
}