package com.github.zer0e.zbot.plugins.base;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.HashSet;
import java.util.Set;

public abstract class SchedulerPlugin extends BasePlugin implements Job {
    public abstract void execute();
    public Set<String> schedulerTimeSet = new HashSet<>();
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        this.execute();
    }
}
