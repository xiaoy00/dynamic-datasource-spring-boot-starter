package com.github.xiaoy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 当前数据源routingKey上下文
 * @author songxiaoyue
 */
public class DataSourceRoutingKeyHolder {

    private static Logger logger = LoggerFactory.getLogger(DataSourceRoutingKeyHolder.class);

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    private DataSourceRoutingKeyHolder(){

    }

    public static void set(String routingKey) {
        logger.debug("set datasource routing key:{}", routingKey);
        contextHolder.set(routingKey);
    }

    public static String get() {
        logger.debug("get datasource routing key:{}", contextHolder.get());
        return contextHolder.get();
    }

    public static void clear() {
        logger.debug("clear datasource routing key:{}", contextHolder.get());
        contextHolder.remove();
    }

}
