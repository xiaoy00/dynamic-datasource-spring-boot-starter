package com.github.xiaoy.aop;

import com.github.xiaoy.DataSource;
import lombok.NonNull;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author TaoYu
 * @since 1.2.0
 */
public class DynamicDataSourceAnnotationAdvisor extends AbstractPointcutAdvisor implements
        BeanFactoryAware {

  private Advice advice;

  private Pointcut pointcut;

  public DynamicDataSourceAnnotationAdvisor(
      @NonNull DynamicDataSourceAnnotationInterceptor dynamicDataSourceAnnotationInterceptor) {
    this.advice = dynamicDataSourceAnnotationInterceptor;
    this.pointcut = buildPointcut();
  }

  @Override
  public Pointcut getPointcut() {
    return this.pointcut;
  }

  @Override
  public Advice getAdvice() {
    return this.advice;
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    if (this.advice instanceof BeanFactoryAware) {
      ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
    }
  }

  private Pointcut buildPointcut() {
    Pointcut cpc = new AnnotationMatchingPointcut(DataSource.class, true);
    Pointcut mpc = AnnotationMatchingPointcut.forMethodAnnotation(DataSource.class);
    return new ComposablePointcut(cpc).union(mpc);
  }
}
