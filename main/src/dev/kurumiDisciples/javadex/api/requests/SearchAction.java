/**

* The SearchAction class provides methods for searching MangaDex API for manga titles and retrieving manga by ID.

* **Note: class we be remade**
*/
package dev.kurumiDisciples.javadex.api.requests;

import dev.kurumiDisciples.javadex.api.manga.Manga;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.json.*;
import javax.json.stream.*;
import java.util.Map;

import dev.kurumiDisciples.javadex.api.requests.utils.GetAction;
import dev.kurumiDisciples.javadex.api.manga.*;
import dev.kurumiDisciples.javadex.api.entities.enums.*;

import dev.kurumiDisciples.javadex.api.requests.utils.MangaQueryJsonConverter;
import dev.kurumiDisciples.javadex.api.exceptions.*;

import java.util.concurrent.CompletableFuture;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import java.util.ArrayList;

import java.util.concurrent.CompletionException;

public class SearchAction {

/**

Retrieves a Manga object for the specified ID from MangaDex API.
@param id a String representing the manga ID.
@return a Manga object containing information about the manga with the specified ID.
*/

  public static int DEFAULT_LIMIT = 100;
  public static int DEFAULT_OFFSET = 0;
  private static String API_BASE_URL = "https://api.mangadex.org/manga";

  
  private Integer limit;
  private Integer offset;
  private String title;
  private UUID authorOrArtist; 
  private List<UUID> authors = new ArrayList<>();
  private List<UUID> artists = new ArrayList<>();
  private Integer year;
  private List<MangaTag> includedTags = new ArrayList<>();
  private String includedTagsMode;
  private List<MangaTag> excludedTags = new ArrayList<>();
  private String excludedTagsMode;
  private Status status;
  private List<OriginalLanguage> originalLanguages = new ArrayList<>();
  private List<OriginalLanguage> excludedLanguages = new ArrayList<>();
  private List<TranslatedLanguage> availableTranslatedLanguages = new ArrayList<>();
  private Demographic demographic;
  private ContentRating contentRating;
  private Boolean hasAvailableChapters;


  public SearchAction(String query){
    this.title = formatString(query);
  }


  public SearchAction(String query, int limit, int offset){
    this.title = query;
    this.limit = limit;
    this.offset = offset;
  }

  public SearchAction setAuthorOrArtist(UUID authorOrArtist){
    this.authorOrArtist = authorOrArtist;
    return this;
  }

  public SearchAction addAuthor(UUID author){
    this.authors.add(author);
    return this;
  }

  public SearchAction addArtist(UUID artist){
    this.artists.add(artist);
    return this;
  }

  public SearchAction addAuthors(List<UUID> authors){
    this.authors.addAll(authors);
    return this;
  }

  public SearchAction addArtists(List<UUID> artists){
    this.artists.addAll(artists);
    return this;
  }

  public SearchAction setYear(int year){
    this.year = year;
    return this;
  }

  public SearchAction setIncludedTagsMode(String mode){
    this.includedTagsMode = mode;
    return this;
  }

  public SearchAction setExcludedTagsMode(String mode){
    this.excludedTagsMode = mode;
    return this;
  }

  public SearchAction addTag(MangaTag tag){
    this.includedTags.add(tag);
    return this;
  }
  
  public SearchAction addTags(List<MangaTag> tags){
    this.includedTags.addAll(tags);
    return this;
  }
  
  public SearchAction addExcludedTag(MangaTag tag){
    this.excludedTags.add(tag);
    return this;
  }
  
  public SearchAction addExcludedTags(List<MangaTag> tags){
    this.excludedTags.addAll(tags);
    return this;
  }

  public SearchAction setStatus(Status status){
    this.status = status;
    return this;
  }

  public SearchAction setDemographic(Demographic demographic){
    this.demographic = demographic;
    return this;
  }
  
  public SearchAction setContentRating(ContentRating contentRating){
    this.contentRating = contentRating;
    return this;
  }

  public SearchAction setAvailableChapters(boolean hasAvailableChapters){
    this.hasAvailableChapters = hasAvailableChapters;
    return this;
  }
  
  public SearchAction setOriginalLanguages(List<OriginalLanguage> originalLanguages){
    this.originalLanguages = originalLanguages;
    return this;
  }
  
