package com.rainyalley.architecture.controller.v1;

import com.rainyalley.architecture.enums.TimerResourceEnum;
import com.rainyalley.architecture.exception.BadArgException;
import com.rainyalley.architecture.impl.JobEchoImpl;
import com.rainyalley.architecture.po.Schedule;
import com.rainyalley.architecture.service.TimerService;
import com.rainyalley.architecture.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.TriggerKey;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.ParseException;

@Api(value = "v1/timer", description = "定时器配置")
@RestController
@RequestMapping("v1/timer")
@Validated
@Slf4j
public class TimerController {

    @Resource(name = "timerService")
    private TimerService timerService;

    @ApiOperation(value="schedule")
    @PostMapping("schedule")
    public Result schedule(@RequestBody @Valid Schedule schedule){
        JobDetail jobDetail = JobBuilder
                .newJob(JobEchoImpl.class)
                .storeDurably()
                .withIdentity("echo")
                .build();

        try {
            timerService.schedule(new TriggerKey(schedule.getName(), schedule.getGroup()), new CronExpression(schedule.getCron()), jobDetail);
        } catch (ParseException e) {
            throw new BadArgException(TimerResourceEnum.CRON);
        }
        return new Result(org.springframework.http.HttpStatus.OK);
    }

}
