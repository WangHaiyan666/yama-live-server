package com.newverse.yama.live.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.newverse.yama.live.domain.entity.Admin;
import com.newverse.yama.live.domain.model.AdminRequest;
import com.newverse.yama.live.domain.model.LoginAdmin;
import com.newverse.yama.live.domain.model.R;
import org.springframework.web.context.request.NativeWebRequest;


public interface AdminService extends IService<Admin> {

    R verityPasswd(AdminRequest adminRequest);

    LoginAdmin checkAuth(String jwtToken);

    LoginAdmin checkAuth(NativeWebRequest request);
}
