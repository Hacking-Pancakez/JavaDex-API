package dev.kurumiDisciples.javadex.api.entities;

import java.time.OffsetDateTime;

public interface IPublishable {


  OffsetDateTime getPublishAt();
  OffsetDateTime getReadableAt();
  
}
