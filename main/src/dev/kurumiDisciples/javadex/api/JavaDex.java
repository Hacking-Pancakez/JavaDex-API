package dev.kurumiDisciples.javadex.api;

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
}
