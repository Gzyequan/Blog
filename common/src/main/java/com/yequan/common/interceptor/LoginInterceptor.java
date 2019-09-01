package com.yequan.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.yequan.common.application.constant.RedisConsts;
import com.yequan.common.application.constant.SecurityConsts;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.bean.TokenPayload;
import com.yequan.common.service.RedisService;
import com.yequan.common.util.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Auther: yq
 * @Date: 2019/7/5 11:00
 * @Description:
 */
public class LoginInterceptor extends BaseInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        //对登录,注册放行
        if (uri.indexOf("/login") > 0 || uri.indexOf("/register") > 0) {
            return true;
        }

        // 校验token

        //获取客户端传入的token
        String accessToken = request.getHeader("access-token");

        if (StringUtils.isEmpty(accessToken)) {
            Logger.error("token为空");
            renderMsg(response, AppResultBuilder.failure(ResultCode.USER_NOT_LOGGED_IN));
            return false;
        }

        //获取加密的自定义载荷信息
        String infoEncrypt = JwtUtil.verify(accessToken);
        if (StringUtils.isEmpty(infoEncrypt)) {
            Logger.error("加密的载荷信息为空");
            renderMsg(response, AppResultBuilder.failure(ResultCode.USER_LOGIN_EXPIRED));
            return false;
        }

        //解密自定义载荷信息
        String infoDecrypt = AESUtil.decryptToStr(infoEncrypt, SecurityConsts.AES_KEY);
        if (StringUtils.isEmpty(infoDecrypt)) {
            Logger.error("解密的载荷信息为空");
            renderMsg(response, AppResultBuilder.failure(ResultCode.USER_LOGIN_ERROR));
            return false;
        }

        //解析有效载荷
        TokenPayload tokenPayload = JSON.parseObject(infoDecrypt, TokenPayload.class);

        //校验载荷信息中用户id是否为空
        Integer userId = tokenPayload.getUserId();
        if (null == userId) {
            Logger.error("载荷信息中用户id为空");
            renderMsg(response, AppResultBuilder.failure(ResultCode.USER_LOGIN_ERROR));
            return false;
        }
        //开始校验与redis中的用户信息是否一致
        //从redis中获取当前token携带的用户id锁所对应的用户信息
        Map redisUserInfoMap = redisService.getMap(RedisConsts.REDIS_CURRENT_USER + userId);
        if (MapUtils.isEmpty(redisUserInfoMap)) {
            Logger.error("redis中获取当前登录用户的信息为空,redis键:{}",RedisConsts.REDIS_CURRENT_USER + userId);
            renderMsg(response, AppResultBuilder.failure(ResultCode.USER_LOGIN_EXPIRED));
            return false;
        }

        //获取客户端信息,用于校验在token有效期内是否是同一个设备中登录
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = IPUtil.getIpAddress(request);
        if (!tokenPayload.getUserAgent().equals(userAgent)
                || !tokenPayload.getIp().equals(ipAddress)) {
            Logger.error("远程登录");
            //TODO 远程登录逻辑有误
            renderMsg(response, AppResultBuilder.failure(ResultCode.USER_LOGIN_REMOTE));
            return false;
        }

        //校验通过,重置过期时间
        boolean redisSuccess = redisService.setExpire(RedisConsts.REDIS_CURRENT_USER + userId,
                RedisConsts.REDIS_EXPIRE_15_MINUTE);
        if (!redisSuccess) {
            Logger.error("设置登录用户过期时间错误,redis键:{}",RedisConsts.REDIS_CURRENT_USER + userId);
            renderMsg(response, AppResultBuilder.failure(ResultCode.ERROR));
            return false;
        }
        CurrentUserLocal.setUserId(userId);
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
