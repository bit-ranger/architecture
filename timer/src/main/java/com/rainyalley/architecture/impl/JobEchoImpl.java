package com.rainyalley.architecture.impl;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.TriggerKey;

@Slf4j
public class JobEchoImpl implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        TriggerKey triggerKey = context.getTrigger().getKey();
        log.info(String.format("echo >>>> %s,%s", triggerKey.getName(), triggerKey.getGroup()));
    }
}
