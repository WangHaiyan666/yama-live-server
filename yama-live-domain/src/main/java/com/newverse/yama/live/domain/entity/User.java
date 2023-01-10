package com.newverse.yama.live.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.newverse.yama.live.domain.spec.UserRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

/**
 * User
 */
@Data
public class User {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("userName")
  private String userName;

  @JsonProperty("email")
  private String email;

  @JsonProperty("emailVerified")
  private Integer emailVerified;

  @JsonProperty("picture")
  private String picture;

  @JsonProperty("locale")
  private String locale;

  @JsonProperty("subscriber")
  private String subscriber;

  @JsonProperty("registerTime")
  private Long registerTime;

  @JsonProperty("role")
  private UserRole role;

  public User id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * userId saved in system
   * @return id
  */
  @ApiModelProperty(value = "userId saved in system")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User userName(String userName) {
    this.userName = userName;
    return this;
  }

  /**
   * name of the user
   * @return userName
  */
  @ApiModelProperty(value = "name of the user")


  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public User email(String email) {
    this.email = email;
    return this;
  }

  /**
   * email address of the user
   * @return email
  */
  @ApiModelProperty(value = "email address of the user")


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public User emailVerified(Integer emailVerified) {
    this.emailVerified = emailVerified;
    return this;
  }

  /**
   * Is the email verified(1)
   * @return emailVerified
  */
  @ApiModelProperty(value = "Is the email verified(1)")


  public Integer getEmailVerified() {
    return emailVerified;
  }

  public void setEmailVerified(Integer emailVerified) {
    this.emailVerified = emailVerified;
  }

  public User picture(String picture) {
    this.picture = picture;
    return this;
  }

  /**
   * image of the user
   * @return picture
  */
  @ApiModelProperty(value = "image of the user")


  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public User locale(String locale) {
    this.locale = locale;
    return this;
  }

  /**
   * locale of the user
   * @return locale
  */
  @ApiModelProperty(value = "locale of the user")


  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public User subscriber(String subscriber) {
    this.subscriber = subscriber;
    return this;
  }

  /**
   * Unique key for the user
   * @return subscriber
  */
  @ApiModelProperty(value = "Unique key for the user")


  public String getSubscriber() {
    return subscriber;
  }

  public void setSubscriber(String subscriber) {
    this.subscriber = subscriber;
  }

  public User registerTime(Long registerTime) {
    this.registerTime = registerTime;
    return this;
  }

  /**
   * Time stamp of user created time.
   * @return registerTime
  */
  @ApiModelProperty(value = "Time stamp of user created time.")


  public Long getRegisterTime() {
    return registerTime;
  }

  public void setRegisterTime(Long registerTime) {
    this.registerTime = registerTime;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(this.id, user.id) &&
        Objects.equals(this.userName, user.userName) &&
        Objects.equals(this.email, user.email) &&
        Objects.equals(this.emailVerified, user.emailVerified) &&
        Objects.equals(this.picture, user.picture) &&
        Objects.equals(this.locale, user.locale) &&
        Objects.equals(this.subscriber, user.subscriber) &&
        Objects.equals(this.registerTime, user.registerTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userName, email, emailVerified, picture, locale, subscriber, registerTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    emailVerified: ").append(toIndentedString(emailVerified)).append("\n");
    sb.append("    picture: ").append(toIndentedString(picture)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    subscriber: ").append(toIndentedString(subscriber)).append("\n");
    sb.append("    registerTime: ").append(toIndentedString(registerTime)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

