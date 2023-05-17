package dev.kurumiDisciples.javadex.api.listeners.checkers;

import dev.kurumiDisciples.javadex.api.JavaDex;
import dev.kurumiDisciples.javadex.api.manga.Manga;
import dev.kurumiDisciples.javadex.api.events.NewChapterEvent;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.*;
import java.time.OffsetDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewChapterChecker {
  private static final Logger LOGGER = Logger.getLogger(NewChapterChecker.class.getName());
  private static Map<Manga, UUID> updateList = new HashMap<>();
  private static JavaDex api;
  private static ScheduledExecutorService scheduler;

  public static void addToUpdateList(Manga manga) {
    updateList.put(manga, manga.getLatestUploadedChapterId());
  }

  public static void startChecker(JavaDex javadex){
    api = javadex;
    scheduler = Executors.newScheduledThreadPool(1);
    long refreshRate = api.getRefreshRate().getSeconds();

    Runnable task = () -> {
      for (Map.Entry<Manga, UUID> entry : updateList.entrySet()) {
        try {
          Manga manga = entry.getKey();
          UUID id = entry.getValue();

          Manga newUpdated = api.getMangaById(manga.getId()).get();

          if (!newUpdated.getLatestUploadedChapterId().toString().equals(id.toString())) {
            updateList.put(newUpdated, newUpdated.getLatestUploadedChapterId());
            notifyNewChapterListeners(newUpdated.getLatestUploadedChapterId());
          }

        } catch (InterruptedException | ExecutionException e) {
          LOGGER.log(Level.SEVERE, "Error checking for new chapters", e);
        }
      }
    };
    scheduler.scheduleAtFixedRate(task, 0, refreshRate, TimeUnit.SECONDS);
  }

  private static void notifyNewChapterListeners(UUID newChapterId){
    api.getChapterById(newChapterId)
    .handle((newChapter, ex) -> {
        if (newChapter == null) {
            LOGGER.log(Level.SEVERE, "Error getting chapter", ex);
            return null;
        } else {
            api.getListeners().forEach(listener -> listener.onNewChapterEvent(
                new NewChapterEvent(
                    api, 
                    OffsetDateTime.now(),
                    newChapter
                )
            ));
            return null;
        }
    });
}
}
