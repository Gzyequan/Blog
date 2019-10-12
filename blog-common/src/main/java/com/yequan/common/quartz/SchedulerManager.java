package com.yequan.common.quartz;

import com.yequan.common.quartz.exception.ScheduleException;
import com.yequan.common.quartz.model.SchedulerJob;
import com.yequan.common.quartz.proxy.taskInterface.JobInterface;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: yq
 * @Date: 2019/4/24 14:39
 * @Description:
 */
public class SchedulerManager {

    /**
     * 日志对象
     */
    private static final Logger logger = LoggerFactory.getLogger(SchedulerManager.class);

    private Scheduler scheduler;

    /**
     * 添加一个定时任务
     *
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobProxy         job任务实现类
     * @param cron             时间设置
     * @param quartzJob        job参数
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
                       Class jobProxy, String cron, JobInterface quartzJob) {
        try {
            JobDataMap jobMap = new JobDataMap();
            jobMap.put(SchedulerJob.QUARTZ_TASK_KEY, quartzJob);
            //任务名,任务组,任务执行类
            JobDetail jobDetail =
                    JobBuilder.newJob(jobProxy).withIdentity(jobName, jobGroupName).usingJobData(jobMap).build();
            //触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            //触发器名,触发器组
            triggerBuilder = triggerBuilder.withIdentity(triggerName, triggerGroupName);
            triggerBuilder.startNow();
            //触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            //创建trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();
            //调度容器设置JobDetail和trigger
            scheduler.scheduleJob(jobDetail, trigger);
            //启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            logger.info("添加一个定时任务异常");
            throw new ScheduleException("添加一个定时任务异常");
        }
    }

    /**
     * 修改一个任务的触发时间
     *
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param cron             时间设置
     */
    protected void modifyJobTime(String jobName, String jobGroupName, String triggerName, String triggerGroupName, String cron) {
        try {
            //获取触发器
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldCron = trigger.getCronExpression();
            if (!oldCron.equalsIgnoreCase(cron)) {
                /**
                 * 调用rescheduleJob开始
                 */
                //触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                //触发器名,触发器组
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.startNow();
                //触发器事假设定
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                //创建Trigger对象
                trigger = ((CronTrigger) triggerBuilder.build());
                //方式一:修改一个任务的触发时间
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            logger.info("修改任务触发时间异常");
            throw new ScheduleException("修改任务触发时间异常");
        }
    }

    /**
     * 移除一个任务
     *
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     */
    protected void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            scheduler.pauseTrigger(triggerKey);//停止触发器
            scheduler.unscheduleJob(triggerKey);//移除触发器
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));//删除任务
        } catch (Exception e) {
            logger.info("移除一个定时任务异常");
            throw new ScheduleException("移除一个定时任务异常");
        }
    }

    /**
     * 暂停一个任务
     *
     * @param jobName      任务名
     * @param jobGroupName 任务组名
     */
    protected void pauseJob(String jobName, String jobGroupName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            logger.info("暂停定时任务异常");
            throw new ScheduleException("暂停定时任务异常");
        }
    }

    /**
     * 恢复运行一个任务
     *
     * @param jobName      任务名
     * @param jobGroupName 任务组名
     */
    protected void resumeJob(String jobName, String jobGroupName) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.resumeJob(jobKey);
        } catch (Exception e) {
            logger.info("恢复定时任务异常");
            throw new ScheduleException("恢复定时任务异常");
        }
    }

    /**
     * 启动所有定时任务
     */
    protected void startJobs() {
        try {
            scheduler.start();
        } catch (Exception e) {
            logger.info("启动所有定时任务异常");
            throw new ScheduleException("启动所有定时任务异常");
        }
    }

    /**
     * 关闭所有定时任务
     */
    protected void shutdownJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (SchedulerException e) {
            logger.info("关闭所有定时任务异常");
            throw new ScheduleException("关闭所有定时任务异常");
        }
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

}
