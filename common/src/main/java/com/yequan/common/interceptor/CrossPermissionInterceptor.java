package com.yequan.common.interceptor;

import com.yequan.common.annotation.CrossPermission;
import com.yequan.common.util.CurrentUserLocal;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Auther: yq
 * @Date: 2019/8/26 21:36
 * @Description: 横向越权拦截器
 */
public class CrossPermissionInterceptor extends BaseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            CrossPermission crossPermission = method.getAnnotation(CrossPermission.class);
            String key = crossPermission.key();
            Integer userId = CurrentUserLocal.getUserId();
            System.out.println(userId);
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
