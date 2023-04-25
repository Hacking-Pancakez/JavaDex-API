package dev.kurumiDisciples.javadex.api.requests;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.json.Json;
import javax.json.JsonObject;

import java.io.*;
import java.net.*;

import dev.kurumiDisciples.javadex.api.JavaDexBuilder;

public class BuildAction {

/**

Retrieves session and refresh tokens from MangaDex API.
@param builder JavaDexBuilder object containing user credentials.
@return an array of String containing session token, expiration time, and refresh token.
@throws MalformedURLException if the URL provided is malformed.
@throws IOException if an I/O error occurs while opening the connection or reading the input stream.
@throws ProtocolException if an invalid HTTP method is used.
*/
  
  public static String[] retrieveTokens(JavaDexBuilder builder) throws MalformedURLException, IOException, ProtocolException {
    String base_url = "https://api.mangadex.org";

        // Credentials
        JsonObject creds = Json.createObjectBuilder()
                .add("username", builder.getUsername())
                .add("password", builder.getPassword())
                .build();

        // Login request
        URL url = new URL(base_url + "/auth/login");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(creds.toString().getBytes());
            os.flush();
        }

        // Send request and parse response
        String session_token, refresh_token;
        long expires;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            JsonObject json = Json.createReader(br).readObject();
            session_token = json.getJsonObject("token").getString("session");
            refresh_token = json.getJsonObject("token").getString("refresh");
            expires = Instant.now().plus(15, ChronoUnit.MINUTES).getEpochSecond();
        }
    
    String[] tokens = { session_token, Long.toString(expires), refresh_token };
    return tokens;
  }

  public static String[] refreshTokens(String refreshToken) throws Exception{
    String base_url = "https://api.mangadex.org";

    JsonObject creds = Json.createObjectBuilder()
      .add("token", refreshToken)
      .build();

    URL url = new URL(base_url + "/auth/refresh");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(creds.toString().getBytes());
            os.flush();
        }

    String session_token, refresh_token;
        long expires;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            JsonObject json = Json.createReader(br).readObject();
            session_token = json.getJsonObject("token").getString("session");
            refresh_token = json.getJsonObject("token").getString("refresh");
            expires = Instant.now().plus(15, ChronoUnit.MINUTES).getEpochSecond();
        }
    
    String[] tokens = { session_token, refresh_token };
    return tokens;
  }
}
