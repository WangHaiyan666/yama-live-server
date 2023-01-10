package com.newverse.yama.live.domain.model;

import lombok.Data;

@Data
public class LoginAdmin {

    private Long id;

    private String name;

    private String token;

    //登录时间
    private Long LoginTime;

    //失效时间
    private Long expireTime;

    private String subscriber;
}
