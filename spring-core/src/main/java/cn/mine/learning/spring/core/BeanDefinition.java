package cn.mine.learning.spring.core;

import java.util.List;

/**
 * @author 丁星（镜月）
 * @since 2021-03-28
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BeanDefinition {
    /**
     * Bean所对应的Class对象，与beanClassName互不为空
     */
    private Class beanClass;

    /**
     * Bean所对应的Class全限定名，与beanClass互不为空
     */
    private String beanClassName;

    /**
     * Bean所设定的名，可以为空，如果为空，则为Class的全限定名
     */
    private String name;

    /**
     * 所依赖的字段
     */
    private List<BeanDependsOn> dependsOn;

    /**
     * 除了Object以外的父类
     */
    private List<String> parent;

    /**
     * 带的注解
     */
    private List<String> annotation;

    /**
     * 是否单例
     */
    private Boolean isSingleton;

    /**
     * 是否主要的
     */
    private Boolean isPrimary;

    /**
     * 0 未创建，1创建中，2创建完成
     */
    private int status = 0;


    public static final int INIT = 0;
    public static final int PENDING_ING = 1;
    public static final int COMPLETED = 2;

    public void pending() {
        status = PENDING_ING;
    }

    public void completed() {
        status = COMPLETED;
    }

    public boolean isPending() {
        return PENDING_ING == status;
    }

    public boolean isCompleted() {
        return COMPLETED == status;
    }

    public List<String> getAnnotation() {
        return annotation;
    }

    public void setAnnotation(List<String> annotation) {
        this.annotation = annotation;
    }

    public Boolean getSingleton() {
        return isSingleton;
    }

    public void setSingleton(Boolean singleton) {
        isSingleton = singleton;
    }

    public Boolean getPrimary() {
        return isPrimary;
    }

    public void setPrimary(Boolean primary) {
        isPrimary = primary;
    }

    public List<String> getParent() {
        return parent;
    }

    public void setParent(List<String> parent) {
        this.parent = parent;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public String getName() {
        return name != null ? name :
                beanClassName != null ? beanClassName :
                        beanClass.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BeanDependsOn> getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(List<BeanDependsOn> dependsOn) {
        this.dependsOn = dependsOn;
    }
}
