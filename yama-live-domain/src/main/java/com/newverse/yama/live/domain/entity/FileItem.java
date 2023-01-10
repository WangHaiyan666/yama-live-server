package com.newverse.yama.live.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

/**
 * @author liangyu
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
@TableName(value = "file_item")
public class FileItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String userSubscriber;

    private String fileKey;

    private Long fileSize;

    private String fileType;

    private String fileTitle;

    private String signedUrl;

    private Integer uploadState;

    private Date createTime;

    private Date modifyTime;

}
