package dev.kurumiDisciples.javadex.api;


import dev.kurumiDisciples.javadex.api.requests.BuildAction;

public class JavaDexBuilder {


  protected String username;
  protected String password;


  private JavaDexBuilder(String username, String password) {
    this.username = username;
    this.password = password;
  }

  private JavaDexBuilder(){
    this.username = null;
    this.password = null;
  }
  /*
  * creates a new instance of JavaDexBuilder
  * with the login creditials provided
  */
  public static JavaDexBuilder createDefault(String username, String password) {
    return new JavaDexBuilder(username, password);
  }

  public static JavaDexBuilder createGuest() {
    return new JavaDexBuilder();
      }

  public JavaDex build(){
    try{
    return new JavaDex(BuildAction.retrieveTokens(this));
    }
    catch (Exception e){
      System.out.println("Error while building the JavaDex");
      System.out.println("Building guest JavaDex");
    }
    return new JavaDex();
}

  public String getUsername() {
    return username;
  }
  public String getPassword() {
    return password;
  }
}