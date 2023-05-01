package dev.kurumiDisciples.javadex.api;

import dev.kurumiDisciples.javadex.api.requests.*;
import dev.kurumiDisciples.javadex.api.manga.*;

import java.util.UUID;

import javax.json.*;
import javax.json.stream.*;

import dev.kurumiDisciples.javadex.api.entities.Chapter;

import dev.kurumiDisciples.javadex.api.requests.utils.GetAction;

import java.util.concurrent.*;
/*Represents a logged in session of the MangaDex API*/
public class JavaDex {


  protected String refreshToken;
  protected String sessionToken;
  protected String expire;
  protected boolean loggedIn = false;

  private final ExecutorService executor = Executors.newFixedThreadPool(10);

  private ScheduledExecutorService scheduler;

  protected JavaDex(String[] tokens){
    this.refreshToken = tokens[2];
    this.sessionToken = tokens[0];
    this.expire = tokens[1];
    startScheduler();
    loggedIn = true;
  }

  protected JavaDex(){
    this.refreshToken = null;
    this.sessionToken = null;
    this.expire = null;
  }

  private void startScheduler() {
        scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = new Runnable() {
            @Override
            public void run() {
              try{
                String[] tokens = BuildAction.refreshTokens(refreshToken);
                sessionToken = tokens[0];
                refreshToken = tokens[1];
              }
              catch (Exception e){
                System.out.println("Error while refreshing the tokens");
                e.printStackTrace();
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

  public Chapter getChapterById(UUID id){
    GetAction action = new GetAction("https://api.mangadex.org/chapter/" + id.toString());

    try{
    return new Chapter(action.execute());
      }
    catch (Exception e){
      return null;
    }
  }  

  public boolean isLoggedIn(){
    return loggedIn;
  }


  private JsonObject getHeader(){
    JsonObjectBuilder header = Json.createObjectBuilder();
    header.add("Authorization", "Bearer " + sessionToken);
    return header.build();
  }
}