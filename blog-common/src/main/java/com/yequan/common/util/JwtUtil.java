package com.yequan.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yequan.constant.TokenConsts;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @Auther: yq
 * @Date: 2019/7/10 11:28
 * @Description: JWT生成token工具类, 携带的信息
 */
public class JwtUtil {

    /**
     * 生成签名
     *
     * @param payload      载荷信息
     * @param timeInterval 过期时间间隔
     * @return
     */
    public static String sign(String payload, long timeInterval) {
        Logger.debug("sign payload :{}, timeInterval :{}", payload, timeInterval);
        try {
            //过期时间,当前时间+过期时长
            Date expireTime = new Date(System.currentTimeMillis() + timeInterval);
            //私钥及加密
            Algorithm algorithm = Algorithm.HMAC256(TokenConsts.TOKEN_PRIVATE_KEY);
            //设置头部信息
            Map<String, Object> header = new HashMap<>();
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            String token = JWT.create()
                    .withHeader(header)
                    .withClaim("info", payload)
                    .withExpiresAt(expireTime)
                    .sign(algorithm);
            Logger.debug("sign token :{}", token);
            return token;
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 校验token
     *
     * @param token
     * @return 返回值为自定义的载荷信息
     */
    public static String verify(String token) {
        Logger.debug(" verify token :{}", token);
        String tokenPayload = null;
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(TokenConsts.TOKEN_PRIVATE_KEY);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT verify = jwtVerifier.verify(token);
            Map<String, Claim> claims = verify.getClaims();
            Claim claim = claims.get("info");
            tokenPayload = claim.asString();
            Logger.debug("verify tokenPayload :{}", tokenPayload);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return tokenPayload;
    }

    public static void main(String[] args) {
        JSONObject infoObject = new JSONObject();
        infoObject.put("mobilephone", "15210482684");
        infoObject.put("userId", 1);
        infoObject.put("userAgent", "111");
        String jsonInfo = JSON.toJSONString(infoObject);
        String token = sign(jsonInfo, TokenConsts.TOKEN_EXPIRE_TIME_15_MINUTE);
        System.out.println(token);
        String verify = verify(token);
        System.out.println(verify);
    }


}
