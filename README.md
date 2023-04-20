
# JavaDex: Java MangaDex API

An unofficial wrapper for the MangaDex API. This wrapper is currently in development. For the most stable version always pull from the `master` branch. This repo is currently open to contribution, contact `HackingPancakez#0001` on Discord for info.


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
**Note: You don't always have to use the JavaDex object!!**

```java
public static void main(String[] args) throws Exception {
    
    Manga titan = new SearchAction("titan")
      .setLimit(1)
      .setOffset(1)
      .addTag(MangaTag.ACTION)
      .search()
      .get(0);

    System.out.println(titan.getTitle());
  }
  ```
## Acknowledgements

 - [MangaDex API Reference Guide](https://api.mangadex.org/docs/swagger.html)
 - [MangaDex Discord Server](https://discord.gg/mangadex)
## Authors

- [@Hacking-Pancakez](https://github.com/Hacking-Pancakez)

