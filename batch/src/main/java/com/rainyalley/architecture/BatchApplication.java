package com.rainyalley.architecture;

import com.rainyalley.architecture.batch.JdbcFirstPageRunner;
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
@ImportResource(locations = {"application-batch.xml"})
public class BatchApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(BatchApplication.class, args);
		JdbcFirstPageRunner jdbcFirstPageRunner = ctx.getBean("jdbcFirstPageRunner", JdbcFirstPageRunner.class);
		jdbcFirstPageRunner.run();
	}
}

