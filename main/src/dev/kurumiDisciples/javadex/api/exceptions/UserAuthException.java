package dev.kurumiDisciples.javadex.api.exceptions;

import javax.json.JsonObject;

import dev.kurumiDisciples.javadex.api.JavaDex;

public class UserAuthException extends ErrorException {

  private JavaDex object;

  public UserAuthException(JsonObject object, JavaDex affectedObject) {
    super(object);
    this.object = affectedObject;
  }


  public JavaDex getJavaDex(){
    return object;
  }
}
