package dev.kurumiDisciples.javadex.api.requests;

import dev.kurumiDisciples.javadex.api.entities.Chapter;
import dev.kurumiDisciples.javadex.api.entities.PageProxy;
import dev.kurumiDisciples.javadex.api.exceptions.*;
import dev.kurumiDisciples.javadex.api.requests.utils.GetAction;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PageBuilder {

    private static final String API_SERVER = "https://api.mangadex.org/at-home/server/";
    private static final String UPLOADS_SERVER = "https://uploads.mangadex.org/data/";
    private static final Logger LOGGER = Logger.getLogger(PageBuilder.class.getName());

    public static List<PageProxy> getPages(Chapter chapter) {
        try {
            GetAction action = new GetAction(API_SERVER + chapter.getId());
            JsonObject jsonObject = action.execute();
            JsonObject chapterData = jsonObject.getJsonObject("chapter");
            String hash = chapterData.getString("hash");
            JsonArray data = chapterData.getJsonArray("data");

            return IntStream.range(0, data.size())
                    .mapToObj(i -> new PageProxy(String.valueOf(i + 1), chapter, buildPageUrl(hash, data.getString(i))))
                    .collect(Collectors.toList());
        } catch (IOException | ErrorException | RateLimitException e) {
            LOGGER.log(Level.WARNING, "Exception occurred in getPages()", e);
            return Collections.emptyList();
        }
    }

    private static String buildPageUrl(String hash, String dataPart) {
        return UPLOADS_SERVER + hash + "/" + dataPart;
    }
}
