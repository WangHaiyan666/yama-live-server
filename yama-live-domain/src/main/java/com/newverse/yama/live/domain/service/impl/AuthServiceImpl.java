package com.newverse.yama.live.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newverse.yama.live.domain.entity.User;
import com.newverse.yama.live.domain.entity.UserInfo;
import com.newverse.yama.live.domain.enums.LoginAuthTypeEnum;
import com.newverse.yama.live.domain.enums.UserStatusEnum;
import com.newverse.yama.live.domain.exception.SystemException;
import com.newverse.yama.live.domain.model.*;
import com.newverse.yama.live.domain.respository.UserInfoMapper;
import com.newverse.yama.live.domain.service.IAuthService;
import com.newverse.yama.live.domain.service.OauthService;
import com.newverse.yama.live.domain.utils.BeanCopierUtil;
import com.newverse.yama.live.domain.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.NativeWebRequest;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Date;
import java.util.Random;

@Service
@Slf4j
public class AuthServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IAuthService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserInfoMapper userInfoMapper;

//    @Resource
//    private WalletServiceImpl walletService;

    private static final String JWT_KEY_FORMAT = "ID_TOKEN:%s";

    private static final String RANDOM_USER_FORMAT = "KJ_%08d";

    @Override
    public R authLoginPost(LoginRequest loginRequest, OauthService oauthService, LoginAuthTypeEnum loginAuthTypeEnum) {

        if (StringUtils.isBlank(loginRequest.getJwtToken())) {
            return R.error(HttpStatus.FORBIDDEN.getReasonPhrase());
        }

        UserInfo userInfo = registerUser(oauthService.verifyAndExchangeToken(loginRequest.getJwtToken()));

        if (userInfo == null) {
            return R.error(HttpStatus.FORBIDDEN.getReasonPhrase());
        }

        //??????????????????????????????FORBIDDEN
        if (userInfo.getStatus() == UserStatusEnum.BLOCKED.getState() || userInfo.getStatus() == UserStatusEnum.DELETE.getState()) {
            return R.error(HttpStatus.FORBIDDEN.getReasonPhrase());
        }

        User user = new User();
        BeanCopierUtil.copyWithNull(userInfo, user);
        user.setRegisterTime(userInfo.getCreateTime().getTime());
        return R.success("success", user);
    }

    @Override
    public R authLogoutGet(NativeWebRequest request) {
        UserCacheInfo userInfo = checkAuth(request);
        if (userInfo == null) {
            return R.error(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
        logout(request);
        return R.success("user logout success");
    }

    /**
     * ???request???header?????????jwt????????????????????????
     *
     * @param request ??????
     * @return null if not logged in
     */
    @Override
    public UserCacheInfo checkAuth(NativeWebRequest request) {
        String jwtToken = JwtUtil.parseJwtFromRequest(request);
        if (StringUtils.isEmpty(jwtToken)) {
            return null;
        }
        return getUserInfoFromCache(jwtToken);
    }

    @Override
    public UserCacheInfo checkAuth(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) {
            return null;
        }
        return getUserInfoFromCache(jwtToken);
    }


    /**
     * ?????????token????????????user
     *
     * @param idToken token
     * @return null if not exist in cache
     */
    private UserCacheInfo getUserInfoFromCache(String idToken) {
        try {
            String userCacheJson = stringRedisTemplate.opsForValue().get(String.format(JWT_KEY_FORMAT, idToken));
            if (StringUtils.isEmpty(userCacheJson)) {
                log.warn("idToken not found in cache {}", idToken);
                return null;
            }
            return new ObjectMapper().readValue(userCacheJson, UserCacheInfo.class);
        } catch (Exception ex) {
            throw new SystemException(ex);
        }
    }

    /**
     * ???request???header?????????jwt???????????????
     *
     * @param request ??????
     */
    public void logout(NativeWebRequest request) {
        String jwtToken = JwtUtil.parseJwtFromRequest(request);
        invalidUserInfoFromCache(jwtToken);
    }

    /**
     * ???????????????????????????
     *
     * @param idToken token
     */
    private void invalidUserInfoFromCache(String idToken) {
        String userInfoJson = stringRedisTemplate.opsForValue().getAndDelete(String.format(JWT_KEY_FORMAT, idToken));
        if (StringUtils.isEmpty(userInfoJson)) {
            log.warn("idToken not found in cache {}", idToken);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public UserInfo registerUser(UserSubscriberEntity subscriberEntity) {
        if (subscriberEntity == null) {
            return null;
        }
        UserInfo userInfo = getUserBySubscribe(subscriberEntity.getSub());
        // ?????????????????????????????????????????????
        if (userInfo == null) {

            userInfo = UserInfo.builder().subscriber(subscriberEntity.getSub()).email(subscriberEntity.getEmail()).locale(subscriberEntity.getLocale()).emailVerified(subscriberEntity.getEmailVerified()).status(UserStatusEnum.NORMAL.getState()).createTime(new Date()).modifyTime(new Date()).build();
            userInfo = createUser(userInfo);

//            walletService.rewordUser(subscriberEntity.getSub(), BillConstant.REWARD_BALANCE_FOR_NEW_USER);
        }

        // ?????????????????????????????????
        // ???redis?????????token
        cacheIdToken(subscriberEntity.getToken(), userInfo, Duration.ofMillis(OauthService.EXPIRE_TIME_SEC * 1000));

        return userInfo;
    }

    /**
     * ??????????????????userInfo
     *
     * @param subscribe ?????????
     * @return null if not exist
     */
    public UserInfo getUserBySubscribe(String subscribe) {
        if (StringUtils.isEmpty(subscribe)) {
            return null;
        }
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq(UserInfo.SUBSCRIBER, subscribe);

        return userInfoMapper.selectOne(userInfoQueryWrapper);
    }

    /**
     * ???????????????
     *
     * @param userInfo ?????????????????????
     * @return ??????????????????
     */
    public UserInfo createUser(UserInfo userInfo) {

        Random random = new Random();
        int profileIconId = random.nextInt(5) + 1;
        userInfo.setPicture(String.valueOf(profileIconId));

        //????????????????????? @TableId(type = IdType.AUTO)???????????????????????????tid
        int save = userInfoMapper.insert(userInfo);

        // ??????user???id????????????????????????username???Jira KP-50???
        userInfo.setUserName(String.format(RANDOM_USER_FORMAT, (long) save));
        return userInfo;
    }

    /**
     * ???????????????token???UserInfo??????redis??????
     *
     * @param idToken  jwt token
     * @param userInfo ????????????userInfo
     * @param ttl      ???????????????????????????jwt expire???????????????
     */
    public void cacheIdToken(String idToken, UserInfo userInfo, Duration ttl) {
        try {
            UserCacheInfo cacheInfo = BeanCopierUtil.copy(userInfo, UserCacheInfo.class);

            stringRedisTemplate.opsForValue().set(String.format(JWT_KEY_FORMAT, idToken), new ObjectMapper().writeValueAsString(cacheInfo), ttl);
        } catch (Exception ex) {
            throw new SystemException(ex);
        }
    }
}
