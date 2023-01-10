package com.newverse.yama.live.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * PreSignUploadFileResponse
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-10-22T19:47:21.553856+08:00[Asia/Shanghai]")
public class PreSignUploadFileResponse {
  @JsonProperty("fileKey")
  private String fileKey;

  @JsonProperty("fileName")
  private String fileName;

  @JsonProperty("fileType")
  private String fileType;

  @JsonProperty("fileSize")
  private Long fileSize;

  @JsonProperty("preSignedUrl")
  private String preSignedUrl;

  public PreSignUploadFileResponse fileKey(String fileKey) {
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

  public PreSignUploadFileResponse fileName(String fileName) {
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

  public PreSignUploadFileResponse fileType(String fileType) {
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

  public PreSignUploadFileResponse fileSize(Long fileSize) {
    this.fileSize = fileSize;
    return this;
  }

  /**
   * file size of upload file.
   * @return fileSize
  */
  @ApiModelProperty(value = "file size of upload file.")


  public Long getFileSize() {
    return fileSize;
  }

  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }

  public PreSignUploadFileResponse preSignedUrl(String preSignedUrl) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PreSignUploadFileResponse preSignUploadFileResponse = (PreSignUploadFileResponse) o;
    return Objects.equals(this.fileKey, preSignUploadFileResponse.fileKey) &&
        Objects.equals(this.fileName, preSignUploadFileResponse.fileName) &&
        Objects.equals(this.fileType, preSignUploadFileResponse.fileType) &&
        Objects.equals(this.fileSize, preSignUploadFileResponse.fileSize) &&
        Objects.equals(this.preSignedUrl, preSignUploadFileResponse.preSignedUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileKey, fileName, fileType, fileSize, preSignedUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PreSignUploadFileResponse {\n");
    
    sb.append("    fileKey: ").append(toIndentedString(fileKey)).append("\n");
    sb.append("    fileName: ").append(toIndentedString(fileName)).append("\n");
    sb.append("    fileType: ").append(toIndentedString(fileType)).append("\n");
    sb.append("    fileSize: ").append(toIndentedString(fileSize)).append("\n");
    sb.append("    preSignedUrl: ").append(toIndentedString(preSignedUrl)).append("\n");
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

