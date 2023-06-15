package dev.kurumiDisciples.javadex.api.entities;


import dev.kurumiDisciples.javadex.api.entities.ISnowflake;

import javax.json.*;
import javax.json.stream.*;

import java.time.OffsetDateTime;
import java.net.URL;

import java.util.UUID;

public class Author implements ISnowflake {
  private final String id;
  private final OffsetDateTime createdAt;
  private final OffsetDateTime updatedAt;
  private final String name;
  private final URL imageUrl;
  private final String twitter;
  private final String pixiv;
  private final String melonBox;
  private final String fanbox;
  private final String nicoVideo;
  private final String booth;
  private final String skeb;
  private final String fantia; 
  private final String tumblr;
  private final String youtube;
  private final String weibo;
  private final String naver;
  private final URL website;
  private final int version;


  public Author(JsonObject jsonObject) {
    JsonObject data = jsonObject.getJsonObject("data");
    this.id = data.getString("id");
    JsonObject attributes = data.getJsonObject("attributes");
    this.createdAt = OffsetDateTime.parse(attributes.getString("createdAt"));
    this.updatedAt = OffsetDateTime.parse(attributes.getString("updatedAt"));
    this.name = attributes.getString("name");
    this.imageUrl = createURL(attributes.getString("imageUrl", null));
    this.twitter = attributes.getString("twitter", null);
    this.pixiv = attributes.getString("pixiv", null);
    this.melonBox = attributes.getString("melonBox", null);
    this.fanbox = attributes.getString("fanbox", null);
    this.nicoVideo = attributes.getString("nicoVideo", null);
    this.booth = attributes.getString("booth", null);
    this.skeb = attributes.getString("skeb", null);
    this.fantia = attributes.getString("fantia", null);
    this.tumblr = attributes.getString("tumblr", null);
    this.youtube = attributes.getString("youtube", null);
    this.weibo = attributes.getString("weibo", null);
    this.naver = attributes.getString("naver", null);
    this.website = createURL(attributes.getString("website", null));
    this.version = attributes.getInt("version");
  }

  private URL createURL(String urlStr) {
        try {
            return new URL(urlStr);
        } catch (Exception e) {
            return null;
        }
    }
  
  public String getId() {
    return id;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public UUID getUUID(){
    return UUID.fromString(id);
  }

  public String getName() {
    return name;
  }

  public URL getImageUrl() {
    return imageUrl;
  }

  public String getTwitter() {
    return twitter;
  }
  
  public String getPixiv() {
    return pixiv;
  }
  
  public String getMelonBox() {
    return melonBox;
  }

  public String getFanbox() {
    return fanbox;
  }

  public String getNicoVideo() {
    return nicoVideo;
  }

  public String getBooth() {
    return booth;
  }

  public String getSkeb() {
    return skeb;
  }
  
  public String getFantia() {
    return fantia;
  }
  
  public String getTumblr() {
    return tumblr;
  }

  public String getYoutube() {
    return youtube;
  }

  public String getWeibo() {
    return weibo;
  }

  public String getNaver() {
    return naver;
  }
  
  public URL getWebsite() {
    return website;
  }

  public int getVersion() {
    return version;
  }
}