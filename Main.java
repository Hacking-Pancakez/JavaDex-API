import dev.kurumiDisciples.javadex.api.requests.SearchAction;

import dev.kurumiDisciples.javadex.api.manga.Manga;
class Main {
  public static void main(String[] args) {
    Manga DUO = SearchAction.getMangaById("8e74e420-b05e-4975-9844-676c7156bd63");
    System.out.println(DUO.getTitle() + "\n" + DUO.