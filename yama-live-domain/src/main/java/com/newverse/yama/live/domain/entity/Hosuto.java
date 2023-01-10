package com.newverse.yama.live.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Data
@Builder
//牛郎
public class Hosuto {
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
