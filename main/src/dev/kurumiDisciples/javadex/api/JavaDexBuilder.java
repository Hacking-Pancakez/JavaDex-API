package dev.kurumiDisciples.javadex.api;
import dev.kurumiDisciples.javadex.api.requests.BuildAction;

import java.time.Duration;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaDexBuilder {

  private static final Logger LOGGER = Logger.getLogger(JavaDexBuilder.class.getName());

  protected Optional<String> username;
  protected Optional<String> password;
  protected Duration refreshRate = Duration.ofMinutes(10);
  protected boolean feedListen = false;

  private JavaDexBuilder(Optional<String> username, Optional<String> password) {
    this.username = username;
    this.password = password;
  }

  public static JavaDexBuilder createDefault(String username, String password) {
    return new JavaDexBuilder(Optional.of(username), Optional.of(password));
  }

  public static JavaDexBuilder createGuest() {
    return new JavaDexBuilder(Optional.empty(), Optional.empty());
  }

  public JavaDex build() {
    try {
      return new JavaDex(BuildAction.retrieveTokens(this), refreshRate);
    } catch (Exception e) {
      // Check if username and password are empty
      if (username.isPresent() && password.isPresent()) {
        LOGGER.log(Level.WARNING, "Error while building the JavaDex", e);
      }
      LOGGER.info("Building guest JavaDex");
      return new JavaDex(refreshRate);
    }
  }

  public Optional<String> getUsername() {
    return username;
  }

  public Optional<String> getPassword() {
    return password;
  }

  public JavaDexBuilder setEventRefresh(Duration time) {
    this.refreshRate = time;
    return this;
  }

  public JavaDexBuilder shouldListenToFeedUpdate(boolean listen) {
    feedListen = listen;
    return this;
  }
}
