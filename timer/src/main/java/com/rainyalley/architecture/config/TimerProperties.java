package com.rainyalley.architecture.config;

import lombok.Data;

import java.time.Duration;

@Data
public class TimerProperties {

    private Duration reloadInterval;

}
