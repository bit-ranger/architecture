package com.rainyalley.architecture.service;

import org.quartz.CronExpression;
import org.quartz.JobDetail;
import org.quartz.TriggerKey;

public interface TimerService {


    void schedule(TriggerKey triggerKey,
                  CronExpression cronExpression,
                  JobDetail jobDetail);
}
