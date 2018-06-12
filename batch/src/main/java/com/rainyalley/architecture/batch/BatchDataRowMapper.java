package com.rainyalley.architecture.batch;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BatchDataRowMapper implements RowMapper<BatchData> {
    @NotNull
    @Override
    public BatchData mapRow(@NotNull ResultSet resultSet, int i) throws SQLException {
        BatchData batchData = new BatchData();
        batchData.setId(resultSet.getLong("id"));
        batchData.setStat(resultSet.getInt("stat"));
        batchData.setCrt_time(resultSet.getDate("crt_time"));
        return batchData;
    }
}
