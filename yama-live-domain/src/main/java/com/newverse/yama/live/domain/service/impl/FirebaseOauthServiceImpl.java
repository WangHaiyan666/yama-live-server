package com.newverse.yama.live.domain.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.ArrayMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.newverse.yama.live.domain.enums.UserEmailVerified;
import com.newverse.yama.live.domain.model.UserSubscriberEntity;
import com.newverse.yama.live.domain.service.OauthService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author FluffyLiu
 */
@Slf4j
@Service
public class FirebaseOauthServiceImpl implements OauthService {

  private final ObjectMapper objectMapper;

  public FirebaseOauthServiceImpl() {
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public UserSubscriberEntity verifyAndExchangeToken(String idToken) {
    FirebaseToken firebaseToken = null;
    try {
      firebaseToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
    } catch (FirebaseAuthException e) {
      log.info("Firebase login failed", e);
    }

    if (firebaseToken == null) {
      return null;
    }
    val firebaseAuths =
        objectMapper.convertValue(firebaseToken.getClaims().get("firebase"), ArrayMap.class);
    val identities = objectMapper.convertValue(firebaseAuths.get("identities"), Map.class);
    val providerSubs =
        objectMapper.convertValue(
            identities.get(firebaseAuths.get("sign_in_provider")), List.class);

    val subOpt = providerSubs.stream().findFirst();
    if (subOpt.isEmpty()) {
      return null;
    }

    val sub = subOpt.get().toString();

    // 过期时间,用于判断ttl
    long expirationTime = Long.parseLong(firebaseToken.getClaims().get("exp").toString()) * 1000L;
    long currentTime = System.currentTimeMillis();

    // token无效，已过期
    if (expirationTime < currentTime) {
      return null;
    }

    long ttlMillis = expirationTime - currentTime;
    log.info(String.valueOf(ttlMillis));

    val email = firebaseToken.getEmail();
    val emailVerified = firebaseToken.isEmailVerified();
    val name = firebaseToken.getName();
    val locale = "";

    return UserSubscriberEntity.builder()
        .token(idToken)
        .sub(sub)
        .email(email)
        .emailVerified(emailVerified ? UserEmailVerified.VERIFIED.getState() : UserEmailVerified.NOT_VERIFIED.getState())
        .locale(locale)
        .build();
  }
}
