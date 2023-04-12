package dev.kurumiDisciples.javadex.api.exceptions;

import javax.json.*;
import javax.json.stream.*;

import dev.kurumiDisciples.javadex.api.entities.ISnowflake;

import java.time.OffsetDateTime;

/*will add individual errors later but for now this will handle all of them*/

public class ErrorException extends Exception implements ISnowflake{

  private String context;
  private int status;
  private String id;
  private String title;
  private String detail;
  


  public ErrorException(JsonObject object) {
    super(object.getJsonArray("errors").getJsonObject(0).getString("detail"));

    this.id = object.getJsonArray("errors").getJsonObject(0).getString("id");
    this.title = object.getJsonArray("errors").getJsonObject(0).getString("title");
    this.detail = object.getJsonArray("errors").getJsonObject(0).getString("detail");
    this.status = object.getJsonArray("errors").getJsonObject(0).getInt("status");
    this.context = object.getJsonArray("errors").getJsonObject(0).getString("context", "empty");
  }
  
  public String getContext() {
    return context;
  }

  @Override
  public String getId() {
    return id;
  }
  
  
  public String getTitle() {
    return title;
  }

  public String getDetail() {
    return detail;
  }
  
  public int getStatus() {
    return status;
  }

  @Override
  public OffsetDateTime getUpdatedAt(){
    return null;
  }

  @Override
  public OffsetDateTime getCreatedAt(){
    return null;
  }
}