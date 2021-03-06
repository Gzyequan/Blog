package com.yequan.common.util;

import com.yequan.constant.SecurityConsts;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * @Auther: yq
 * @Date: 2019/7/10 14:29
 * @Description: AES加解密工具
 */
public class AESUtil {

    /**
     * 加密
     *
     * @param data 明文待加密
     * @param key  秘钥
     * @return
     */
    public static byte[] encrypt(String data, String key) {
        Logger.debug("encrypt data :{}, key :{}", data, key);
        try {
            if (StringUtils.isEmpty(data) || StringUtils.isEmpty(key)) {
                return null;
            }
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            //选择一种算法
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key.getBytes());
            keyGenerator.init(SecurityConsts.AES_KEY_SIZE, secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] encoded = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(encoded, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * AES加密，返回String
     *
     * @param data
     * @param key
     * @return
     */
    public static String encryptToStr(String data, String key) {
        return StringUtils.isNotBlank(data) ? ByteUtil.byteArray2HexString(encrypt(data, key)) : null;
    }

    /**
     * 解密
     *
     * @param data
     * @param key
     * @return
     */
    public static byte[] decrypt(byte[] data, String key) {
        Logger.debug("decrypt data is null :{}, key :{}", ArrayUtils.isEmpty(data), key);
        if (ArrayUtils.isEmpty(data) || StringUtils.isEmpty(key)) {
            return null;
        }
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            //选择一种固定算法，为了避免不同java实现的不同算法，生成不同的密钥，而导致解密失败
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(key.getBytes());
            keyGenerator.init(SecurityConsts.AES_KEY_SIZE, random);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);// 初始化
            return cipher.doFinal(data);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * AES解密，返回String
     *
     * @param encryptData
     * @param key
     * @return
     */
    public static String decryptToStr(String encryptData, String key) {
        return StringUtils.isNotBlank(encryptData) ? new String(decrypt(ByteUtil.hexString2ByteArray(encryptData), key)) : null;
    }

    public static void main(String[] args) {
        String a = "hello";
        String encryptToStr = encryptToStr(a, SecurityConsts.AES_KEY);
        System.out.println(encryptToStr);
        String decryptToStr = decryptToStr(encryptToStr, SecurityConsts.AES_KEY);
        System.out.println(decryptToStr);
    }

}
