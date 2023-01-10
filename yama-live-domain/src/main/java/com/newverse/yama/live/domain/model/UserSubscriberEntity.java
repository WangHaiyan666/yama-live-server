package com.newverse.yama.live.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyu
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSubscriberEntity {

  private String token;

  private String sub;

  private String email;

  private Integer emailVerified;

  private String locale = "";
}
