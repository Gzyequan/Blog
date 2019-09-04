package com.yequan.common.service.impl;

import com.yequan.common.application.constant.HttpHeaderConsts;
import com.yequan.common.application.constant.RedisConsts;
import com.yequan.common.application.response.AppResult;
import com.yequan.common.application.response.AppResultBuilder;
import com.yequan.common.application.response.ResultCode;
import com.yequan.common.service.RedisService;
import com.yequan.common.service.TokenService;
import com.yequan.common.util.Logger;
import com.yequan.common.util.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: Administrator
 * @Date: 2019/8/31 10:48
 * @Description:
 */
@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisService redisService;

    @Override
    public AppResult createToken() {
        String uuid32 = RandomUtil.UUID32();
        StringBuilder token = new StringBuilder();
        token.append(RedisConsts.REDIS_TOKEN).append(uuid32);
        boolean redisSuccess = redisService.set(token.toString(), token.toString(), RedisConsts.REDIS_EXPIRE_1_MINUTE);
        if (!redisSuccess) {
            Logger.error("操作redis错误");
            return AppResultBuilder.failure(ResultCode.ERROR);
        }
        return AppResultBuilder.success(token);
    }

    @Override
    public boolean checkToken(HttpServletRequest request) {
        String idempotentToken = request.getHeader(HttpHeaderConsts.IDEMPOTENT_TOKEN);
        if (StringUtils.isEmpty(idempotentToken)) {
            return false;
        }
        String tokenValue = redisService.get(idempotentToken).toString();
        //校验是否存在该token
        if (StringUtils.isEmpty(tokenValue)) {
            return false;
        }
        return redisService.del(idempotentToken);
    }

}
