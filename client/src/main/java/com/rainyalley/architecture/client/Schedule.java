package com.rainyalley.architecture.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Schedule {

    private String name;

    private String group;

    private String cron;

}
