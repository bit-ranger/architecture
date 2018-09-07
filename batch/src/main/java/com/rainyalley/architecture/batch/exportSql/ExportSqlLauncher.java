package com.rainyalley.architecture.batch.exportSql;

import com.rainyalley.architecture.batch.JobRunner;
import org.springframework.batch.core.JobParametersBuilder;

public class ExportSqlLauncher {

    private JobRunner jobRunner;

    public void launch() throws Exception{
        JobParametersBuilder builder = new JobParametersBuilder();
        builder.addString("label", "exportExampleData");
        builder.addString("dataSourceName", "secondaryDataSource", false);
        builder.addString("selectClause", "select id, stat, crt_time", false);
        builder.addString("fromClause", "from example_data", false);
        builder.addString("whereClause", "", false);
        builder.addString("sortKey", "id", false);
        builder.addString("fileHeader", "编号,状态,创建时间", false);
        builder.addString("itemFunction", "csvItemFunction", false);
        jobRunner.abandon("exportSqlJob", builder.toJobParameters());
        jobRunner.run("exportSqlJob", builder.toJobParameters());
    }

    public void setJobRunner(JobRunner jobRunner) {
        this.jobRunner = jobRunner;
    }
}
