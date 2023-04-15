package dev.kurumiDisciples.javadex.api.requests;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

import dev.kurumiDisciples.javadex.api.exceptions.ErrorException;

import javax.json.*;
import javax.json.stream.*;

import dev.kurumiDisciples.javadex.api.entities.PageProxy;

import dev.kurumiDisciples.javadex.api.requests.utils.GetAction;

import dev.kurumiDisciples.javadex.api.entities.Chapter;

public class PageBuilder {
  

  private static String API_SERVER = "https://api.mangadex.org/at-home/server/";
  private static String UPLOADS_SERVER = "https://uploads.mangadex.org/data/";
  
  public static List<PageProxy> getPages(Chapter chapter) throws ErrorException, IOException{
    GetAction action = new GetAction(API_SERVER + chapter.getId());

    JsonObject jsonObject = action.execute();

    JsonObject chapterData = jsonObject.getJsonObject("chapter");

    String hash = chapterData.getString("hash");

    List<PageProxy> pages = new ArrayList<>();

    JsonArray data = chapterData.getJsonArray("data");

    for(int i = 0; i < data.size(); i++){
      int currentPageNum = i + 1;
      String url = UPLOADS_SERVER + hash + "/" + data.getString(i);
      pages.add(new PageProxy(String.valueOf(currentPageNum), chapter, url));
    }

    return pages;
  }
}