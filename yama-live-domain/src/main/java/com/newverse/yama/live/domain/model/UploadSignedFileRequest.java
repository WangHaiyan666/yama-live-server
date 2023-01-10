package com.newverse.yama.live.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * UploadSignedFileRequest
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-10-22T19:47:21.553856+08:00[Asia/Shanghai]")
public class UploadSignedFileRequest {
  @JsonProperty("fileKey")
  private String fileKey;

  public UploadSignedFileRequest fileKey(String fileKey) {
    this.fileKey = fileKey;
    return this;
  }

  /**
   * key of upload file.
   * @return fileKey
  */
  @ApiModelProperty(value = "key of upload file.")


  public String getFileKey() {
    return fileKey;
  }

  public void setFileKey(String fileKey) {
    this.fileKey = fileKey;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UploadSignedFileRequest uploadSignedFileRequest = (UploadSignedFileRequest) o;
    return Objects.equals(this.fileKey, uploadSignedFileRequest.fileKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileKey);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UploadSignedFileRequest {\n");
    
    sb.append("    fileKey: ").append(toIndentedString(fileKey)).append("\n");
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

