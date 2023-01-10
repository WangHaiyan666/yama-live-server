package com.newverse.yama.live.api.interceptor;

import com.newverse.yama.live.api.annotation.RequestPermission;
import com.newverse.yama.live.domain.model.LoginAdmin;
import com.newverse.yama.live.domain.model.R;
import com.newverse.yama.live.domain.model.UserCacheInfo;
import com.newverse.yama.live.domain.service.AdminService;
import com.newverse.yama.live.domain.service.IAuthService;
import com.newverse.yama.live.domain.utils.JwtUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import software.amazon.awssdk.core.util.json.JacksonUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class TokenPermissionInterceptor implements HandlerInterceptor {

    @Resource
    private IAuthService authService;

    @Resource
    private AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 判断是否需要校验请求token许可，只需要看目标请求处理方法上是否有自定义请求token许可注解-TokenPermission
        if (this.checkTargetMethodHasTokenPermission(handler)){

            // 从header中获取from_source
            String fromSource = JwtUtil.parseFromSourceFromHttpRequest(request);
            // 从header中获取token
            String token = JwtUtil.parseJwtFromHttpRequest(request);

            if(StringUtils.isNotEmpty(fromSource) && "company".equals(fromSource)){
                //企业端校验用户登录
                LoginAdmin loginAdmin = adminService.checkAuth(token);
                if (loginAdmin == null) {
                    this.returnTokenCheckJson(response, "902", "token参数为空，鉴权失败，请重新登录！");
                    // 权限校验失败，需要拦截请求
                    return false;
                }
            }else{
                UserCacheInfo userCacheInfo = authService.checkAuth(token);
                if (userCacheInfo == null) {
                    this.returnTokenCheckJson(response, "902", "token参数为空，鉴权失败，请重新登录！");
                    // 权限校验失败，需要拦截请求
                    return false;
                }
            }
        }

        // 不需要拦截，直接放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    /**
     * @author : kaihe
     * @date   : 2022/12/7
     * @param  : [handler]
     * @return : boolean
     * @description : 判断目标请求方法是否需要鉴权，是返回true,否发false
     */
    public boolean checkTargetMethodHasTokenPermission(Object handler){

        // 判断当前处理的handler是否已经映射到目标请求处理方法，看是不是HandlerMethod的实例对象
        if(handler instanceof HandlerMethod){
            // 强转为目标请求处理方法的实例对象，因为：HandlerMethod对象封装了目标请求处理方法的所有内容，包括方法所有的声明
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // 尝试获取目标请求处理方法上，是否添加了自定义请求token许可注解-TokenPermission，取到了就是加了，取不到就没加
            RequestPermission requestPermission = handlerMethod.getMethod().getAnnotation(RequestPermission.class);

            // 判断是否成功获取到请求token许可注解，如果没有获取到，不一定代表不需要进行权限校验，因为此注解还可能加载处理类，要再次尝试从请求处理方法所在处理类上获取该注解
            if(ObjectUtils.isEmpty(requestPermission)){
                requestPermission = handlerMethod.getMethod().getDeclaringClass().getAnnotation(RequestPermission.class);
            }

            // 最终判断是否需要进行请求token许可校验，如果获取到了，说明需要校验，否则直接放行
            return null != requestPermission;
        }

        // 请求不是需要进行鉴权操作，直接返回false
        return false;
    }

    /**
     * @author : zhukang
     * @date   : 2022/11/4
     * @param  : [response, returnCode, returnMsg]
     * @return : void
     * @description : 拦截器中，token鉴权失败的统一返回json处理
     */
    public void returnTokenCheckJson(HttpServletResponse response, String returnCode, String returnMsg){
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            response.getWriter().print(JacksonUtils.toJsonString((R.error(returnMsg))));
        } catch (IOException e) {
            log.warn("****** 请求token许可拦截器返回结果异常：{} ******", e.getMessage());
        }
    }

}
