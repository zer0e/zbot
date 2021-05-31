package com.github.zer0e.zbot.plugins.base;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.HashSet;
import java.util.Set;

public interface SchedulerPlugin extends Job {
    Set<String> schedulerTimeSet = new HashSet<>();

    void init();
    void execute();

    @Override
    default void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException{
        this.execute();
    }
}
