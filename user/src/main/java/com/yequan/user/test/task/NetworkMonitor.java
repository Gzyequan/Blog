package com.yequan.user.test.task;

import com.yequan.common.util.NetworkUtil;
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
    private NetworkMonitorListener listener;

    private final static int DEFAULT_PINGTIMES = 5;
    private final static int DEFAULT_TIMEOUT = 3000;

    public NetworkMonitor(NetworkMonitorListener listener, String host, int pingTimes, int timeout) {
        this.host = host;
        this.pingTimes = pingTimes;
        this.timeout = timeout;
        this.listener = listener;
    }

    public NetworkMonitor(NetworkMonitorListener listener, String host) {
        this(listener, host, DEFAULT_PINGTIMES, DEFAULT_TIMEOUT);
    }

    public void setListener(NetworkMonitorListener listener) {
        this.listener = listener;
    }

    public void execute() {
        boolean isReachable = NetworkUtil.ping(host, pingTimes, timeout);
        if (null != listener) {
            listener.onMessage(isReachable);
        } else {
            throw new NullPointerException("NetworkMonitorListener 不能为空");
        }
    }

}
