package dev.kurumiDisciples.javadex.api;

import dev.kurumiDisciples.javadex.api.requests.*;
import dev.kurumiDisciples.javadex.api.manga.*;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.time.Duration;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.*;
import javax.json.stream.*;

import dev.kurumiDisciples.javadex.api.entities.Chapter;

import dev.kurumiDisciples.javadex.api.requests.utils.GetAction;

import dev.kurumiDisciples.javadex.api.listeners.*;
import dev.kurumiDisciples.javadex.api.listeners.checkers.*;

import java.util.concurrent.*;
/*Represents a logged in session of the MangaDex API*/
public class JavaDex {


  protected String refreshToken;
  protected String sessionToken;
  protected String expire;
  protected boolean loggedIn = false;
  private Duration refreshRate; 

  private static final Logger LOGGER = Logger.getLogger(JavaDex.class.getName());

  private List<ListenerImpl> listeners = new ArrayList<>();

  private List<Manga> checkManga = new ArrayList<>();

  private final ExecutorService executor = Executors.newFixedThreadPool(10);

  private ScheduledExecutorService scheduler;


  protected JavaDex(String[] tokens, Duration refreshTime){
    this.refreshToken = tokens[2];
    this.sessionToken = tokens[0];
    this.expire = tokens[1];
    startScheduler();
    loggedIn = true;
    refreshRate = refreshTime;
    startCheckers();
  }

  protected JavaDex(Duration refreshTime){
    this.refreshToken = null;
    this.sessionToken = null;
    this.expire = null;
    refreshRate = refreshTime;
    startCheckers();
  }

  private void startScheduler() {
        scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    String[] tokens = BuildAction.refreshTokens(refreshToken);
                    sessionToken = tokens[0];
                    refreshToken = tokens[1];
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error while refreshing login tokens", e);
                }
            }
        };

        // Schedule the task to run every 15 minutes
        scheduler.scheduleAtFixedRate(task, 0, 15, TimeUnit.MINUTES);
    }

  public GroupAction findGroup(String name){
    return new GroupAction().setName(name);
  }

  public GroupAction findGroup(){
    return new GroupAction();
  }

  public SearchAction findManga(String name){
    return new SearchAction().setQuery(name);  
  }

  public SearchAction findManga(){
    return new SearchAction("");
  }

  public CompletableFuture<Manga> getMangaById(String id){
    return SearchAction.getMangaById(id);
  }


  public FeedAction requestMyFeed(){
   if (!isLoggedIn()) throw new UnsupportedOperationException("Current JavaDex object does not have a user associated with it.");
    
    return new FeedAction(getHeader());
  }

  public CompletableFuture<Chapter> getChapterById(UUID id) {
    return CompletableFuture.supplyAsync(() -> {
        GetAction action = new GetAction("https://api.mangadex.org/chapter/" + id.toString());

        try {
            return new Chapter(action.execute().getJsonObject("data"));
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    });
}

  public boolean isLoggedIn(){
    return loggedIn;
  }


  private JsonObject getHeader(){
    JsonObjectBuilder header = Json.createObjectBuilder();
    header.add("Authorization", "Bearer " + sessionToken);
    return header.build();
  }

  public JavaDex addEventListeners(ListenerImpl listener){
    listeners.add(listener);
    return this;
  }

  public JavaDex addEventListeners(List<ListenerImpl> listeners){
    this.listeners.addAll(listeners);
    return this;
  }

  public JavaDex addMangaToCheck(Manga manga){
    NewChapterChecker.addToUpdateList(manga);
    return this;
  }

  public Duration getRefreshRate(){
    return refreshRate;
  }

  public List<ListenerImpl> getListeners(){
    return listeners;
  }

  private void startCheckers(){
    NewChapterChecker.startChecker(this);
  }
}