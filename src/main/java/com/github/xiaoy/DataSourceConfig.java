package com.github.xiaoy;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.xiaoy.aop.DynamicDataSourceAnnotationAdvisor;
import com.github.xiaoy.aop.DynamicDataSourceAnnotationInterceptor;
import com.github.xiaoy.transaction.MultiDataSourceTransactionFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据源配置
 * @author songxiaoyue
 */
@Configuration
@Import(MultiDbConnectionConfig.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class DataSourceConfig {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Autowired
    private MultiDbConnectionConfig dbConnConfig;

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    @Primary
    public DataSource dataSource() {

        List<MultiDbConnectionConfig.ConnectionInfo> list = dbConnConfig.getList();
        List<MultiDbConnectionConfig.ConnectionInfo> connectionInfoList =list;
        Map<Object, Object> datasourceMap = new HashMap<>(list.size());
        for (MultiDbConnectionConfig.ConnectionInfo connectionInfo: connectionInfoList) {
            datasourceMap.put(connectionInfo.getName(), createDataSource(connectionInfo));
        }
        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setTargetDataSources(datasourceMap);

        if(list != null && list.get(0) != null && list.get(0).getName() != null){
            routingDataSource.setDefaultTargetDataSource(datasourceMap.get(list.get(0).getName()));
        }else {
            routingDataSource.setDefaultTargetDataSource(datasourceMap.get("defaults"));
        }

        logger.info("init routingDataSource: " + connectionInfoList);
        return routingDataSource;
    }

    private DataSource createDataSource(MultiDbConnectionConfig.ConnectionInfo connectionInfo) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(connectionInfo.getUrl());
        dataSource.setUsername(connectionInfo.getUsername());
        dataSource.setPassword(connectionInfo.getPassword());
        dataSource.setDriverClassName(connectionInfo.getDriverClassName());
        if (connectionInfo.getInitialSize() != null) {
            dataSource.setInitialSize(connectionInfo.getInitialSize());
        }

        if (connectionInfo.getMaxActive() != null) {
            dataSource.setMaxActive(connectionInfo.getMaxActive());
        }

        if (connectionInfo.getMinIdle() != null) {
            dataSource.setMinIdle(connectionInfo.getMinIdle());
        }

        if (connectionInfo.getMaxIdle() != null) {
            dataSource.setMaxIdle(connectionInfo.getMaxIdle());
        }

        if (connectionInfo.getValidationQuery() != null) {
            dataSource.setValidationQuery(connectionInfo.getValidationQuery());
        }

        if (connectionInfo.getTestOnBorrow() != null) {
            dataSource.setTestOnBorrow(connectionInfo.getTestOnBorrow());
        }

        if (connectionInfo.getTestOnReturn() != null) {
            dataSource.setTestOnReturn(connectionInfo.getTestOnReturn());
        }

        if (connectionInfo.getTestWhileIdle() != null) {
            dataSource.setTestWhileIdle(connectionInfo.getTestWhileIdle());
        }
        if(connectionInfo.getTimeBetweenEvictionRunsMillis() != null){
            dataSource.setTimeBetweenEvictionRunsMillis(connectionInfo.getTimeBetweenEvictionRunsMillis());
        }
        String schema = connectionInfo.getSchema();
        if (StringUtils.hasText(schema)) {
            runScript(dataSource, schema);
        }
        return dataSource;
    }

    private void runScript(DataSource dataSource, String location) {
        if (StringUtils.hasText(location)) {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.setSeparator(";");
            ClassPathResource resource = new ClassPathResource(location);
            if (resource.exists()) {
                populator.addScript(resource);
                try {
                    DatabasePopulatorUtils.execute(populator, dataSource);
                } catch (Exception e) {
                    logger.warn("execute sql error", e);
                }
            } else {
                logger.warn("could not find schema or data file {}", location);
            }
        }
    }

    @Bean(name="sqlSessionFactory")
    public SqlSessionFactory SqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setTransactionFactory(new MultiDataSourceTransactionFactory());
        String mapperLocations = applicationContext.getEnvironment().getProperty("mybatis.mapper-locations");
        if(!StringUtils.isEmpty(mapperLocations)){
            sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        }
        return sessionFactoryBean.getObject();
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceAnnotationAdvisor dynamicDatasourceAnnotationAdvisor() {
        DynamicDataSourceAnnotationInterceptor interceptor = new DynamicDataSourceAnnotationInterceptor();
        DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(
                interceptor);
        advisor.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return advisor;
    }

}
