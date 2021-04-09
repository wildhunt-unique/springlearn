package cn.mine.learning.spring.core;

/**
 * @author 丁星（镜月）
 * @since 2021-03-31
 */
public interface FactoryRefreshedListener {
    void afterRefreshed(BeanFactory beanFactory);
}
