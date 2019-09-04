package com.yequan.common.interceptor;

import com.yequan.common.annotation.Idempotent;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Auther: Administrator
 * @Date: 2019/8/31 8:49
 * @Description: 接口幂等拦截器
 */
public class IdempotentInterceptor extends BaseInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (!method.isAnnotationPresent(Idempotent.class)) {
                return true;
            }
            boolean success = tokenService.checkToken(request);
            if (!success) {
                renderMsg(response, AppResultBuilder.failure(ResultCode.INTERFACE_REQUEST_REPETITIVE));
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
