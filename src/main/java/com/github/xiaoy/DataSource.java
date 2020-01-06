package com.github.xiaoy;

import java.lang.annotation.*;

/**
 * 数据源RoutingKey指定注解
 * @author songxiaoyue
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String value();
}
