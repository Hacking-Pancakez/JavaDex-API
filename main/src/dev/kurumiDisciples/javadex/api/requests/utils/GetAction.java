package dev.kurumiDisciples.javadex.api.requests.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class GetAction {
    private String url;
    private ExecutorService executor;
    private JsonObject headers;
    private JsonObject params;
    
    public GetAction(String url, ExecutorService executor, JsonObject headers, JsonObject params) {
        this.url = url;
        this.executor = executor;
        this.headers = headers;
        this.params = params;
    }

  public GetAction(String url, JsonObject headers, JsonObject params){
    this.url = url;
    this.headers = headers;
    this.params = params;
    this.executor = Executors.newFixedThreadPool(10);
  }
    
    public CompletableFuture<JsonObject> executeAsync() {
        CompletableFuture<JsonObject> future = new CompletableFuture<>();
        executor.execute(() -> {
            try {
                JsonObject result = execute();
                future.complete(result);
            } catch (IOException ex) {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }
    
    public JsonObject execute() throws IOException {
        String queryString = buildQueryString(params);
        URL url = new URL(this.url + queryString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        for (String key : headers.keySet()) {
            connection.setRequestProperty(key, headers.getString(key));
        }
        
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            JsonReader jsonReader = Json.createReader(connection.getInputStream());
            JsonObject jsonObject = jsonReader.readObject();
            jsonReader.close();
            return jsonObject;
        } else {
            throw new IOException("GET request failed with response code " + responseCode);
        }
    }
    
    private String buildQueryString(JsonObject params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        
        for (String key : params.keySet()) {
            sb.append(key).append("=").append(params.getString(key)).append("&");
        }
        
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
