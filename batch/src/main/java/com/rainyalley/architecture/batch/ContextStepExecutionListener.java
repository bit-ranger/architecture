/**
 * 
 */
package com.rainyalley.architecture.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class ContextStepExecutionListener implements StepExecutionListener {
	private static final Logger logger = LoggerFactory.getLogger(ContextStepExecutionListener.class);

	@Override
	public void beforeStep(StepExecution stepExecution) {
		stepExecution.getJobExecution().getExecutionContext().put("jobExecutionId", stepExecution.getJobExecution().getId());
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {

		return stepExecution.getExitStatus();
	}

}
