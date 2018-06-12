package com.rainyalley.architecture.batch;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @author bin.zhang
 */
public class JdbcFirstPageCircleItemReader<T> extends AbstractItemCountingItemStreamItemReader<T> implements ItemReader<T>, InitializingBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private DataSource dataSource;

    private RowMapper rowMapper;

    private PagingQueryProvider queryProvider;

    private PreparedStatementSetter preparedStatementSetter;

    private String[] sqlValues;

    private JdbcTemplate jdbcTemplate;

    private String firstPageSql;

    protected List<T> results;

    private int pageSize = 100;

    private int currentInPage = 0;


    @Override
    protected T doRead() throws Exception {
        if (results == null || currentInPage >= pageSize) {

            if (logger.isDebugEnabled()) {
                logger.debug("reading page, currentItemCount: " + getCurrentItemCount());
            }

            doReadPage();

            if (currentInPage >= pageSize) {
                currentInPage = 0;
            }

        }

        int next = currentInPage++;
        if (next < results.size()) {
            return results.get(next);
        }
        else {
            return null;
        }
    }

    @Override
    protected void doOpen() throws Exception { }

    @Override
    protected void doClose() throws Exception { }


    protected void doReadPage(){
        if (results == null) {
            results = new CopyOnWriteArrayList<T>();
        }
        else {
            results.clear();
        }

        List<T> result = jdbcTemplate.query(firstPageSql, preparedStatementSetter, rowMapper);

        results.addAll(result);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.isTrue(pageSize > 0);
        Assert.notNull(rowMapper);
        Assert.notNull(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setMaxRows(getPageSize());
        Assert.notNull(queryProvider);
        queryProvider.init(dataSource);
        this.firstPageSql = queryProvider.generateFirstPageQuery(getPageSize());
    }


    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public RowMapper getRowMapper() {
        return rowMapper;
    }

    public void setRowMapper(RowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }

    public PagingQueryProvider getQueryProvider() {
        return queryProvider;
    }

    public void setQueryProvider(PagingQueryProvider queryProvider) {
        this.queryProvider = queryProvider;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public PreparedStatementSetter getPreparedStatementSetter() {
        return preparedStatementSetter;
    }

    public void setPreparedStatementSetter(PreparedStatementSetter preparedStatementSetter) {
        this.preparedStatementSetter = preparedStatementSetter;
    }

    public String[] getSqlValues() {
        return sqlValues;
    }

    public void setSqlValues(String[] sqlValues) {
        this.sqlValues = sqlValues;
    }
}
