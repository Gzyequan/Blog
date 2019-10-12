package com.yequan.common.util;

import com.yequan.constant.OSEnum;

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
        Logger.debug("ping host :{},pingTimes :{},timeout :{}", host, pingTimes, timeout);
        BufferedReader in = null;
        try {
            if (null == host || pingTimes <= 0 || timeout <= 0) {
                return false;
            }
            Runtime r = Runtime.getRuntime();
            String pingCommand = null;
            if (OSUtil.getOSInfo() == OSEnum.OS_WIN.getCode()) {
                //windows格式的命令
                pingCommand = "ping " + host + " -n " + pingTimes + " -w " + timeout;
            } else if (OSUtil.getOSInfo() == OSEnum.OS_LINUX.getCode()) {
                //linux格式的命令
                pingCommand = "ping " + host + " -c " + pingTimes + " -w" + timeout;
            } else {
                Logger.error("ping : unsupported OS :{}", OSUtil.getOSInfo());
                return false;
            }
            Logger.debug("ping pingCommand :{}", pingCommand);
            Process p = r.exec(pingCommand);
            if (p == null) {
                Logger.error("ping process is null");
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
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        } finally {
            try {
                if (null != in)
                    in.close();
            } catch (IOException e) {
                Logger.error(e.getMessage(), e);
            }
        }
        return false;
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

}
