package com.rainyalley.architecture.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(
        scanBasePackages = {
                "com.rainyalley.architecture.config",
                "com.rainyalley.architecture.aop",
                "com.rainyalley.architecture.impl"},
        exclude = {DataSourceAutoConfiguration.class})
@ImportResource(locations = {"application-batch.xml"})
public class BatchConfig {}
