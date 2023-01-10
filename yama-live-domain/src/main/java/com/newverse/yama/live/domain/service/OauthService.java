package com.newverse.yama.live.domain.service;


import com.newverse.yama.live.domain.model.UserSubscriberEntity;

/**
 * 定义第三方登陆所需要的一些公共方法
 *
 * @author he.qiming
 */
public interface OauthService {

    /**
     * cache时间设置为30天
     */
    Long EXPIRE_TIME_SEC = 30 * 24 * 3600L;

    /**
     * 从客户端获取idToken（jwt），并向对应的idp校验token真实性，如果校验成功后对我方服务器进行映射和缓存
     *
     * @param idToken 第三方获取的jwtToken
     * @return UserSubscriberEntity, null if not verified
     */
    UserSubscriberEntity verifyAndExchangeToken(String idToken);
}
