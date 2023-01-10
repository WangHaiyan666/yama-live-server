package com.newverse.yama.live.domain.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.newverse.yama.live.domain.entity.UserInfo;
import com.newverse.yama.live.domain.enums.LoginAuthTypeEnum;
import com.newverse.yama.live.domain.model.LoginRequest;
import com.newverse.yama.live.domain.model.R;
import com.newverse.yama.live.domain.model.UserCacheInfo;
import org.springframework.web.context.request.NativeWebRequest;

public interface IAuthService extends IService<UserInfo> {

    R authLoginPost(LoginRequest loginRequest, OauthService oauthService, LoginAuthTypeEnum loginAuthTypeEnum);

    R authLogoutGet(NativeWebRequest request);

    UserCacheInfo checkAuth(NativeWebRequest request);

    UserCacheInfo checkAuth(String jwtToken);
}
