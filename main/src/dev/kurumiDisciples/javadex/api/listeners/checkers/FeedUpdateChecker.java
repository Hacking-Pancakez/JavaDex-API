package dev.kurumiDisciples.javadex.api.listeners.checkers;

import dev.kurumiDisciples.javadex.api.JavaDex;
import dev.kurumiDisciples.javadex.api.manga.Manga;
import dev.kurumiDisciples.javadex.api.events.FeedUpdateEvent;
import dev.kurumiDisciples.javadex.api.entities.Chapter;
import dev.kurumiDisciples.javadex.api.listeners.ListenerImpl;

import java.util.*;
import java.util.concurrent.*;
import java.time.OffsetDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FeedUpdateChecker {
  private static final Logger LOGGER = Logger.getLogger(FeedUpdateChecker.class.getName());
  private static ScheduledExecutorService scheduler;
  private static final Map<JavaDex, List<Chapter>> updateList = new HashMap<>();

  public static void addToUpdateList(JavaDex javadex) {
    updateList.put(javadex, javadex.requestMyFeed().retrieve());
  }

  public static void startChecker(JavaDex javadex) {
    scheduler = Executors.newScheduledThreadPool(1);
    long refreshRate = javadex.getRefreshRate().getSeconds();

    Runnable task = () -> {
      for (Map.Entry<JavaDex, List<Chapter>> entry : updateList.entrySet()) {
        JavaDex api = entry.getKey();
        List<Chapter> chapters = entry.getValue();
        
        List<Chapter> updatedFeed = api.requestMyFeed().setLimit(100).retrieve();

        if (updatedFeed.get(0).getId().equals(chapters.get(0).getId())) {
          updateList.remove(api);
          updateList.put(api, updatedFeed);
          notifyFeedUpdateEvents(api, updatedFeed);
        }
      }
    };

    scheduler.scheduleAtFixedRate(task, 0, refreshRate, TimeUnit.SECONDS);
  }

  private static void notifyFeedUpdateEvents(JavaDex api, List<Chapter> feed){
    List<ListenerImpl> listeners = api.getListeners();
    FeedUpdateEvent event = new FeedUpdateEvent(api, feed, OffsetDateTime.now());

    for (ListenerImpl listener : listeners){
      listener.onFeedUpdateEvent(event);
    }
  }
}
