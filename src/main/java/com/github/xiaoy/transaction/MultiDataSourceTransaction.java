package com.github.xiaoy.transaction;

import com.github.xiaoy.DataSourceRoutingKeyHolder;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.springframework.util.Assert.notNull;

/**
 * @Author: songxiaoyue
 */
public class MultiDataSourceTransaction implements Transaction {

    private final DataSource dataSource;

    private Connection mainConnection;

    private String mainDatabaseIdentification;

    private ConcurrentMap<String, Connection> otherConnectionMap;

    private boolean isConnectionTransactional;

    private boolean autoCommit;

    private Connection connection;

    public MultiDataSourceTransaction(DataSource dataSource) {
        notNull(dataSource, "No DataSource specified");
        this.dataSource = dataSource;
        otherConnectionMap = new ConcurrentHashMap<>();
        mainDatabaseIdentification= DataSourceRoutingKeyHolder.get().toString();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection() throws SQLException {
        String databaseIdentification = DataSourceRoutingKeyHolder.get().toString();
        if (databaseIdentification.equals(mainDatabaseIdentification)) {
            if (mainConnection != null){
                return mainConnection;
            }else {
                openMainConnection();
                mainDatabaseIdentification =databaseIdentification;
                return mainConnection;
            }
        } else {
            if (!otherConnectionMap.containsKey(databaseIdentification)) {
                try {
                    Connection conn = dataSource.getConnection();
                    conn.setAutoCommit(autoCommit);
                    otherConnectionMap.put(databaseIdentification, conn);
                } catch (SQLException ex) {
                    throw new CannotGetJdbcConnectionException("Could not get JDBC Connection", ex);
                }
            }
            return otherConnectionMap.get(databaseIdentification);
        }

    }


    private void openMainConnection() throws SQLException {
        connection = DataSourceUtils.getConnection(this.dataSource);
        boolean autoCommit =connection.getAutoCommit();
        this.mainConnection = this.dataSource.getConnection();
        mainConnection.setAutoCommit(autoCommit);
        this.autoCommit = this.mainConnection.getAutoCommit();
        this.isConnectionTransactional = DataSourceUtils.isConnectionTransactional(connection, this.dataSource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() throws SQLException {
        if (this.mainConnection != null && this.isConnectionTransactional && !this.autoCommit) {

            this.mainConnection.commit();
            for (Connection connection : otherConnectionMap.values()) {
                connection.commit();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback() throws SQLException {
        if (this.mainConnection != null && this.isConnectionTransactional && !this.autoCommit) {
            this.mainConnection.rollback();
            for (Connection connection : otherConnectionMap.values()) {
                connection.rollback();
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        DataSourceUtils.releaseConnection(this.connection,this.dataSource);
        DataSourceUtils.releaseConnection(this.mainConnection, this.dataSource);
        for (Connection connection : otherConnectionMap.values()) {
            DataSourceUtils.releaseConnection(connection, this.dataSource);
        }
    }

    /**
     * 涉及到多个数据源,暂时默认以主数据源的timeout为准。
     * @return
     */
    @Override
    public Integer getTimeout() {
        if (this.dataSource != null) {
            ConnectionHolder holder = (ConnectionHolder)TransactionSynchronizationManager.getResource(this.dataSource);
            return holder != null && holder.hasTimeout() ? holder.getTimeToLiveInSeconds() : null;
        }
        return null;
    }
}
