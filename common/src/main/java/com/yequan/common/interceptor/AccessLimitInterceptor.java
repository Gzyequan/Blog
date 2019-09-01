package com.yequan.common.interceptor;

import com.yequan.common.annotation.AccessLimit;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.service.RedisService;
import com.yequan.common.util.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Auther: yq
 * @Date: 2019/7/4 15:49
 * @Description: 限流拦截器:redis,注解,拦截器
 */
public class AccessLimitInterceptor extends BaseInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (!method.isAnnotationPresent(AccessLimit.class)) {
                return true;
            }
            AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
            int limit = accessLimit.limit();
            int sec = accessLimit.sec();

            String key = IPUtil.getIpAddress(request) + request.getRequestURI();
            Integer maxLimit = (Integer) redisService.get(key);
            if (null == maxLimit) {
                //表示这次请求前还没有发生过请求,记录一次请求,+1
                redisService.set(key, 1, sec);
            } else if (maxLimit < limit) {
                redisService.set(key, maxLimit + 1, sec);
            } else {
                renderMsg(response, AppResultBuilder.failure(ResultCode.INTERFACE_REQUEST_FREQUENT));
                return false;
            }
        }
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
