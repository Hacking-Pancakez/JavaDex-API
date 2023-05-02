package dev.kurumiDisciples.javadex.api.manga;

import java.util.UUID;

public enum MangaTag {

  //36
  ONESHOT("Oneshot", UUID.fromString("0234a31e-a729-4e28-9d6a-3f87c4966b9e")),
  THRILLER("Thriller", UUID.fromString("07251805-a27e-4d59-b488-f0bfbec15168")),
  AWARD_WINNING("Award Winning", UUID.fromString("0a39b5a1-b235-4886-a747-1d05d216532d")),
  REINCARNATION("Reincarnation", UUID.fromString("0bc90acb-ccc1-44ca-a34a-b9f3a73259d0")),
  SCI_FI("Sci-Fi", UUID.fromString("256c8bd9-4904-4360-bf4f-508a76d67183")),
  TIME_TRAVEL("Time Travel", UUID.fromString("292e862b-2d17-4062-90a2-0356caa4ae27")),
  GENDERSWAP("Genderswap", UUID.fromString("2bd2e8d0-f146-434a-9b51-fc9ff2c5fe6a")),
  LOLI("Loli", UUID.fromString("2d1f5d56-a1e5-4d0d-a961-2193588b08ec")),
  TRADITIONAL_GAMES("Traditional Games", UUID.fromString("31932a7e-5b8e-49a6-9f12-2afa39dc544c")),
  OFFICIAL_COLORED("Official Colored", UUID.fromString("320831a8-4026-470b-94f6-8353740e6f04")),
  HISTORICAL("Historical", UUID.fromString("33771934-028e-4cb3-8744-691e866a923e")),
  MONSTERS("Monsters", UUID.fromString("36fd93ea-e8b8-445e-b836-358f02b3d33d")),
  ACTION("Action", UUID.fromString("391b0423-d847-456f-aff0-8b0cfc03066b")),
  DEMONS("Demons", UUID.fromString("39730448-9a5f-48a2-85b0-a70db87b1233")),
  PSYCHOLOGICAL("Psychological", UUID.fromString("3b60b75c-a2d7-4860-ab56-05f391bb889c")),
  GHOSTS("Ghosts", UUID.fromString("3bb26d85-09d5-4d2e-880c-c34b974339e9")),
  ANIMALS("Animals", UUID.fromString("3de8c75d-8ee3-48ff-98ee-e20a65c86451")),
  LONG_STRIP("Long Strip", UUID.fromString("3e2b8dae-350e-4ab8-a8ce-016e844b9f0d")),
  ROMANCE("Romance", UUID.fromString("423e2eae-a7a2-4a8b-ac03-a8351462d71d")),
  NINJA("Ninja", UUID.fromString("489dd859-9b61-4c37-af75-5b18e88daafc")),
  COMEDY("Comedy", UUID.fromString("4d32cc48-9f00-4cca-9b5a-a839f0764984")),
  MECHA("Mecha", UUID.fromString("50880a9d-5440-4732-9afb-8f457127e836")),
  ANTHOLOGY("Anthology", UUID.fromString("51d83883-4103-437c-b4b1-731cb73d786c")),
  BOYS_LOVE("Boys' Love", UUID.fromString("5920b825-4181-4a17-beeb-9918b0ff7a30")),
  INCEST("Incest", UUID.fromString("5bd0e105-4481-44ca-b6e7-7544da56b1a3")),
  CRIME("Crime", UUID.fromString("5ca48985-9a9d-4bd8-be29-80dc0303db72")),
  SURVIVAL("Survival", UUID.fromString("5fff9cde-849c-4d78-aab0-0d52b2ee1d25")),
  ZOMBIES("Zombies", UUID.fromString("631ef465-9aba-4afb-b0fc-ea10efe274a8")),
  REVERSE_HAREM("Reverse Harem", UUID.fromString("65761a2a-415e-47f3-bef2-a9dababba7a6")),
  SPORTS("Sports", UUID.fromString("69964a64-2f90-4d33-beeb-f3ed2875eb4c")),
  SUPERHERO("Superhero", UUID.fromString("7064a261-a137-4d3a-8848-2d385de3a99c")),
  MARTIAL_ARTS("Martial Arts", UUID.fromString("799c202e-7daa-44eb-9cf7-8a3c0441531e")),
  FAN_COLORED("Fan Colored", UUID.fromString("7b2ce280-79ef-4c09-9b58-12b7c23a9b78")),
  SAMURAI("Samurai", UUID.fromString("81183756-1453-4c81-aa9e-f6e1b63be016")),
  MAGICAL_GIRLS("Magical Girls", UUID.fromString("81c836c9-914a-4eca-981a-560dad663e73")),
  MAFIA("Mafia", UUID.fromString("85daba54-a71c-4554-8a28-9901a8b0afad")),
  ADVENTURE("Adventure", UUID.fromString("87cc87cd-a395-47af-b27a-93258283bbc6")),
  GIRLS_LOVE("Girls' Love", UUID.fromString("a3c67850-4684-404e-9b7f-c69850ee5da6"));
  

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
      if (tag.getName().equalsIgnoreCase(name)) {
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