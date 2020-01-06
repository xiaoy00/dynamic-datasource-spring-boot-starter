package com.github.xiaoy.aop;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * @author songxiaoyue
 */
@Slf4j
public class DynamicDataSourceClassResolver {

  private static boolean mpEnabled = false;

  private static Field mapperInterfaceField;

  static {
    Class<?> proxyClass = null;
    try {
      proxyClass = Class.forName("org.apache.ibatis.binding.MapperProxy");
    } catch (ClassNotFoundException e3) {

    }
    if (proxyClass != null) {
      try {
        mapperInterfaceField = proxyClass.getDeclaredField("mapperInterface");
        mapperInterfaceField.setAccessible(true);
        mpEnabled = true;
      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
  }

  public Class<?> targetClass(MethodInvocation invocation) throws IllegalAccessException {
    if (mpEnabled) {
      Object target = invocation.getThis();
      Class<?> targetClass = target.getClass();
      return Proxy.isProxyClass(targetClass) ? (Class) mapperInterfaceField
          .get(Proxy.getInvocationHandler(target)) : targetClass;
    }
    return invocation.getMethod().getDeclaringClass();
  }
}
