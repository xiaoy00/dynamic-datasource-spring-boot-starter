package com.github.xiaoy;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 对多数据源支持的包装数据源容器
 * @author songxiaoyue
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceRoutingKeyHolder.get();
    }

}
