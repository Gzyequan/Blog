package com.yequan.common.interceptor;

import com.yequan.common.annotation.CrossPermission;
import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.util.CurrentUserLocal;
import com.yequan.common.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Auther: yq
 * @Date: 2019/8/26 21:36
 * @Description: 横向越权拦截器
 */
public class CrossPermissionInterceptor extends BaseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AppResult<Object> responseInfo;
        //获取当前登录用户
        Integer userId = CurrentUserLocal.getUserId();
        if (null == userId) {
            LogUtil.error("CrossPermissionInterceptor: 获取当前用户id为空");
            responseInfo = AppResultBuilder.failure(ResultCode.ERROR);
            renderMsg(response, responseInfo);
            return false;
        }
        if (handler instanceof HandlerMethod) {
            //获取当前被@CrossPermission注解的method对象
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (!method.isAnnotationPresent(CrossPermission.class)) {
                return true;
            }

            //解析@CrossPermission注解
            CrossPermission crossPermission = method.getAnnotation(CrossPermission.class);
            String key = crossPermission.key();
            boolean isPathVariable = crossPermission.isPathVariable();
            if (isPathVariable) {
                //从路径中获取用@PathVariable注解的参数值
                Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                //不存在@PathVariable注解
                if (pathVariables.size() == 0 || !pathVariables.containsKey(key)) {
                    LogUtil.error("CrossPermissionInterceptor: 横向越权拦截内部错误,不存在@PathVariable注解");
                    responseInfo = AppResultBuilder.failure(ResultCode.ERROR);
                    renderMsg(response, responseInfo);
                    return false;
                }
                Object pathValueObj = pathVariables.get(key);
                if (null == pathValueObj) {
                    LogUtil.error("CrossPermissionInterceptor: PathVariable路径参数为空");
                    responseInfo = AppResultBuilder.failure(ResultCode.ERROR);
                    renderMsg(response, responseInfo);
                    return false;
                }
                //传入的userId和当前登录用户id不一致,判断为横向越权访问
                if (Integer.parseInt(pathValueObj.toString()) != userId) {
                    responseInfo = AppResultBuilder.failure(ResultCode.PERMISSION_NO_ACCESS);
                    renderMsg(response, responseInfo);
                    return false;
                }
            } else {
                Map<String, String[]> parameterMap = request.getParameterMap();
                if (parameterMap == null || parameterMap.size() == 0 || !parameterMap.containsKey(key)) {
                    LogUtil.error("CrossPermissionInterceptor: 横向越权拦截错误,路径传参错误");
                    responseInfo = AppResultBuilder.failure(ResultCode.ERROR);
                    renderMsg(response, responseInfo);
                    return false;
                }
                String parameterId = request.getParameter(key);
                if (StringUtils.isEmpty(parameterId)) {
                    LogUtil.error("CrossPermissionInterceptor: 横向越权拦截错误,路径传参错误,传入用户id为空");
                    responseInfo = AppResultBuilder.failure(ResultCode.ERROR);
                    renderMsg(response, responseInfo);
                    return false;
                }
                //传入的userId和当前登录用户id不一致,判断为横向越权访问
                if (Integer.parseInt(parameterId) != (userId)) {
                    responseInfo = AppResultBuilder.failure(ResultCode.PERMISSION_NO_ACCESS);
                    renderMsg(response, responseInfo);
                    return false;
                }
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
