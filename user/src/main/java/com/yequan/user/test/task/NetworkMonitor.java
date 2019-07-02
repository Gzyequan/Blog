package com.yequan.user.test.task;

import com.yequan.common.network.NetworkUtil;
import com.yequan.common.quartz.proxy.taskInterface.AsyncJobInterface;

/**
 * @Auther: yq
 * @Date: 2019/7/2 15:28
 * @Description:
 */
public class NetworkMonitor implements AsyncJobInterface {

    private String host;
    private int pingTimes;
    private int timeout;

    private final static int DEFAULT_PINGTIMES = 5;
    private final static int DEFAULT_TIMEOUT = 3000;

    public NetworkMonitor(String host, int pingTimes, int timeout) {
        this.host = host;
        this.pingTimes = pingTimes;
        this.timeout = timeout;
    }

    public NetworkMonitor(String host) {
        this(host, DEFAULT_PINGTIMES, DEFAULT_TIMEOUT);
    }


    public void execute() {
        boolean isReachable = NetworkUtil.ping(host, pingTimes, timeout);
        if (isReachable) {
            System.out.println("主机: " + host + "  可达");
        } else {
            System.out.println("主机: " + host + "  不可达");
        }
    }
}
