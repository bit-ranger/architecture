package com.rainyalley.architecture;

import com.rainyalley.architecture.batch.JobRunner;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

/**
 * @author bin.zhang
 */
@SpringBootApplication(
		scanBasePackages = {
				"com.rainyalley.architecture.config",
				"com.rainyalley.architecture.aop",
				"com.rainyalley.architecture.impl"},
		exclude = {DataSourceAutoConfiguration.class})
@ImportResource(locations = {"application-batch-import.xml"})
public class BatchApplication {

	public static void main(String[] args) throws Exception{
		ConfigurableApplicationContext ctx = SpringApplication.run(BatchApplication.class, args);
		JobRunner jobRunner = ctx.getBean("jobRunner", JobRunner.class);
		JobParametersBuilder builder = new JobParametersBuilder();
		builder.addLong("version", System.currentTimeMillis());
		builder.addString("dataSourceName", "secondaryDataSource");
		builder.addString("selectClause", "select id, stat, crt_time");
		builder.addString("fromClause", "from example_data");
		builder.addString("whereClause", "");
		builder.addString("sortKey", "id");
		builder.addString("fileHeader", "编号,状态,创建时间");
		jobRunner.run("exportSqlJob", builder.toJobParameters());
	}
}

