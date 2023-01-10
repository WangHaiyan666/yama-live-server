package com.newverse.yama.live.api.controller;

import com.newverse.yama.live.domain.enums.LoginAuthTypeEnum;
import com.newverse.yama.live.domain.model.LoginRequest;
import com.newverse.yama.live.domain.model.R;
import com.newverse.yama.live.domain.service.IAuthService;
import com.newverse.yama.live.domain.service.impl.AppleOauthServiceImpl;
import com.newverse.yama.live.domain.service.impl.FirebaseOauthServiceImpl;
import com.newverse.yama.live.domain.service.impl.GoogleOauthServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Api(tags = "用户登录功能")
public class AuthApiController {

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public AuthApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Resource
    private GoogleOauthServiceImpl googleOauthService;

    @Resource
    private AppleOauthServiceImpl appleOauthService;

    @Resource
    private FirebaseOauthServiceImpl firebaseOauthService;

    @Resource
    private IAuthService authService;

    @ApiOperation("普通登录")
    @PostMapping("/login")
    public R authLoginPost(@ApiParam(value = "") @Valid @RequestBody(required = false) LoginRequest loginRequest) {
        return authService.authLoginPost(loginRequest, firebaseOauthService, LoginAuthTypeEnum.FIREBASE);
    }

    @ApiOperation("Apple账号登录")
    @PostMapping("/apple/login")
    public R authAppleLoginPost(@ApiParam(value = "") @Valid @RequestBody(required = false) LoginRequest loginRequest) {
        return authService.authLoginPost(loginRequest, appleOauthService, LoginAuthTypeEnum.APPLE);
    }

    @ApiOperation("Google账号登录")
    @PostMapping("/google/login")
    public R authGoogleLoginPost(@ApiParam(value = "") @Valid @RequestBody(required = false) LoginRequest loginRequest) {
        return authService.authLoginPost(loginRequest, googleOauthService, LoginAuthTypeEnum.GOOGLE);
    }

    @ApiOperation(value = "账号退出", authorizations = {
            @Authorization(value = "bearerAuth")}, tags = {"auth"})
    @PostMapping("/logout")
    public R authLogoutGet() {
        return authService.authLogoutGet(request);
    }
}