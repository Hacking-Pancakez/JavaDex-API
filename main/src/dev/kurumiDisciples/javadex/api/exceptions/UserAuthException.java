package dev.kurumiDisciples.javadex.api.exceptions;

import javax.json.JsonObject;

public class UserAuthException extends ErrorException {


  public UserAuthException(JsonObject object) {
    super(object);
  }
}
