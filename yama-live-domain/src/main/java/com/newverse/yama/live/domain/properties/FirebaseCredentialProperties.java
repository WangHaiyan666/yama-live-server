package com.newverse.yama.live.domain.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author liangyu
 */
@Data
@Validated
@Configuration
@ConfigurationProperties("idp.firebase")
public class FirebaseCredentialProperties {

    @NotBlank
    public String credential;
}
