package com.github.zer0e.zbot.core.handler;

import com.github.zer0e.zbot.core.Registry;
import com.github.zer0e.zbot.utils.FileUtil;
import com.github.zer0e.zbot.utils.ReflectionUtils;
import lombok.NonNull;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class ScheduleHandler {
    private Registry registry;
    private Scheduler scheduler;
    private volatile boolean stop = false;
    private static Logger logger = LoggerFactory.getLogger(MsgHandler.class);


    public ScheduleHandler(@NonNull Registry registry) {
        this.registry = registry;
    }

    public void start(){
        if (scheduler != null && !stop){
            logger.info("定时任务已经启动");
            return;
        }
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
//            Set<JobDetail> jobDetailSet = new HashSet<>();
            // 创建插件的定时job
            for (Object o : registry.getScheduler_obj()){
                // 多个cron表达式
                for (String schedulerTime : (Set<String>)ReflectionUtils.getField(o,"schedulerTimeSet")){
                    JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) o.getClass())
                            .build();
                    CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                            .startNow()
                            .withSchedule(CronScheduleBuilder.cronSchedule(schedulerTime))
                            .build();
                    scheduler.scheduleJob(jobDetail,cronTrigger);
                }
            }
            scheduler.start();
            stop = false;
        }catch (SchedulerException e){
            e.printStackTrace();
            logger.error("quartz启动失败");
            this.stop();
        }
    }

    public void stop(){
        stop = true;
        if (scheduler != null){
            try {
                scheduler.shutdown();
            }catch (Exception ignored){

            }
        }
    }
}
