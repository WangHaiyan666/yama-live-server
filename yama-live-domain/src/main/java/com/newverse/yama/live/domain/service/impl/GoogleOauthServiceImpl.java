package com.newverse.yama.live.domain.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.newverse.yama.live.domain.enums.UserEmailVerified;
import com.newverse.yama.live.domain.model.UserSubscriberEntity;
import com.newverse.yama.live.domain.properties.GoogleApiProperties;
import com.newverse.yama.live.domain.service.OauthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

/**
 * google验证
 *
 * @author he.qiming
 */
@Service
@Slf4j
public class GoogleOauthServiceImpl implements OauthService {

  @Resource private GoogleApiProperties googleApiProperties;

  /** 多个client公用一个verifier */
  private GoogleIdTokenVerifier verifier;

  @PostConstruct
  public void init() {
    verifier =
        new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
            // Specify the CLIENT_ID of the app that accesses the backend:
            .setAudience(
                Arrays.asList(
                    googleApiProperties.getWebClientId(),
                    googleApiProperties.getAndroidClientId(),
                    googleApiProperties.getIosClientId()))
            .build();
  }

  /**
   * 从客户端获取idToken（jwt），并向对应的idp校验token真实性，如果校验成功后对我方服务器进行映射和缓存
   *
   * @param idToken 第三方获取的jwtToken
   * @return userInfo, null if not verified
   */
  @Override
  public UserSubscriberEntity verifyAndExchangeToken(String idToken) {
    log.info("verifyAndExchangeToken start");
    GoogleIdToken googleIdToken = null;
    try {
      googleIdToken = verifier.verify(idToken);
    } catch (GeneralSecurityException | IOException | IllegalArgumentException e) {
      log.error("error while fetching google token , ", e);
    }
    // google 授权失败
    if (googleIdToken == null) {
      return null;
    }
    // 过期时间,用于判断ttl
    long expirationTime = googleIdToken.getPayload().getExpirationTimeSeconds() * 1000L;
    long currentTime = System.currentTimeMillis();
    // token无效，已过期
    if (expirationTime < currentTime) {
      return null;
    }
    long ttlMillis = expirationTime - currentTime;
    log.info("google auth token ttl:{}", ttlMillis);

    GoogleIdToken.Payload payload = googleIdToken.getPayload();
    // Get profile information from payload
    String email = payload.getEmail();
    boolean emailVerified = payload.getEmailVerified();
    String locale = (String) payload.get("locale");

    return UserSubscriberEntity.builder()
        .token(idToken)
        .sub(payload.getSubject())
        .email(email)
        .emailVerified(
            emailVerified
                ? UserEmailVerified.VERIFIED.getState()
                : UserEmailVerified.NOT_VERIFIED.getState())
        .locale(locale)
        .build();
  }
}
