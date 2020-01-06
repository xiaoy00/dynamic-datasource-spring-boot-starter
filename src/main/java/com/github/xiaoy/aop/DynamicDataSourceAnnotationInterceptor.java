package com.github.xiaoy.aop;


import com.github.xiaoy.DataSource;
import com.github.xiaoy.DataSourceRoutingKeyHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * Core Interceptor of Dynamic Datasource
 * @author songxiaoyue
 */
public class DynamicDataSourceAnnotationInterceptor implements MethodInterceptor {

  private static final DynamicDataSourceClassResolver RESOLVER = new DynamicDataSourceClassResolver();
  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    try {
      DataSourceRoutingKeyHolder.set(determineDatasource(invocation));
      return invocation.proceed();
    } finally {
      DataSourceRoutingKeyHolder.clear();
    }
  }

  private String determineDatasource(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    DataSource ds = method.isAnnotationPresent(DataSource.class)
        ? method.getAnnotation(DataSource.class)
        : AnnotationUtils.findAnnotation(RESOLVER.targetClass(invocation), DataSource.class);
    String key = ds.value();
    return key;
  }
}