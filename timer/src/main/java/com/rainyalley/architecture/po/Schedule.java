package com.rainyalley.architecture.po;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Schedule {

    @NotBlank
    private String name;

    @NotBlank
    private String group;

    @NotBlank
    private String cron;

}
