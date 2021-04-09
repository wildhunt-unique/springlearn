package cn.mine.learning.spring.core;

import java.util.List;

/**
 * @author 丁星（镜月）
 * @since 2021-03-28
 */
@SuppressWarnings("unused")
public interface BeanFactory {
    /**
     * Get bean from factory
     *
     * @param beanName  Name of bean
     * @param beanClass Class
     * @param <T>       Class of bean
     * @return Provider
     */
    <T> T getBean(String beanName, Class<T> beanClass);

    /**
     * Get bean from factory
     *
     * @param beanClass class
     * @param <T>       Class of bean
     * @return Provider
     */
    <T> T getBean(Class<T> beanClass);

    /**
     * List bean of this type
     *
     * @param beanClass Class of bean
     * @param <T>       Class
     * @return Bean list
     */
    <T> List<T> listBeanByClass(Class<T> beanClass);

    /**
     * List bean of this type
     *
     * @param beanClass Class of bean
     * @param <T>       Class
     * @return Bean list
     */
    <T> List<T> listBeanByAnnotationType(Class<?> beanClass);

    /**
     * Get bean from factory
     *
     * @param beanName Name of bean
     * @return Provider
     */
    Object getBean(String beanName);
}