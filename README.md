
# JavaDex: Java MangaDex API

An unofficial wrapper for the MangaDex API. This wrapper is currently in development. For the most stable version always pull from the `master` branch. This repo is currently open to contribution, contact `hackingpancakez` on Discord for info.


## Disclaimer
By using this wrapper, you agree to MangaDex's privacy policy and terms of service.

The developer(s) of this wrapper are not responsible for misuse and/or other ill-advised activites that affect others.

From MangaDex's API page:
 ```
Usage of our services implies acceptance of the following:

You MUST credit us
You MUST credit scanlation groups if you offer the ability to read chapters
You CANNOT run ads or paid services on your website and/or apps 
```
## Retrieving Manga
To retrive a `Manga` object you can use the example below:
```java
public static void main(String[] args) throws Exception {
    
    JavaDex JD = JavaDexBuilder.createDefault(username, password).build();

    Manga titan = JD.findManga("Attack on Titan")
      .setLimit(1)
      .addTag(MangaTag.ACTION)
      .search().get(0);

    System.out.println(titan.getTitle());
  }
  ```
## Retrieve a Specific Chapter
The current method to retrieve a chapter is a bit complicated but will be refined later on.
```java
public static void main(String[] args) throws Exception{

    JavaDex JD = JavaDexBuilder.createDefault(username, password).build();

    Manga titan = JD.findManga("Attack On Titan")
      .setLimit(1)
      .addTag(MangaTag.ACTION)
      .search().get(0);

    CompletableFuture<Chapter> c = titan.retrieveChapterByNumber(TranslatedLanguage.ENGLISH, 1); // starts at 1

    System.out.println(c.get().getTitle());
}
```
## Downloading Pages
Downloading pages is a simple task with JavaDex.
1. First retrieve the manga you want.
```java 
JavaDex JD = JavaDexBuilder.createDefault(creditials, here).build();
Manga titan = JD.findManga("Attack on Titan").search().get(0);
```
2. Then retrieve a chapter
```java
Chapter chapter = titan.retrieveFeed().get(0);
```
3. Then retrieve the pages
```java 
List<PageProxy> pages = chapter.retrievePages();
```
4. Download to File
```java
pages.forEach(page -> {
    page.downloadToFile(new File("Page_"+page.getPageNumber()+".jpg"));
});
```

## Acknowledgements

 - [MangaDex API Reference Guide](https://api.mangadex.org/docs/swagger.html)
 - [MangaDex Discord Server](https://discord.gg/mangadex)
## Authors

- [@Hacking-Pancakez](https://github.com/Hacking-Pancakez)

