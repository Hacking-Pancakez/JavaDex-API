package dev.kurumiDisciples.javadex.api.manga;

import java.util.UUID;

public enum MangaTag {


  ONESHOT("Oneshot", UUID.fromString("0234a31e-a729-4e28-9d6a-3f87c4966b9e")),
  THRILLER("Thriller", UUID.fromString("07251805-a27e-4d59-b488-f0bfbec15168")),
  AWARD_WINNING("Award Winning", UUID.fromString("0a39b5a1-b235-4886-a747-1d05d216532d")),
  REINCARNATION("Reincarnation", UUID.fromString("0bc90acb-ccc1-44ca-a34a-b9f3a73259d0")),
  SCI_FI("Sci-Fi", UUID.fromString("256c8bd9-4904-4360-bf4f-508a76d67183")),
  TIME_TRAVEL("Time Travel", UUID.fromString("292e862b-2d17-4062-90a2-0356caa4ae27")),
  GENDERSWAP("Genderswap", UUID.fromString("2bd2e8d0-f146-434a-9b51-fc9ff2c5fe6a")),
  LOLI("Loli", UUID.fromString("2d1f5d56-a1e5-4d0d-a961-2193588b08ec"));
  

  private String name;
  private UUID id;
  
  private MangaTag(String name, UUID id) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public UUID getId() {
    return id;
  }

  public static MangaTag getByName(String name) {
    for (MangaTag tag : MangaTag.values()) {
      if (tag.getName().equals(name)) {
        return tag;
      }
    }
    return null;
  }

  public static MangaTag getById(UUID id) {
    for (MangaTag tag : MangaTag.values()) {
      if (tag.getId().equals(id)) {
        return tag;
      }
    }
    return null;
  }
}