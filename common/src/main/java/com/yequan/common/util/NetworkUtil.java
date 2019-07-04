package com.yequan.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: yq
 * @Date: 2019/7/2 09:23
 * @Description:
 */
public class NetworkUtil {

    public static boolean ping(String host, int pingTimes, int timeout) {
        BufferedReader in = null;
        try {
            Runtime r = Runtime.getRuntime();
            String pingCommand = null;
            if (OSUtil.getOSInfo() == OSUtil.OS_WIN) {
                //windows格式的命令
                pingCommand = "ping " + host + " -n " + pingTimes + " -w " + timeout;
            } else if (OSUtil.getOSInfo() == OSUtil.OS_LINUX) {
                //linux格式的命令
                pingCommand = "ping " + host + " -c " + pingTimes + " -w" + timeout;
            }
            Process p = r.exec(pingCommand);
            if (p == null) {
                return false;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));   // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
            int connectedCount = 0;
            String line = null;
            while ((line = in.readLine()) != null) {
                connectedCount += getCheckResult(line);
            }
            // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
            return connectedCount == pingTimes;
        } catch (Exception ex) {
            ex.printStackTrace();   // 出现异常则返回假
            return false;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 若line含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.
     *
     * @param line
     * @return
     */
    private static int getCheckResult(String line) {
        Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            return 1;
        }
        return 0;
    }

    public static void main(String[] args) {
        String ipAddress = "www.baidu.com";
        System.out.println(ping(ipAddress, 5, 5000));
    }

}
