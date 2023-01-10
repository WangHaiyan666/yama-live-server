package com.newverse.yama.live.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 登录第三方认证类型
 *
 * @author 梁禹
 */
@AllArgsConstructor
public enum LoginAuthTypeEnum {

  /** apple */
  APPLE("apple"),

  /** google */
  GOOGLE("google"),

  /** firebase */
  FIREBASE("firebase");

  @Getter private final String authType;

  private static final Map<String, LoginAuthTypeEnum> MAP;

  static {
    MAP =
        Arrays.stream(LoginAuthTypeEnum.values())
            .collect(
                Collectors.toMap(LoginAuthTypeEnum::getAuthType, authTypeEnum -> authTypeEnum));
  }

  public static LoginAuthTypeEnum parse(String env) {
    return MAP.get(env);
  }
}
