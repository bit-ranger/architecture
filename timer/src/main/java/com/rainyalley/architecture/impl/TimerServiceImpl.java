package com.rainyalley.architecture.impl;

import com.rainyalley.architecture.enums.TimerResourceEnum;
import com.rainyalley.architecture.exception.BadArgException;
import com.rainyalley.architecture.service.TimerService;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.util.Date;

public class TimerServiceImpl implements TimerService {


    private Scheduler scheduler;


    @Override
    public void schedule(TriggerKey triggerKey, CronExpression cronExpression, JobDetail jobDetail) {

        if (isValidExpression(cronExpression)) {

            CronTriggerImpl trigger = new CronTriggerImpl();
            trigger.setCronExpression(cronExpression);
            trigger.setJobName(jobDetail.getKey().getName());
            trigger.setKey(triggerKey);

            try {
                scheduler.addJob(jobDetail, true);
                if (scheduler.checkExists(triggerKey)) {
                    scheduler.rescheduleJob(triggerKey, trigger);
                } else {
                    scheduler.scheduleJob(trigger);
                }
            } catch (SchedulerException e) {
                throw new IllegalArgumentException(e);
            }
        } else{
            throw new BadArgException(TimerResourceEnum.CRON);
        }
    }


    private boolean isValidExpression(final CronExpression cronExpression) {

        CronTriggerImpl trigger = new CronTriggerImpl();
        trigger.setCronExpression(cronExpression);

        Date now = new Date(System.currentTimeMillis() - 999);
        Date fireTime = trigger.computeFirstFireTime(null);

        return fireTime != null && !fireTime.before(now);
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}
