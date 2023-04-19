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
import javax.json.*;
import dev.kurumiDisciples.javadex.api.exceptions.ErrorException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.json.JsonObject;
import java.nio.charset.StandardCharsets;

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

  public GetAction(String url){
    this.url = url;
    this.headers = Json.createObjectBuilder().build();
    this.params = Json.createObjectBuilder().build();
    this.executor = Executors.newFixedThreadPool(10);
  }
    
    public CompletableFuture<JsonObject> executeAsync() {
        CompletableFuture<JsonObject> future = new CompletableFuture<>();
        executor.execute(() -> {
            try {
                JsonObject result = execute();
                future.complete(result);
            } catch (Exception ex) {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }
    
    public JsonObject execute() throws IOException, ErrorException {
        String queryString = jsonObjectToQueryString(params);
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
          if (isError(jsonObject)) throw new ErrorException(jsonObject);
            return jsonObject;
        } else {
            throw new IOException("GET request failed with response code " + responseCode);
        }
    }
    
    public static String jsonObjectToQueryString(JsonObject jsonObject) {
        StringBuilder queryString = new StringBuilder();

        for (String key : jsonObject.keySet()) {
            JsonValue value = jsonObject.get(key);

            if (value.getValueType() == JsonValue.ValueType.ARRAY) {
                JsonArray jsonArray = value.asJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    queryString.append(encodeUrlParameter(key))
                            .append("=")
                            .append(encodeUrlParameter(jsonArray.getString(i)))
                            .append("&");
                }
            } else {
                queryString.append(encodeUrlParameter(key))
                        .append("=")
                        .append(encodeUrlParameter(value.toString()))
                        .append("&");
            }
        }

        // Remove the last '&' character
        if (queryString.length() > 0) {
            queryString.setLength(queryString.length() - 1);
        }
        System.out.println(queryString.toString().replace("%22", "").replace("%5B", "[").replace("%5D", "]").replace("%25", "%"));
        return "?" + queryString.toString().replace("%22", "").replace("%5B", "[").replace("%5D", "]").replace("%25", "%");
    }

  private static String encodeUrlParameter(String parameter) {
        try {
            return URLEncoder.encode(parameter, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding is not supported", e);
        }
    }

private static String urlEncode(String value) {
    try {
        return URLEncoder.encode(value, "UTF-8");
    } catch (UnsupportedEncodingException e) {
        // This should never happen since UTF-8 is a standard encoding
        throw new RuntimeException(e);
    }
}

  private static boolean isError(JsonObject response){
    return response.getString("result").equals("error");
  }
}
