package com.yequan.common.util;


import org.apache.commons.lang3.ArrayUtils;

/**
 * @Auther: yq
 * @Date: 2019/7/10 14:48
 * @Description:
 */
public class ByteUtil {

    /**
     * 将二进制数组转换成16进制字符串
     *
     * @param data
     * @return
     */
    public static String byteArray2HexString(byte[] data) {
        Logger.debug("byteArray2HexString  data is null :{}", ArrayUtils.isEmpty(data));
        if (null == data || data.length < 1) {
            return null;
        }
        String hexString = null;
        try {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < data.length; i++) {
                String hex = Integer.toHexString(data[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                stringBuffer.append(hex.toUpperCase());
            }
            hexString = stringBuffer.toString();
            Logger.debug("byteArray2HexString hexString :{}", hexString);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return hexString;
    }

    /**
     * 将16进制字符串转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] hexString2ByteArray(String hexStr) {
        Logger.debug("hexString2ByteArray hexStr :{}", hexStr);
        if (null == hexStr || hexStr.length() < 1) {
            return null;
        }
        byte[] result = null;
        try {
            result = new byte[hexStr.length() / 2];
            for (int i = 0; i < hexStr.length() / 2; i++) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte) (high * 16 + low);
            }
            Logger.debug("byteArray2HexString result length :{}", result.length);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        return result;
    }

    public static void main(String[] args) {
        String str = new String("1213");
        byte[] bytes = hexString2ByteArray(str);
        String s = byteArray2HexString(bytes);
        System.out.println(s);
    }

}
