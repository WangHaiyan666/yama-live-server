package com.newverse.yama.live.domain.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * 配置google登陆所需要的信息
 *
 * @author he.qiming
 */
@Data
@Validated
@Configuration
@ConfigurationProperties("idp.google")
public class GoogleApiProperties {

    private String iosClientId;
    private String androidClientId;
    private String webClientId;
}
