package com.newverse.yama.live.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liangyu
 */
@Data
@TableName("user_info")
@ApiModel(value = "用户信息", description = "用户信息")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String userName;

    private String email;

    /**
     * email是否验证过，1为验证过，0为未验证
     */
    private Integer emailVerified;

    private String picture;

    private String locale;

    /**
     * 第三方提供的唯一用户键
     */
    private String subscriber;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;
    public static final String SUBSCRIBER = "subscriber";

    public static final String CREATE_TIME = "create_time";
    public static final String USER_NAME = "user_name";

    public static final String STATUS = "status";

}
