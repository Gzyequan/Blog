package com.yequan.common.quartz.proxy;

import com.yequan.common.quartz.model.SchedulerJob;
import com.yequan.common.quartz.proxy.taskInterface.AsyncJobInterface;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @Auther: yq
 * @Date: 2019/4/24 18:52
 * @Description: 异步任务代理
 */
public class AsyncJobProxy extends QuartzJobBean {
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        AsyncJobInterface asyncJobInterface = (AsyncJobInterface) jobDataMap.get(SchedulerJob.QUARTZ_TASK_KEY);
        asyncJobInterface.execute();
    }
}
