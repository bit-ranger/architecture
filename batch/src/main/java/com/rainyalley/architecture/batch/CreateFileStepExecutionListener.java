package com.rainyalley.architecture.batch;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import java.io.File;

public class CreateFileStepExecutionListener implements StepExecutionListener {

    private String filePath;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        try {
            File file = new File(filePath);
            FileUtils.touch(file);
        } catch (Exception e) {
            throw new RuntimeException(String.format("CreateFile error, %s", filePath));
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
