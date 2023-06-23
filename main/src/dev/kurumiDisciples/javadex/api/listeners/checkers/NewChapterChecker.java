package dev.kurumiDisciples.javadex.api.listeners.checkers;

import dev.kurumiDisciples.javadex.api.JavaDex;
import dev.kurumiDisciples.javadex.api.manga.Manga;
import dev.kurumiDisciples.javadex.api.events.NewChapterEvent;
import dev.kurumiDisciples.javadex.api.entities.Chapter;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
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
    long refreshInterval = api.getTokenRefreshInterval().getSeconds();

    Runnable task = () -> {
        for (Map.Entry<Manga, UUID> entry : updateList.entrySet()) {
            try {
                Manga manga = entry.getKey();
                UUID id = entry.getValue();

                Manga updatedManga = api.fetchMangaById(manga.getId()).get();
                if (!updatedManga.getLatestUploadedChapterId().toString().equals(id.toString())) {
                    updateList.remove(manga);
                    updateList.put(updatedManga, updatedManga.getLatestUploadedChapterId());
                    notifyNewChapterListeners(updatedManga.getLatestUploadedChapterId());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve the interrupt
                LOGGER.log(Level.SEVERE, "Interrupted while checking for new chapters", e);
            } catch (ExecutionException e) {
                LOGGER.log(Level.SEVERE, "Error occurred while checking for new chapters", e);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Unexpected error occurred during task execution", e);
            }
        }
    };

    scheduler.scheduleAtFixedRate(task, 0, refreshInterval, TimeUnit.SECONDS);
}
  
    private static void notifyNewChapterListeners(UUID newChapterId){
        api.fetchChapterById(newChapterId)
            .handle((newChapter, ex) -> {
                if (newChapter == null) {
                    LOGGER.log(Level.SEVERE, "Error getting chapter", ex);
                    return null;
                } else {
                    api.getRegisteredListeners().forEach(listener -> listener.onNewChapterEvent(
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
