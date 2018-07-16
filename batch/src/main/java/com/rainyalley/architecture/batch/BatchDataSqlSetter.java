package com.rainyalley.architecture.batch;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BatchDataSqlSetter implements PreparedStatementSetter {
    @Override
    public void setValues(@NotNull PreparedStatement ps) throws SQLException {
        ps.setInt(1, 0);
    }
}
