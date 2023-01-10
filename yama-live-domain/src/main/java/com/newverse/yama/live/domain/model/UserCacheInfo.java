package com.newverse.yama.live.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyu
 * @version 1.0
 * @since 2022/6/6 20:57
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCacheInfo {

    private Long id;

    private String userName;

    private String email;

    /**
     * email是否验证过，1为验证过，0为未验证
     */
    private Integer emailVerified;

    private String picture;

    private String subscriber;

    private Integer status;
}
