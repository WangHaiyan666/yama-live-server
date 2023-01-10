package com.newverse.yama.live.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

@TableName(value = "admin")
@Data
public class Admin {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String password;

    private String subscriber;

    private String email;

    private int status;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
