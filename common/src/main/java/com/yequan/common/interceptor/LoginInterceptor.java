package com.yequan.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.yequan.common.application.constant.RedisConsts;
import com.yequan.common.application.constant.SecurityConsts;
import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.bean.TokenPayload;
import com.yequan.common.redis.RedisService;
import com.yequan.common.util.AESUtil;
import com.yequan.common.util.CurrentUserLocal;
import com.yequan.common.util.IPUtil;
import com.yequan.common.util.JwtUtil;
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

        AppResult<Object> responseInfo;
        /**
         * 校验token
         */
        //获取客户端传入的token
        String accessToken = request.getHeader("access-token");

        if (StringUtils.isEmpty(accessToken)) {
            responseInfo = AppResultBuilder.failure(ResultCode.USER_NOT_LOGGED_IN);
            renderMsg(response, responseInfo);
            return false;
        }

        //获取加密的自定义载荷信息
        String infoEncrypt = JwtUtil.verify(accessToken);
        if (StringUtils.isEmpty(infoEncrypt)) {
            responseInfo = AppResultBuilder.failure(ResultCode.USER_LOGIN_EXPIRED);
            renderMsg(response, responseInfo);
            return false;
        }

        //解密自定义载荷信息
        String infoDecrypt = AESUtil.decryptToStr(infoEncrypt, SecurityConsts.AES_KEY);
        if (StringUtils.isEmpty(infoDecrypt)) {
            responseInfo = AppResultBuilder.failure(ResultCode.USER_LOGIN_ERROR);
            renderMsg(response, responseInfo);
            return false;
        }

        //解析有效载荷
        TokenPayload tokenPayload = JSON.parseObject(infoDecrypt, TokenPayload.class);

        //校验载荷信息中用户id是否为空
        Integer userId = tokenPayload.getUserId();
        if (null == userId) {
            responseInfo = AppResultBuilder.failure(ResultCode.USER_LOGIN_ERROR);
            renderMsg(response, responseInfo);
            return false;
        }
        //开始校验与redis中的用户信息是否一致
        //从redis中获取当前token携带的用户id锁所对应的用户信息
        Map redisUserInfoMap = redisService.getMap(RedisConsts.REDIS_CURRENT_USER + userId);
        if (MapUtils.isEmpty(redisUserInfoMap)) {
            responseInfo = AppResultBuilder.failure(ResultCode.USER_LOGIN_EXPIRED);
            renderMsg(response, responseInfo);
            return false;
        }

        //获取客户端信息,用于校验在token有效期内是否是同一个设备中登录
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = IPUtil.getIpAddress(request);
        if (!tokenPayload.getUserAgent().equals(userAgent)
                || !tokenPayload.getIp().equals(ipAddress)) {
            //TODO 远程登录逻辑有误
            responseInfo = AppResultBuilder.failure(ResultCode.USER_LOGIN_REMOTE);
            renderMsg(response, responseInfo);
            return false;
        }

        //登录成功后重置过期时间
        redisService.setExpire(RedisConsts.REDIS_CURRENT_USER + userId,
                RedisConsts.REDIS_EXPIRE_SECOND);
        CurrentUserLocal.setUserId(userId);
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
