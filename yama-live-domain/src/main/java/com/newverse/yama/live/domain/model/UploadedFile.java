package com.newverse.yama.live.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * uploaded file info
 */
@ApiModel(description = "uploaded file info")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-10-22T19:47:21.553856+08:00[Asia/Shanghai]")
public class UploadedFile {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("type")
  private String type;

  @JsonProperty("size")
  private Long size;

  @JsonProperty("url")
  private String url;

  @JsonProperty("signedUrl")
  private String signedUrl;

  @JsonProperty("status")
  private Integer status;

  @JsonProperty("timestamp")
  private Long timestamp;

  public UploadedFile id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * id of file
   * @return id
  */
  @ApiModelProperty(value = "id of file")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UploadedFile name(String name) {
    this.name = name;
    return this;
  }

  /**
   * name of file
   * @return name
  */
  @ApiModelProperty(value = "name of file")


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UploadedFile type(String type) {
    this.type = type;
    return this;
  }

  /**
   * type of file
   * @return type
  */
  @ApiModelProperty(value = "type of file")


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public UploadedFile size(Long size) {
    this.size = size;
    return this;
  }

  /**
   * size of file
   * @return size
  */
  @ApiModelProperty(value = "size of file")


  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public UploadedFile url(String url) {
    this.url = url;
    return this;
  }

  /**
   * url of file
   * @return url
  */
  @ApiModelProperty(value = "url of file")


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public UploadedFile signedUrl(String signedUrl) {
    this.signedUrl = signedUrl;
    return this;
  }

  /**
   * signedUrl of file
   * @return signedUrl
  */
  @ApiModelProperty(value = "signedUrl of file")


  public String getSignedUrl() {
    return signedUrl;
  }

  public void setSignedUrl(String signedUrl) {
    this.signedUrl = signedUrl;
  }

  public UploadedFile status(Integer status) {
    this.status = status;
    return this;
  }

  /**
   * file status(0 initial; 1 success; 2 fail; 3 timeout)
   * @return status
  */
  @ApiModelProperty(value = "file status(0 initial; 1 success; 2 fail; 3 timeout)")


  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public UploadedFile timestamp(Long timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * timestamp of the file
   * @return timestamp
  */
  @ApiModelProperty(value = "timestamp of the file")


  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UploadedFile uploadedFile = (UploadedFile) o;
    return Objects.equals(this.id, uploadedFile.id) &&
        Objects.equals(this.name, uploadedFile.name) &&
        Objects.equals(this.type, uploadedFile.type) &&
        Objects.equals(this.size, uploadedFile.size) &&
        Objects.equals(this.url, uploadedFile.url) &&
        Objects.equals(this.signedUrl, uploadedFile.signedUrl) &&
        Objects.equals(this.status, uploadedFile.status) &&
        Objects.equals(this.timestamp, uploadedFile.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, type, size, url, signedUrl, status, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UploadedFile {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    signedUrl: ").append(toIndentedString(signedUrl)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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

