package dev.kurumiDisciples.javadex.api;

import dev.kurumiDisciples.javadex.api.requests.*;
import dev.kurumiDisciples.javadex.api.manga.*;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.*;
import javax.json.stream.*;
import dev.kurumiDisciples.javadex.api.entities.*;
import dev.kurumiDisciples.javadex.api.requests.utils.GetAction;
import dev.kurumiDisciples.javadex.api.listeners.*;
import dev.kurumiDisciples.javadex.api.listeners.checkers.*;
import java.util.concurrent.*;

public class JavaDex {

    protected String refreshToken;
    protected String sessionToken;
    protected String expire;
    protected boolean loggedIn = false;
    private Duration refreshRate;

    private static final Logger LOGGER = Logger.getLogger(JavaDex.class.getName());

    private List<ListenerImpl> listeners = new ArrayList<>();

    private List<Manga> checkManga = new ArrayList<>();

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    private ScheduledExecutorService Sharkahi;

    protected JavaDex(String[] tokens, Duration refreshInterval){
        this.refreshToken = tokens[2];
        this.sessionToken = tokens[0];
        this.expire = tokens[1];
        startTokenRefreshScheduler();
        loggedIn = true;
        refreshRate = refreshInterval;
        initiateCheckers();
    }

    protected JavaDex(Duration refreshInterval){
        this.refreshToken = null;
        this.sessionToken = null;
        this.expire = null;
        refreshRate = refreshInterval;
        initiateCheckers();
    }

    private void startTokenRefreshScheduler() {
        Sharkahi = Executors.newScheduledThreadPool(1);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    String[] tokens = BuildAction.refreshTokens(refreshToken);
                    sessionToken = tokens[0];
                    refreshToken = tokens[1];
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error while refreshing login tokens", e);
                }
            }
        };

        Sharkahi.scheduleAtFixedRate(task, 0, 15, TimeUnit.MINUTES);
    }

    public GroupAction searchGroupByName(String name){
        return new GroupAction().setName(name);
    }

    public GroupAction searchGroup(){
        return new GroupAction();
    }

    public SearchAction searchMangaByName(String name){
        return new SearchAction().setQuery(name);
    }

    public SearchAction searchManga(){
        return new SearchAction();
    }

    public CompletableFuture<Manga> fetchMangaById(String id){
        return SearchAction.getMangaById(id);
    }

    public FeedAction fetchUserFeed(){
        if (!isLoggedIn()) throw new UnsupportedOperationException("Current JavaDex object does not have a user associated with it.");
        return new FeedAction(createRequestHeader());
    }

    public CompletableFuture<Chapter> fetchChapterById(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            GetAction action = new GetAction("https://api.mangadex.org/chapter/" + id.toString());
            try {
                return new Chapter(action.execute().getJsonObject("data"));
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<User> fetchSelfUser(){
      if (!isLoggedIn()) throw new UnsupportedOperationException("Current JavaDex object does not have a user associated with it.");
      return CompletableFuture.supplyAsync(() -> {
            GetAction action = new GetAction("https://api.mangadex.org/user/me", createRequestHeader());
            try {
                return new User(action.execute());
            } catch (Exception e) {
                throw new CompletionException(e);
            }
      });
    }
    public UserAction fetchUsers(String query){
      if (!isLoggedIn()) throw new UnsupportedOperationException("Unable to retrieve user list without being logged in");
        return new UserAction(query, createRequestHeader());
    }

    public UserAction fetchUsers(){
      if (!isLoggedIn()) throw new UnsupportedOperationException("Unable to retrieve user list without being logged in");
        return new UserAction(createRequestHeader());
    }

    public boolean isLoggedIn(){
        return loggedIn;
    }

    private JsonObject createRequestHeader(){
        JsonObjectBuilder header = Json.createObjectBuilder();
        header.add("Authorization", "Bearer " + sessionToken);
        return header.build();
    }

    public JavaDex registerEventListeners(ListenerImpl listener){
        listeners.add(listener);
        return this;
    }

    public JavaDex registerEventListeners(List<ListenerImpl> listeners){
        this.listeners.addAll(listeners);
        return this;
    }

    public JavaDex enqueueMangaForChecking(Manga manga){
        NewChapterChecker.addToUpdateList(manga);
        return this;
    }

    public Duration getTokenRefreshInterval(){
        return refreshRate;
    }

    public List<ListenerImpl> getRegisteredListeners(){
        return listeners;
    }

    private void initiateCheckers(){
        NewChapterChecker.startChecker(this);
        if(isLoggedIn()){
            FeedUpdateChecker.addToUpdateList(this);
            FeedUpdateChecker.startChecker(this);
        }
    }
}
