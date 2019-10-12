package com.yequan.user.test;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * @Auther: yq
 * @Date: 2019/7/2 15:27
 * @Description:
 */
public class NetworkRun {

    private static String host = "172.16.20.73";
    private static int pingTimes = 5;
    private static int timeout = 3000;

    public static void main(String[] args) {

//        NetworkMonitor networkMonitor = new NetworkMonitor(host);
//        SchedulerService.getInstance().startJob(
//                "NetworkMonitor",
//                AsyncJobProxy.class,
//                "0/5 * * * * ?",
//                networkMonitor);
        List<GarbageCollectorMXBean> beans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean bean : beans) {
            System.out.println(bean.getName());
        }

    }

}
