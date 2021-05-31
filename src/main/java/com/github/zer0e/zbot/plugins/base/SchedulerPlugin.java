package com.github.zer0e.zbot.plugins.base;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public interface SchedulerPlugin extends Job {
    void init();
    void execute();

    @Override
    default void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException{
        this.execute();
    }
}
