/**

* The SearchAction class provides methods for searching MangaDex API for manga titles and retrieving manga by ID.
*/
package dev.kurumiDisciples.javadex.api.requests;

import dev.kurumiDisciples.javadex.api.manga.Manga;

import javax.json.*;
import javax.json.stream.*;
import java.util.Map;

import dev.kurumiDisciples.javadex.api.requests.utils.GetAction;
import dev.kurumiDisciples.javadex.api.manga.Manga;

import java.util.concurrent.CompletableFuture;
import java.io.IOException;
import java.util.List;

public class SearchAction {

/**

Retrieves a Manga object for the specified ID from MangaDex API.
@param id a String representing the manga ID.
@return a Manga object containing information about the manga with the specified ID.
*/
  public static Manga getMangaById(String id) {
    try {
    GetAction getAction = new GetAction("https://api.mangadex.org/manga/" + id, Json.createObjectBuilder().build(), Json.createObjectBuilder().build());
    return new Manga(getAction.execute());
    }
    catch (Exception e) {
      System.out.println("Unable to retrieve Manga");
      e.printStackTrace();
    }
    return null;
  }

  /**

* Retrieves a list of Manga objects containing the specified name from MangaDex API.
* @param name a String representing the name of the manga.
* @return a List of Manga objects containing information about the manga(s) with the specified name.
*/
  public static List<Manga> getMangasByName(String name) {
    try {
      GetAction getAction = new GetAction("https://api.mangadex.org/manga?title=" + name, null, null);
      JsonObject jsonObject = getAction.execute();

      JsonArray jsonArray = jsonObject.getJsonArray("data");

      List<Manga> mangaList = new java.util.ArrayList<Manga>();

      for (int i = 0; i < jsonArray.size(); i++) {
        mangaList.add(new Manga(jsonArray.getJsonObject(i)));
      }
      return mangaList;
    }
    catch (Exception e) {
      System.out.println("Unable to retrieve Manga");
      e.printStackTrace();
    }
    return null;
  }
}