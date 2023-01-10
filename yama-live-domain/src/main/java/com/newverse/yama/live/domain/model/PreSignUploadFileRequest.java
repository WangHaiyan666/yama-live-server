package com.newverse.yama.live.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * PreSignUploadFileRequest
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-10-22T19:47:21.553856+08:00[Asia/Shanghai]")
public class PreSignUploadFileRequest {
  @JsonProperty("fileKey")
  private String fileKey;

  @JsonProperty("fileName")
  private String fileName;

  @JsonProperty("fileType")
  private String fileType;

  @JsonProperty("fileSize")
  private Long fileSize;

  public PreSignUploadFileRequest fileKey(String fileKey) {
    this.fileKey = fileKey;
    return this;
  }

  /**
   * key of upload file(need to be unique,random key by hex,Content SHA256).
   * @return fileKey
  */
  @ApiModelProperty(value = "key of upload file(need to be unique,random key by hex,Content SHA256).")


  public String getFileKey() {
    return fileKey;
  }

  public void setFileKey(String fileKey) {
    this.fileKey = fileKey;
  }

  public PreSignUploadFileRequest fileName(String fileName) {
    this.fileName = fileName;
    return this;
  }

  /**
   * name of upload file
   * @return fileName
  */
  @ApiModelProperty(value = "name of upload file")


  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public PreSignUploadFileRequest fileType(String fileType) {
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

  public PreSignUploadFileRequest fileSize(Long fileSize) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PreSignUploadFileRequest preSignUploadFileRequest = (PreSignUploadFileRequest) o;
    return Objects.equals(this.fileKey, preSignUploadFileRequest.fileKey) &&
        Objects.equals(this.fileName, preSignUploadFileRequest.fileName) &&
        Objects.equals(this.fileType, preSignUploadFileRequest.fileType) &&
        Objects.equals(this.fileSize, preSignUploadFileRequest.fileSize);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileKey, fileName, fileType, fileSize);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PreSignUploadFileRequest {\n");
    
    sb.append("    fileKey: ").append(toIndentedString(fileKey)).append("\n");
    sb.append("    fileName: ").append(toIndentedString(fileName)).append("\n");
    sb.append("    fileType: ").append(toIndentedString(fileType)).append("\n");
    sb.append("    fileSize: ").append(toIndentedString(fileSize)).append("\n");
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

