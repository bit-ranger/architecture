package com.rainyalley.architecture.batch;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.File;

public class DeleteDirTasklet implements Tasklet, InitializingBean {

    private String dirPath;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        FileUtils.deleteDirectory(new File(dirPath));
        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(dirPath, "dirPath can not be null");
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }
}
