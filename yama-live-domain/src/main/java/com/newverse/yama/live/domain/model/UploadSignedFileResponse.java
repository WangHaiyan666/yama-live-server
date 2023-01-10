package com.newverse.yama.live.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * UploadSignedFileResponse
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-10-22T19:47:21.553856+08:00[Asia/Shanghai]")
public class UploadSignedFileResponse {
  @JsonProperty("fileKey")
  private String fileKey;

  @JsonProperty("fileName")
  private String fileName;

  @JsonProperty("fileType")
  private String fileType;

  @JsonProperty("preSignedUrl")
  private String preSignedUrl;

  @JsonProperty("url")
  private String url;

  @JsonProperty("fileStatus")
  private Integer fileStatus;

  @JsonProperty("createTime")
  private Long createTime;

  public UploadSignedFileResponse fileKey(String fileKey) {
    this.fileKey = fileKey;
    return this;
  }

  /**
   * key of upload file(need to be unique,random key by hex).
   * @return fileKey
  */
  @ApiModelProperty(value = "key of upload file(need to be unique,random key by hex).")


  public String getFileKey() {
    return fileKey;
  }

  public void setFileKey(String fileKey) {
    this.fileKey = fileKey;
  }

  public UploadSignedFileResponse fileName(String fileName) {
    this.fileName = fileName;
    return this;
  }

  /**
   * key of upload file(need to be unique).
   * @return fileName
  */
  @ApiModelProperty(value = "key of upload file(need to be unique).")


  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public UploadSignedFileResponse fileType(String fileType) {
    this.fileType = fileType;
    return this;
  }

  /**
   * file type of upload file.
   * @return fileType
  */
  @ApiModelProperty(value = "file type of upload file.")


  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public UploadSignedFileResponse preSignedUrl(String preSignedUrl) {
    this.preSignedUrl = preSignedUrl;
    return this;
  }

  /**
   * preSigned url of upload file for fileKey.
   * @return preSignedUrl
  */
  @ApiModelProperty(value = "preSigned url of upload file for fileKey.")


  public String getPreSignedUrl() {
    return preSignedUrl;
  }

  public void setPreSignedUrl(String preSignedUrl) {
    this.preSignedUrl = preSignedUrl;
  }

  public UploadSignedFileResponse url(String url) {
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

  public UploadSignedFileResponse fileStatus(Integer fileStatus) {
    this.fileStatus = fileStatus;
    return this;
  }

  /**
   * status of upload file.
   * @return fileStatus
  */
  @ApiModelProperty(value = "status of upload file.")


  public Integer getFileStatus() {
    return fileStatus;
  }

  public void setFileStatus(Integer fileStatus) {
    this.fileStatus = fileStatus;
  }

  public UploadSignedFileResponse createTime(Long createTime) {
    this.createTime = createTime;
    return this;
  }

  /**
   * Time stamp of upload create time.
   * @return createTime
  */
  @ApiModelProperty(value = "Time stamp of upload create time.")


  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UploadSignedFileResponse uploadSignedFileResponse = (UploadSignedFileResponse) o;
    return Objects.equals(this.fileKey, uploadSignedFileResponse.fileKey) &&
        Objects.equals(this.fileName, uploadSignedFileResponse.fileName) &&
        Objects.equals(this.fileType, uploadSignedFileResponse.fileType) &&
        Objects.equals(this.preSignedUrl, uploadSignedFileResponse.preSignedUrl) &&
        Objects.equals(this.url, uploadSignedFileResponse.url) &&
        Objects.equals(this.fileStatus, uploadSignedFileResponse.fileStatus) &&
        Objects.equals(this.createTime, uploadSignedFileResponse.createTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileKey, fileName, fileType, preSignedUrl, url, fileStatus, createTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UploadSignedFileResponse {\n");
    
    sb.append("    fileKey: ").append(toIndentedString(fileKey)).append("\n");
    sb.append("    fileName: ").append(toIndentedString(fileName)).append("\n");
    sb.append("    fileType: ").append(toIndentedString(fileType)).append("\n");
    sb.append("    preSignedUrl: ").append(toIndentedString(preSignedUrl)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    fileStatus: ").append(toIndentedString(fileStatus)).append("\n");
    sb.append("    createTime: ").append(toIndentedString(createTime)).append("\n");
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

