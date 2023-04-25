package dev.kurumiDisciples.javadex.api;

import dev.kurumiDisciples.javadex.api.requests.*;
import dev.kurumiDisciples.javadex.api.manga.*;


import java.util.concurrent.*;
/*Represents a logged in session of the MangaDex API*/
public class JavaDex {


  protected String refreshToken;
  protected String sessionToken;
  protected String expire;

  private ScheduledExecutorService scheduler;

  protected JavaDex(String[] tokens){
    this.refreshToken = tokens[2];
    this.sessionToken = tokens[0];
    this.expire = tokens[1];
    startScheduler();
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
}