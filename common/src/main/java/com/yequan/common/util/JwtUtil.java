package com.yequan.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
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
     * 过期时间15分钟
     */
    private static final long EXPIRE_TIME_15 = 15 * 60 * 1000;

    private static final long EXPIRE_TIME_1 = 60 * 1000;

    /**
     * 过期时间30分钟
     */
    private static final long EXPIRE_TIME_30 = 30 * 60 * 1000;

    /**
     * token私钥
     */
    private final static String TOKEN_PRIVATE_KEY = "e10adc3949ba59abbe56e057f20f883e";

    /**
     * 生成签名
     *
     * @param jsonInfo
     * @return
     */
    public static String sign(String jsonInfo) {
        try {
            //过期时间,当前时间+过期时长
            Date expireTime = new Date(System.currentTimeMillis() + EXPIRE_TIME_15);
            //私钥及加密
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_PRIVATE_KEY);
            //设置头部信息
            Map<String, Object> header = new HashMap<>();
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            return JWT.create()
                    .withHeader(header)
                    .withClaim("info", jsonInfo)
                    .withExpiresAt(expireTime)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
        String tokenPayload = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_PRIVATE_KEY);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT verify = jwtVerifier.verify(token);
            Map<String, Claim> claims = verify.getClaims();
            Claim claim = claims.get("info");
            tokenPayload = claim.asString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tokenPayload;
    }

    public static void main(String[] args) {
        JSONObject infoObject = new JSONObject();
        infoObject.put("mobilephone", "15210482684");
        infoObject.put("userId", 1);
        infoObject.put("userAgent", "111");
        String jsonInfo = JSON.toJSONString(infoObject);
        String token = sign(jsonInfo);
        System.out.println(token);
        String verify = verify(token);
        System.out.println(verify);
    }


}
