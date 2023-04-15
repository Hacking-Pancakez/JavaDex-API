package dev.kurumiDisciples.javadex.api.entities;

import dev.kurumiDisciples.javadex.api.entities.Chapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;

import java.util.concurrent.CompletableFuture;

import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;

public class PageProxy {

  private String pageNumber;
  private Chapter chapter;
  private String url;

 public PageProxy(String pageNumber, Chapter chapter, String url) {
   this.pageNumber = pageNumber;
   this.chapter = chapter;
   this.url = url;
 }

  public String getPageNumber() {
    return pageNumber;
  }

  public Chapter getChapter() {
    return chapter;
  }

  public String getUrl() {
    return url;
  }

   public InputStream download() throws IOException {
    URL url = new URL(getUrl());
    URLConnection conn = url.openConnection();
    return conn.getInputStream();
  }


  public CompletableFuture<Path> downloadToPath(Path path) {
  CompletableFuture<Path> future = new CompletableFuture<>();

  try {
    InputStream in = download();
    Path filePath = path.resolve(pageNumber + ".jpg");
    Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
    future.complete(filePath);
  } catch (IOException e) {
    future.completeExceptionally(e);
  }

  return future;
}

  public CompletableFuture<File> downloadToFile(File file) {
  CompletableFuture<File> future = new CompletableFuture<>();

  try {
    InputStream in = download();
    Path filePath = file.toPath();
    Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
    future.complete(file);
  } catch (IOException e) {
    future.completeExceptionally(e);
  }

  return future;
}
}