package com.newverse.yama.live.domain.service.impl;

import com.newverse.yama.live.domain.common.client.apple.AppleIdToken;
import com.newverse.yama.live.domain.common.client.apple.AppleIdTokenVerifier;
import com.newverse.yama.live.domain.enums.UserEmailVerified;
import com.newverse.yama.live.domain.model.UserSubscriberEntity;
import com.newverse.yama.live.domain.properties.AppleApiProperties;
import com.newverse.yama.live.domain.service.OauthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import static java.util.Arrays.asList;

/**
 * @author he.qiming
 */
@Service
@Slf4j
public class AppleOauthServiceImpl implements OauthService {

  @Resource private AppleApiProperties appleApiProperties;

  /** 多个client公用一个verifier */
  private AppleIdTokenVerifier verifier;

  @PostConstruct
  public void init() {
    verifier =
        new AppleIdTokenVerifier(
            asList(
                appleApiProperties.webClientId,
                appleApiProperties.androidClientId,
                appleApiProperties.iosClientId));
  }

  /**
   * 从客户端获取idToken（jwt），并向对应的idp校验token真实性，如果校验成功后对我方服务器进行映射和缓存
   *
   * @param idToken 第三方获取的jwtToken
   * @return UserSubscriberEntity, null if not verified
   */
  @Override
  public UserSubscriberEntity verifyAndExchangeToken(String idToken) {
    AppleIdToken claim;
    try {
      claim = verifier.verify(idToken);
    } catch (IllegalArgumentException e) {
      log.error("error while fetching apple token , ", e);
      return null;
    }
    // apple 授权失败
    if (claim == null) {
      log.error("apple授权失败");
      return null;
    }
    long expirationTime = claim.getPayload().getExpirationTimeSeconds() * 1000;
    long currentTime = System.currentTimeMillis();

    // token无效，已过期
    if (expirationTime < currentTime) {
      log.warn("token is expired {}", idToken);
      return null;
    }

    String email = claim.getEmail();
    boolean emailVerified = Boolean.parseBoolean(claim.getEmailVerified());
    String subscriber = claim.getPayload().getSubject();
    String locale = "";

    log.info("{} login success", subscriber);

    return UserSubscriberEntity.builder()
        .token(idToken)
        .sub(subscriber)
        .email(email)
        .emailVerified(
            emailVerified
                ? UserEmailVerified.VERIFIED.getState()
                : UserEmailVerified.NOT_VERIFIED.getState())
        .locale(locale)
        .build();
  }
}
