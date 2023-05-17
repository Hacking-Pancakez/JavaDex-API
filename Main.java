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
  //add logger 
  private static Logger logger = Logger.getLogger(Main.class.getName());
  public static void main(String[] args) throws Exception  {
    
  }

  @Override
  public void onNewChapterEvent(NewChapterEvent event) {
      System.out.println(event.getChapter().getTitle());
  }
}