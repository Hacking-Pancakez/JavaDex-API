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
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final Map<JavaDex, List<Chapter>> updateList = new ConcurrentHashMap<>();

    public static void addToUpdateList(JavaDex javadex) {
        updateList.put(javadex, javadex.fetchUserFeed().retrieve());
    }

    public static void startChecker(JavaDex javadex) {
        long refreshInterval = javadex.getTokenRefreshInterval().getSeconds();

        Runnable task = () -> {
            for (Map.Entry<JavaDex, List<Chapter>> entry : updateList.entrySet()) {
                JavaDex api = entry.getKey();
                List<Chapter> chapters = entry.getValue();

                List<Chapter> updatedFeed = api.fetchUserFeed().setLimit(10).retrieve();

                if (updatedFeed == null || updatedFeed.isEmpty()) {
                    LOGGER.log(Level.WARNING, "Updated feed is empty or null");
                    return;
                }

                if (!updatedFeed.get(0).getId().equals(chapters.get(0).getId())) {
                    updateList.put(api, updatedFeed);
                    notifyFeedUpdateEvents(api, updatedFeed);
                }
            }
        };

        scheduler.scheduleAtFixedRate(task, 0, refreshInterval, TimeUnit.SECONDS);
    }

    public static void notifyFeedUpdateEvents(JavaDex api, List<Chapter> feed){
        List<ListenerImpl> listeners = api.getRegisteredListeners();
        FeedUpdateEvent event = new FeedUpdateEvent(api, feed, OffsetDateTime.now());

        listeners.forEach(listener -> listener.onFeedUpdateEvent(event));
    }
}
