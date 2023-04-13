import dev.kurumiDisciples.javadex.api.requests.SearchAction;

import dev.kurumiDisciples.javadex.api.manga.Manga;
import dev.kurumiDisciples.javadex.api.entities.Chapter;

import java.util.List;

class Main {
  public static void main(String[] args) {
    Manga DUO = SearchAction.getMangaById("ed996855-70de-449f-bba2-e8e24224c14d");
    //System.out.println(DUO.getTitle() + "\n" + DUO.retrieveChaptersByLang("en", true).get(0).getTitle());
    System.out.println(DUO.getTitle() + ": " + DUO.getContentRating());
    List<Chapter> chapters = DUO.retrieveChaptersByLang("en", true);
    /*for (Chapter chapter : chapters) {
      System.out.println(chapter);
    } */
  }
}