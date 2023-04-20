package dev.kurumiDisciples.javadex.api;

import dev.kurumiDisciples.javadex.api.requests.*;

public class JavaDex {


  protected String refreshToken;
  protected String sessionToken;
  protected String expire;

  protected JavaDex(String[] tokens){
    this.refreshToken = tokens[2];
    this.sessionToken = tokens[0];
    this.expire = tokens[1];
  }

  protected JavaDex(){
    this.refreshToken = null;
    this.sessionToken = null;
    this.expire = null;
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
