package dev.kurumiDisciples.javadex.api.entities;

import java.time.OffsetDateTime;

/*Write documentation*/
public interface ISnowflake {

  String getId();

  OffsetDateTime getCreatedAt();

  OffsetDateTime getUpdatedAt();
}