package com.yequan.user.interceptor;

import com.yequan.common.application.AppConstant;
import com.yequan.common.interceptor.BaseInterceptor;
import com.yequan.user.pojo.UserDO;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Auther: yq
 * @Date: 2019/7/5 11:00
 * @Description:
 */
public class LoginInterceptor extends BaseInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        //对登录,注册放行
        if (uri.indexOf("/login") > 0 || uri.indexOf("/register") > 0) {
            return true;
        }
        //获取session
        HttpSession session = request.getSession();
        UserDO user = (UserDO) session.getAttribute(AppConstant.SessionConstant.SESSION_USER_KEY);
        if (null != user) {
            return true;
        }
        renderPage(request, response, "/index.jsp");
        return false;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
