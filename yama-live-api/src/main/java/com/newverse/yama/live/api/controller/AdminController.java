package com.newverse.yama.live.api.controller;

import com.newverse.yama.live.domain.model.AdminRequest;
import com.newverse.yama.live.domain.model.R;
import com.newverse.yama.live.domain.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台管理的相关事宜
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "企业端登录")
public class AdminController {
    @Autowired
    private AdminService adminService;

    // 判断是否登录成功
    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public R loginStatus(@RequestBody AdminRequest adminRequest) {
        return adminService.verityPasswd(adminRequest);
    }
}
