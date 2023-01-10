package com.newverse.yama.live.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * UploadedFileListRequest
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-10-22T19:47:21.553856+08:00[Asia/Shanghai]")
public class UploadedFileListRequest {
  @JsonProperty("page")
  private Integer page = 1;

  @JsonProperty("size")
  private Integer size = 10;

  @JsonProperty("status")
  private Integer status;

  public UploadedFileListRequest page(Integer page) {
    this.page = page;
    return this;
  }

  /**
   * current page(start with 1)
   * @return page
  */
  @ApiModelProperty(value = "current page(start with 1)")


  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public UploadedFileListRequest size(Integer size) {
    this.size = size;
    return this;
  }

  /**
   * page size
   * @return size
  */
  @ApiModelProperty(value = "page size")


  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public UploadedFileListRequest status(Integer status) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UploadedFileListRequest uploadedFileListRequest = (UploadedFileListRequest) o;
    return Objects.equals(this.page, uploadedFileListRequest.page) &&
        Objects.equals(this.size, uploadedFileListRequest.size) &&
        Objects.equals(this.status, uploadedFileListRequest.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(page, size, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UploadedFileListRequest {\n");
    
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