  public SearchAction setAvailableTranslatedLanguages(List<TranslatedLanguage> availableTranslatedLanguages){
    this.availableTranslatedLanguages = availableTranslatedLanguages;
    return this;
  }

  public SearchAction setExcludedLanguages(List<OriginalLanguage> excludedLanguages){
    this.excludedLanguages = excludedLanguages;
    return this;
  }


  

  public Integer getLimit() {
    return limit;
  }

  public Integer getOffset() {
    return offset;
  }

  public String getQuery(){
    return title;
  }

  public UUID getAuthorOrArtist() {
    return authorOrArtist;
  }
  
  public List<UUID> getAuthors() {
    return authors;
  }
  
  public List<UUID> getArtists() {
    return artists;
  }
  
  public Integer getYear() {
    return year;
  }
  
  public String getIncludedTagsMode() {
    return includedTagsMode;
  }
  
  public String getExcludedTagsMode() {
    return excludedTagsMode;
  }

  public List<MangaTag> getIncludedTags() {
    return includedTags;
  }

  public List<MangaTag> getExcludedTags() {
    return excludedTags;
  }

  public Status getStatus() {
    return status;
  }

  public Demographic getDemographic() {
    return demographic;
  }

  public ContentRating getContentRating() {
    return contentRating;
  }

  public Boolean hasAvailableChapters() {
    return hasAvailableChapters;
  }
  
  public List<OriginalLanguage> getOriginalLanguages() {
    return originalLanguages;
  }

  public List<TranslatedLanguage> getAvailableTranslatedLanguages() {
    return availableTranslatedLanguages;
  }

  public List<OriginalLanguage> getExcludedLanguages() {
    return excludedLanguages;
  }

  public SearchAction setLimit(int limit) {
    this.limit = limit;
    return this;
  }
  
  public SearchAction setOffset(int offset) {
    this.offset = offset;
    return this;
  }

  public CompletableFuture<List<Manga>> submit() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        return search();
      } catch (ErrorException | IOException e) {
        throw new CompletionException(e);
      }
    });
}



  public List<Manga> search() throws ErrorException, IOException{
    
    JsonObject param = MangaQueryJsonConverter.toJson(this);
    
    GetAction get = new GetAction(API_BASE_URL, Json.createObjectBuilder().build(), param);

    JsonObject response = get.execute();

    JsonArray data = response.getJsonArray("data");

    List<Manga> mangaList = new ArrayList<>();
    
    for (int i = 0; i < data.size(); i++) {
        mangaList.add(new Manga(data.getJsonObject(i)));
      }
      return mangaList;
  }

  public String debug(){
    return MangaQueryJsonConverter.toJson(this).toString();
  }










  
  
  public static Manga getMangaById(String id) {
    try {
    GetAction getAction = new GetAction("https://api.mangadex.org/manga/" + id, Json.createObjectBuilder().build(), Json.createObjectBuilder().build());
    return new Manga(getAction.execute());
    }
    catch (Exception e) {
      System.out.println("Unable to retrieve Manga");
      e.printStackTrace();
    }
    return null;
  }

  /**

* Retrieves a list of Manga objects containing the specified name from MangaDex API.
* @param name a String representing the name of the manga.
* @return a List of Manga objects containing information about the manga(s) with the specified name.
*/
  public static List<Manga> getMangasByName(String name) {
    try {
      GetAction getAction = new GetAction("https://api.mangadex.org/manga?title=" + formatString(name));
      JsonObject jsonObject = getAction.execute();
      JsonArray jsonArray = jsonObject.getJsonArray("data");

      List<Manga> mangaList = new java.util.ArrayList<Manga>();

      for (int i = 0; i < jsonArray.size(); i++) {
        mangaList.add(new Manga(jsonArray.getJsonObject(i)));
      }
      return mangaList;
    }
    catch (Exception e) {
      System.out.println("Unable to retrieve Manga");
      e.printStackTrace();
    }
    return null;
  }

  private static String formatString(String input) {
    StringBuilder output = new StringBuilder();

    for (int i = 0; i < input.length(); i++) {
        if (input.charAt(i) == ' ') {
            output.append("%20");
        } else {
            output.append(input.charAt(i));
        }
    }

    return output.toString();
}
}