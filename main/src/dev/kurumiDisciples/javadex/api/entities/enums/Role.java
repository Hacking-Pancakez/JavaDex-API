package dev.kurumiDisciples.javadex.api.entities.enums;


public enum Role {

  GROUP_MEMBER("ROLE_GROUP_MEMBER"),
  GROUP_OWNER("ROLE_GROUP_LEADER"),
  MEMBER("ROLE_MEMBER"),
  MD_AT_HOME("ROLE_MD_AT_HOME"),
  GLOBAL_MODERATOR("ROLE_GLOBAL_MODERATOR"),
  POWER_UPLOADER("ROLE_POWER_UPLOADER"),
  STAFF("ROLE_STAFF"),
  FORUM_MODERATOR("ROLE_FORUM_MODERATOR");


  private final String id;
  
  Role(String id) {
    this.id = id;
  }

  public static Role getRole(String id) {
    return Role.valueOf(id);
  }


}