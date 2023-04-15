import dev.kurumiDisciples.javadex.api.requests.SearchAction;

import dev.kurumiDisciples.javadex.api.manga.*;
import dev.kurumiDisciples.javadex.api.entities.Chapter;

import dev.kurumiDisciples.javadex.api.entities.enums.*;

import java.util.List;

class Main {
  public static void main(String[] args) throws Exception {
    System.out.println(new SearchAction("null")
                    .setLimit(5)
                    .search().get(0).getTitle());
    /*
    Manga DUO = SearchAction.getMangasByName("Attack On Titan").get(0);
    //System.out.println(DUO.getTitle() + "\n" + DUO.retrieveChaptersByLang("en", true).get(0).getTitle());
    System.out.println(DUO.getTitle() + " : " + DUO.getContentRating().getValue());
    List<Chapter> chapters = DUO.retrieveChaptersByLang("en", true);

    chapters.forEach(chapter -> chapter.retrieveHashAsync());
*/
    
  }
}