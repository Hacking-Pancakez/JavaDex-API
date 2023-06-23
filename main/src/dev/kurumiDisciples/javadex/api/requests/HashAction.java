package dev.kurumiDisciples.javadex.api.requests;

import dev.kurumiDisciples.javadex.api.entities.Chapter;
import dev.kurumiDisciples.javadex.api.exceptions.*;
import dev.kurumiDisciples.javadex.api.requests.utils.GetAction;

import javax.json.JsonObject;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HashAction {
  private static final ExecutorService executor = Executors.newFixedThreadPool(10);

  private static String getHash(Chapter chapter) throws IOException, ErrorException, RateLimitException {
    try (GetAction getAction = new GetAction("https://api.mangadex.org/at-home/server/" + chapter.getId())) {
      JsonObject responseJson = getAction.execute();
      if (isError(responseJson)) throw new ErrorException(responseJson);
      return responseJson.getJsonObject("chapter").getString("hash");
    }
  }

  private static boolean isError(JsonObject responseJson) {
    return responseJson.getString("result").equals("error");
  }

  public static CompletableFuture<String> getHashAsync(Chapter chapter) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return getHash(chapter);
      } catch (IOException | ErrorException | RateLimitException ex) {
        throw new RuntimeException(ex);
      }
    }, executor);
  }
}
