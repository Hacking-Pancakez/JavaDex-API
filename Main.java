import dev.kurumiDisciples.javadex.api.requests.*;

import dev.kurumiDisciples.javadex.api.manga.*;
import dev.kurumiDisciples.javadex.api.entities.*;

import dev.kurumiDisciples.javadex.api.listeners.*;  
import dev.kurumiDisciples.javadex.api.events.*;

import dev.kurumiDisciples.javadex.api.entities.enums.*;

import dev.kurumiDisciples.javadex.api.*;

import java.util.concurrent.*;

import java.util.List;
import java.util.UUID;
import java.time.Duration;


import java.util.logging.*;

import java.io.File;

public class Main extends ListenerImpl {
  private static Logger logger = Logger.getLogger(Main.class.getName());
  public static void main(String[] args) throws Exception  {
    JavaDex api = JavaDexBuilder.createGuest()
      .setEventRefresh(Duration.ofSeconds(10))
      .build();
    api.registerEventListeners(new Main());

    
    
   // api.addEventListeners(new Main());
    Manga DUO = api.searchMangaByName("Attack On Titan")
      .setLimit(1)
      .submit()
      .get()
      .get(0);
    
    api.enqueueMangaForChecking(DUO);
    System.out.println(DUO.retrieveChaptersIds(TranslatedLanguage.ENGLISH).get().size());
    System.out.println(DUO.getTitle());
    
  }

  @Override
  public void onNewChapterEvent(NewChapterEvent event) {
      System.out.println(event.getChapter().getTitle());
  }

  @Override
  public void onFeedUpdateEvent(FeedUpdateEvent event) {
   try{
    System.out.println(event.getFeed().get(0).getId());
    System.out.println(event.getFeed().get(0).getAssociatedManga().get().getTitle());
   }
    catch (Exception e){
      System.out.println("error");
    }
  }
}