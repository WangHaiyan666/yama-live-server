package com.newverse.yama.live.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * uploaded file list response
 */
@ApiModel(description = "uploaded file list response")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-10-22T19:47:21.553856+08:00[Asia/Shanghai]")
public class UploadedFileListResponse {
  @JsonProperty("uploadedFileList")
  @Valid
  private List<UploadedFile> uploadedFileList = null;

  @JsonProperty("page")
  private Integer page = 1;

  @JsonProperty("size")
  private Integer size = 10;

  @JsonProperty("total")
  private Integer total;

  public UploadedFileListResponse uploadedFileList(List<UploadedFile> uploadedFileList) {
    this.uploadedFileList = uploadedFileList;
    return this;
  }

  public UploadedFileListResponse addUploadedFileListItem(UploadedFile uploadedFileListItem) {
    if (this.uploadedFileList == null) {
      this.uploadedFileList = new ArrayList<UploadedFile>();
    }
    this.uploadedFileList.add(uploadedFileListItem);
    return this;
  }

  /**
   * uploaded file list.
   * @return uploadedFileList
  */
  @ApiModelProperty(value = "uploaded file list.")

  @Valid

  public List<UploadedFile> getUploadedFileList() {
    return uploadedFileList;
  }

  public void setUploadedFileList(List<UploadedFile> uploadedFileList) {
    this.uploadedFileList = uploadedFileList;
  }

  public UploadedFileListResponse page(Integer page) {
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

  public UploadedFileListResponse size(Integer size) {
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

  public UploadedFileListResponse total(Integer total) {
    this.total = total;
    return this;
  }

  /**
   * total count of uploaded file
   * @return total
  */
  @ApiModelProperty(value = "total count of uploaded file")


  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UploadedFileListResponse uploadedFileListResponse = (UploadedFileListResponse) o;
    return Objects.equals(this.uploadedFileList, uploadedFileListResponse.uploadedFileList) &&
        Objects.equals(this.page, uploadedFileListResponse.page) &&
        Objects.equals(this.size, uploadedFileListResponse.size) &&
        Objects.equals(this.total, uploadedFileListResponse.total);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uploadedFileList, page, size, total);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UploadedFileListResponse {\n");
    
    sb.append("    uploadedFileList: ").append(toIndentedString(uploadedFileList)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
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

