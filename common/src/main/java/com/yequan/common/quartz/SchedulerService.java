package com.yequan.common.quartz;

import com.yequan.common.quartz.proxy.taskInterface.JobInterface;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Auther: yq
 * @Date: 2019/4/25 10:12
 * @Description:
 */
public class SchedulerService {

    private SchedulerManager schedulerManager;

    volatile private static SchedulerService instance = null;

    private static ApplicationContext applicationContext;

    static {
        applicationContext = new ClassPathXmlApplicationContext("spring-quartz.xml");
    }

    private SchedulerService() {
        schedulerManager = ((SchedulerManager) applicationContext.getBean("schedulerManager"));
    }

    public static SchedulerService getInstance() {
        if (null == instance) {
            synchronized (SchedulerService.class) {
                if (null == instance) {
                    instance = new SchedulerService();
                }
            }
        }
        return instance;
    }

    /**
     * 启动定时任务
     *  @param jobProxy
     * @param cron
     * @param quartzJob
     */
    public void startJob(String jobName, Class jobProxy, String cron, JobInterface quartzJob) {
        schedulerManager.addJob(jobName, jobName, jobName, jobName, jobProxy, cron, quartzJob);
    }

    /**
     * 修改定时器任务时间
     *
     * @param jobName
     * @param cron
     */
    public void updateJobTime(String jobName, String cron) {
        schedulerManager.modifyJobTime(jobName, jobName, jobName, jobName, cron);
    }

    /**
     * 移除定时任务
     *
     * @param jobName
     */
    public void removeJob(String jobName) {
        schedulerManager.removeJob(jobName, jobName, jobName, jobName);
    }

    /**
     * 暂停一次定时任务
     *
     * @param jobName
     */
    public void pauseJob(String jobName) {
        schedulerManager.pauseJob(jobName, jobName);
    }

    /**
     * 恢复一次定时任务
     *
     * @param jobName
     */
    public void resumeJob(String jobName) {
        schedulerManager.resumeJob(jobName, jobName);
    }

    /**
     * 关闭所有定时任务
     */
    public void shutdownJobs() {
        schedulerManager.shutdownJobs();
    }

}
