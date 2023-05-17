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
import javax.json.*;
import javax.json.JsonReader;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;


import dev.kurumiDisciples.javadex.api.exceptions.ErrorException;
import java.nio.charset.StandardCharsets;

public class GetAction implements AutoCloseable {

    private static final int THREAD_POOL_SIZE = 10;

    private final String url;
    private final ExecutorService executor;
    private final JsonObject headers;
    private final JsonObject params;
    private HttpURLConnection connection;

    public GetAction(String url, ExecutorService executor, JsonObject headers, JsonObject params) {
        this.url = url;
        this.executor = executor;
        this.headers = headers;
        this.params = params;
    }

    public GetAction(String url, JsonObject headers, JsonObject params){
        this(url, Executors.newFixedThreadPool(THREAD_POOL_SIZE), headers, params);
    }

    public GetAction(String url){
        this(url, Json.createObjectBuilder().build(), Json.createObjectBuilder().build());
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
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        for (String key : headers.keySet()) {
            connection.setRequestProperty(key, headers.getString(key));
        }
        
        return handleResponse(connection);
    }
    
    private JsonObject handleResponse(HttpURLConnection connection) throws IOException, ErrorException {
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            JsonObject jsonObject = readResponse(connection);
            if (isError(jsonObject)) {
                throw new ErrorException(jsonObject);
            }
            return jsonObject;
        } else {
            throw new IOException("GET request failed with response code " + responseCode);
        }
    }

    private JsonObject readResponse(HttpURLConnection connection) throws IOException {
        try (JsonReader jsonReader = Json.createReader(connection.getInputStream())) {
            return jsonReader.readObject();
        }
    }

    private static boolean isError(JsonObject response) {
        return response.getString("result").equals("error");
    }

    public static String jsonObjectToQueryString(JsonObject jsonObject) {
        StringBuilder queryString = new StringBuilder();
        for (String key : jsonObject.keySet()) {
            JsonValue value = jsonObject.get(key);

            if (value.getValueType() == JsonValue.ValueType.ARRAY) {
                appendArrayParameters(queryString, key, value.asJsonArray());
            } else {
                appendParameter(queryString, key, value.toString());
            }
        }

        if (queryString.length() > 0) {
            queryString.setLength(queryString.length() - 1); // Remove the last '&' character
        }

        return "?" + queryString.toString().replace("%22", "").replace("%5B", "[").replace("%5D", "]").replace("%25", "%");
    }

    private static void appendArrayParameters(StringBuilder queryString, String key, JsonArray jsonArray) {
        for (int i = 0; i < jsonArray.size(); i++) {
        appendParameter(queryString, key, jsonArray.getString(i));
    }
}

private static void appendParameter(StringBuilder queryString, String key, String value) {
    queryString.append(encodeUrlParameter(key))
            .append("=")
            .append(encodeUrlParameter(value))
            .append("&");
}

private static String encodeUrlParameter(String parameter) {
    try {
        return URLEncoder.encode(parameter, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("UTF-8 encoding is not supported", e);
    }
}

@Override
public void close() throws IOException {
    if (connection != null) {
        connection.disconnect();
    }
}
}