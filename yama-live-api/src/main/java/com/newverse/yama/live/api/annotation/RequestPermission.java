package com.newverse.yama.live.api.annotation;

import java.lang.annotation.*;

/**
 * Created On : 7/12/2022.
 * <p>
 * Author : kaihe
 * <p>
 * Description: 请求token许可自定义注解，只要请求处理方法上加了此注解，就需要token鉴权
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestPermission {
}
