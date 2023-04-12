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


  public static JsonObject getMangaById(String id) {
    try {
    GetAction getAction = new GetAction("https://api.mangadex.org/manga/" + id, Json.createObjectBuilder().build(), Json.createObjectBuilder().build());
    return getAction.execute();
    }
    catch (IOException e) {
      System.out.println("Unable to retrieve Manga");
      e.printStackTrace();
    }
    return null;
  }

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
    catch (IOException e) {
      System.out.println("Unable to retrieve Manga");
      e.printStackTrace();
    }
    return null;
  }
}