package dev.kurumiDisciples.javadex.api;


import dev.kurumiDisciples.javadex.api.requests.BuildAction;

public class JavaDexBuilder {


  protected String username;
  protected String password;


  private JavaDexBuilder(String username, String password) {
    this.username = username;
    this.password = password;
  }
  /*
  * creates a new instance of JavaDexBuilder
  * with the login creditials provided
  */
  public static JavaDexBuilder createDefault(String username, String password) {
    return new JavaDexBuilder(username, password);
  }

  public JavaDex build(){
    return new JavaDex(BuildAction.retrieveTokens(this));
}

  public String getUsername() {
    return username;
  }
  public String getPassword() {
    return password;
  }
}