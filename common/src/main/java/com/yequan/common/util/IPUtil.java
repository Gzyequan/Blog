package com.yequan.common.util;


import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: yq
 * @Date: 2019/7/4 16:02
 * @Description: 根据request获取真实ip地址, 适用于集群或代理环境
 */
public class IPUtil {

    public static String getIpAddress(HttpServletRequest request) {
        //用户真实ip
        String xip = request.getHeader("X-Real-IP");
        //X-Forwarded-For存储了真实ip和中间代理服务器IP,中间以','分割,第一个ip为真实ip
        String xfor = request.getHeader("X-Forwarded-For");

        if (!"".equals(xfor) && "unKnow".equals(xfor)) {
            int index = xfor.indexOf(",");
            if (index != -1) {
                //取出第一个ip即是真实ip
                return xfor.substring(0, index);
            } else {
                //没有','即表示只有一个真实ip
                return xfor;
            }
        }

        xfor = xip;
        if (!"".equals(xfor) && !"unKnown".equalsIgnoreCase(xfor)) {
            return xfor;
        }
        if ("".equals(xfor) || "unknown".equalsIgnoreCase(xfor)) {
            xfor = request.getHeader("Proxy-Client-IP");
        }
        if ("".equals(xfor) || "unknown".equalsIgnoreCase(xfor)) {
            xfor = request.getHeader("WL-Proxy-Client-IP");
        }
        if ("".equals(xfor) || "unknown".equalsIgnoreCase(xfor)) {
            xfor = request.getHeader("HTTP_CLIENT_IP");
        }
        if ("".equals(xfor) || "unknown".equalsIgnoreCase(xfor)) {
            xfor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if ("".equals(xfor) || "unknown".equalsIgnoreCase(xfor)) {
            xfor = request.getRemoteAddr();
        }
        return xfor;
    }

}
