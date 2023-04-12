package dev.kurumiDisciples.javadex.api.manga;


public enum MangaTag {


  ROMANCE("Romance"), ACTION("Action"), ADVENTURE("Adventure");



  private String tag; 

   MangaTag(String tag) {
    this.tag = tag;
  }

  public String getTag() {
    return tag;
  }

  public static MangaTag fromTag(String tag) {
    for (MangaTag mangaTag : MangaTag.values()) {
      if (mangaTag.getTag().equals(tag)) {
        return mangaTag;
      }
    }
    return null;
  }
}