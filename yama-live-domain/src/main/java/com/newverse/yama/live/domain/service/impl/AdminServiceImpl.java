package com.newverse.yama.live.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newverse.yama.live.domain.constant.SecurityConstants;
import com.newverse.yama.live.domain.entity.Admin;
import com.newverse.yama.live.domain.exception.SystemException;
import com.newverse.yama.live.domain.model.AdminRequest;
import com.newverse.yama.live.domain.model.LoginAdmin;
import com.newverse.yama.live.domain.model.R;
import com.newverse.yama.live.domain.respository.AdminMapper;
import com.newverse.yama.live.domain.service.AdminService;
import com.newverse.yama.live.domain.utils.IdUtils;
import com.newverse.yama.live.domain.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = 720;

    private final static String ACCESS_TOKEN = "login_tokens:";

    private final static Long MILLIS_MINUTE_TEN = 120 * MILLIS_MINUTE;

    @Override
    public R verityPasswd(AdminRequest adminRequest) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",adminRequest.getUsername());
        queryWrapper.eq("password",adminRequest.getPassword());

        Admin admin = adminMapper.selectOne(queryWrapper);
        if (null != admin) {
            Map<String, Object> result = createToken(admin);
            return R.success("登录成功", result);
        } else {
            return R.error("用户名或密码错误");
        }
    }

    public Map<String, Object> createToken(Admin admin)
    {
        LoginAdmin loginAdmin = new LoginAdmin();
        String token = IdUtils.fastUUID();
        Long userId = admin.getId();
        String userName = admin.getName();
        loginAdmin.setToken(token);
        loginAdmin.setId(userId);
        loginAdmin.setName(userName);
        loginAdmin.setSubscriber(admin.getSubscriber());
        refreshToken(loginAdmin);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, userName);

        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<String, Object>();
        rspMap.put("access_token", JwtUtil.createToken(claimsMap));
        rspMap.put("expires_in", expireTime);
        return rspMap;
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginAdmin 登录信息
     */
    public void refreshToken(LoginAdmin loginAdmin)
    {
        loginAdmin.setLoginTime(System.currentTimeMillis());
        loginAdmin.setExpireTime(loginAdmin.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginAdmin.getToken());
        try {
            stringRedisTemplate.opsForValue().set(userKey, new ObjectMapper().writeValueAsString(loginAdmin), expireTime, TimeUnit.MINUTES);
        } catch (Exception ex) {
            throw new SystemException(ex);
        }
    }

    private String getTokenKey(String token)
    {
        return ACCESS_TOKEN + token;
    }


    @Override
    public LoginAdmin checkAuth(String jwtToken) {
       try {
           String userKey = JwtUtil.getUserKey(jwtToken);
           String loginAdminJson = stringRedisTemplate.opsForValue().get(getTokenKey(userKey));

           return new ObjectMapper().readValue(loginAdminJson, LoginAdmin.class);
       }catch (Exception ex){
           throw new SystemException(ex);
       }
    }

    public LoginAdmin checkAuth(NativeWebRequest request) {
        try {
            String token = JwtUtil.parseJwtFromRequest(request);
            String userKey = JwtUtil.getUserKey(token);
            String loginAdminJson = stringRedisTemplate.opsForValue().get(getTokenKey(userKey));

            return new ObjectMapper().readValue(loginAdminJson, LoginAdmin.class);
        }catch (Exception ex){
            throw new SystemException(ex);
        }
    }
}
