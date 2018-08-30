package com.rainyalley.architecture.batch;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PassThroughRowMapper implements RowMapper<ResultSet> {

    @Override
    public ResultSet mapRow(ResultSet resultSet, int i) throws SQLException {
        return resultSet;
    }
}
