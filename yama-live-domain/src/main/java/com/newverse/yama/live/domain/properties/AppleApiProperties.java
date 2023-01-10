package com.newverse.yama.live.domain.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * @author liangyu
 */
@Data
@Validated
@Configuration
@ConfigurationProperties("idp.apple")
public class AppleApiProperties {

    public String iosClientId;

    public String androidClientId;

    public String webClientId;
}
