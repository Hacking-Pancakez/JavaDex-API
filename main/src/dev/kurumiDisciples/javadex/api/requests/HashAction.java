package dev.kurumiDisciples.javadex.api.requests;

import javax.json.*;
import javax.json.stream.*;
import java.util.Map;

import dev.kurumiDisciples.javadex.api.requests.utils.GetAction;

import java.util.concurrent.CompletableFuture;
import java.io.IOException;
import java.util.List;

import dev.kurumiDisciples.javadex.api.entities.Chapter;
import dev.kurumiDisciples.javadex.api.exceptions.ErrorException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HashAction {


  public static ExecutorService executor = Executors.newFixedThreadPool(10);

  public static String getHash(Chapter chapter) throws IOException, ErrorException{
    GetAction getAction = new GetAction("https://api.mangadex.org/at-home/server/" + chapter.getId());

    JsonObject json = getAction.execute();

    if (isError(json)) throw new ErrorException(json);

    
    
    return json.getJsonObject("chapter").getString("hash");
  }

  private static boolean isError(JsonObject json) {
    return json.getString("result").equals("error");
  }

  public static CompletableFuture<String> getHashAsync(Chapter chapter) {
    CompletableFuture<String> future = new CompletableFuture<>();
    executor.execute(() -> {
      try {
        future.complete(getHash(chapter));
      } catch (IOException | ErrorException ex) {
        future.completeExceptionally(ex);
      }
    });
    return future;
  }
}