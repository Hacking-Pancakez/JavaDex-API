package dev.kurumiDisciples.javadex.api.manga;


public enum MangaTags {


  ROMANCE("Romance"), ACTION("Action"), ADVENTURE("Adventure");



  private String tag; 

  public MangaTags(String tag) {
    this.tag = tag;
  }

  public String getTag() {
    return tag;
  }
}