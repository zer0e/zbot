package com.github.zer0e.zbot.plugins;

import com.github.zer0e.zbot.core.Api;
import com.github.zer0e.zbot.plugins.base.SchedulerPlugin;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SchedulerTestPlugin extends SchedulerPlugin {
    private final Api api = Api.getApi();

    @Override
    public void init() {
        // 一个cron表达式
        this.schedulerTimeSet.add("0 0/2 * * * ?");
    }

    @Override
    public void execute() {
        api.send_plain_msg_to_group("123456","这是一个定时任务");
    }
}
