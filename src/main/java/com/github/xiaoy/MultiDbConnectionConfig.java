package com.github.xiaoy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 多数据库连接信息配置
 * application.yml配置示例：
 * spring:
 *   datasource:
 *     list:
 *     - name: user
 *       url: jdbc:mysql://127.0.0.1:3306/user
 *       username: root
 *       password: root
 *       driverClassName: com.mysql.jdbc.Driver
 *       ......
 *     - name: order
 *       url: jdbc:mysql://127.0.0.1:3306/order
 *       username: root
 *       password: root
 *       driverClassName: com.mysql.jdbc.Driver
 *       ......
 * @author songxiaoyue
 */
@Configuration
@ConfigurationProperties("spring.datasource")
public class MultiDbConnectionConfig {

    private final List<ConnectionInfo> list = new ArrayList<>();

    public static class ConnectionInfo {

        private String name;
        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private Integer initialSize;
        private Integer maxActive;
        private Integer minIdle;
        private Integer maxIdle;
        private Long maxWait;
        private String validationQuery;
        private Boolean testOnBorrow;
        private Boolean testOnReturn;
        private Boolean testWhileIdle;
        private Long timeBetweenEvictionRunsMillis;
        private String schema;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public Integer getInitialSize() {
            return initialSize;
        }

        public void setInitialSize(Integer initialSize) {
            this.initialSize = initialSize;
        }

        public Integer getMaxActive() {
            return maxActive;
        }

        public void setMaxActive(Integer maxActive) {
            this.maxActive = maxActive;
        }

        public Integer getMinIdle() {
            return minIdle;
        }

        public void setMinIdle(Integer minIdle) {
            this.minIdle = minIdle;
        }

        public Integer getMaxIdle() {
            return maxIdle;
        }

        public void setMaxIdle(Integer maxIdle) {
            this.maxIdle = maxIdle;
        }

        public Long getMaxWait() {
            return maxWait;
        }

        public void setMaxWait(Long maxWait) {
            this.maxWait = maxWait;
        }

        public String getValidationQuery() {
            return validationQuery;
        }

        public void setValidationQuery(String validationQuery) {
            this.validationQuery = validationQuery;
        }

        public Boolean getTestOnBorrow() {
            return testOnBorrow;
        }

        public void setTestOnBorrow(Boolean testOnBorrow) {
            this.testOnBorrow = testOnBorrow;
        }

        public Boolean getTestOnReturn() {
            return testOnReturn;
        }

        public void setTestOnReturn(Boolean testOnReturn) {
            this.testOnReturn = testOnReturn;
        }

        public Boolean getTestWhileIdle() {
            return testWhileIdle;
        }

        public void setTestWhileIdle(Boolean testWhileIdle) {
            this.testWhileIdle = testWhileIdle;
        }

        public Long getTimeBetweenEvictionRunsMillis() {
            return timeBetweenEvictionRunsMillis;
        }

        public void setTimeBetweenEvictionRunsMillis(Long timeBetweenEvictionRunsMillis) {
            this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
        }

        public String getSchema() {
            return schema;
        }

        public void setSchema(String schema) {
            this.schema = schema;
        }

        @Override
        public String toString() {
            return "ConnectionInfo{" +
                    "name='" + name + '\'' +
                    ", url='" + url + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", driverClassName='" + driverClassName + '\'' +
                    ", initialSize=" + initialSize +
                    ", maxActive=" + maxActive +
                    ", minIdle=" + minIdle +
                    ", maxIdle=" + maxIdle +
                    ", maxWait=" + maxWait +
                    ", validationQuery='" + validationQuery + '\'' +
                    ", testOnBorrow=" + testOnBorrow +
                    ", testOnReturn=" + testOnReturn +
                    ", testWhileIdle=" + testWhileIdle +
                    ", timeBetweenEvictionRunsMillis=" + timeBetweenEvictionRunsMillis +
                    ", schema='" + schema + '\'' +
                    '}';
        }
    }

    public List<ConnectionInfo> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "MultiDbConnectionConfig{" +
                "list=" + list +
                '}';
    }
}
