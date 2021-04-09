package cn.mine.learning.spring.core;

import cn.mine.learning.spring.core.util.MultipleMap;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 丁星（镜月）
 * @since 2021-03-28
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class AbstractBeanFactory implements BeanFactory {
    private final Object REFRESH_LOCK = new Object();
    private final Map<String, Object> singleBeanInstance;
    private final Map<String, BeanDefinition> nameToBeanDef;
    private final MultipleMap<String, BeanDefinition> typeToBeanDef;
    private final MultipleMap<String, BeanDefinition> annotationToBeanDef;

    public AbstractBeanFactory() {
        singleBeanInstance = new HashMap<>(128);
        nameToBeanDef = new HashMap<>(128);
        typeToBeanDef = MultipleMap.create();
        annotationToBeanDef = MultipleMap.create();
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return this.doGetBean(null, clazz);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> clazz) {
        return this.doGetBean(beanName, clazz);
    }

    @Override
    public Object getBean(String beanName) {
        return this.doGetBean(beanName, null);
    }

    @Override
    public <T> List<T> listBeanByClass(Class<T> beanClass) {
        if (null == beanClass) {
            throw new NullPointerException();
        }

        List<BeanDefinition> beanDefinitions = this.typeToBeanDef.find(beanClass.getName());
        if (null == beanDefinitions || beanDefinitions.isEmpty()) {
            return Collections.emptyList();
        }

        List<T> beanList = new ArrayList<>(beanDefinitions.size());
        beanDefinitions.forEach(beanDefinition -> {
            T t = this.doGetBean(beanDefinition.getName(), beanClass);
            if (null != t) {
                beanList.add(t);
            }
        });

        return beanList;
    }

    @Override
    public <T> List<T> listBeanByAnnotationType(Class<?> beanClass) {
        if (null == beanClass) {
            throw new NullPointerException();
        }

        List<BeanDefinition> beanDefinitions = this.annotationToBeanDef.find(beanClass.getName());
        if (null == beanDefinitions || beanDefinitions.isEmpty()) {
            return Collections.emptyList();
        }

        List<T> beanList = new ArrayList<>(beanDefinitions.size());
        beanDefinitions.forEach(beanDefinition -> {
            T t = this.doGetBean(beanDefinition.getName(), null);
            if (null != t) {
                beanList.add(t);
            }
        });

        return beanList;
    }

    protected void refresh() {
        synchronized (REFRESH_LOCK) {
            // 加载，并解析BeanDef
            this.parserBeanDefinition(this.loadBeanDefinition());

            // 提前初始化单例Bean
            this.initSingletonBean();

            // 初始化完成回调
            this.refreshedCallback();
        }
    }


    /**
     * loadBeanDefinition
     *
     * @return Map<String, BeanDefinition>
     */
    protected abstract List<BeanDefinition> loadBeanDefinition();

    protected void refreshedCallback() {
        // FactoryRefreshedListener回调
        this.singleBeanInstance.values().forEach(singleBean -> {
            if (singleBean instanceof FactoryRefreshedListener) {
                ((FactoryRefreshedListener) singleBean).afterRefreshed(this);
            }
        });
    }

    protected void initSingletonBean() {
        this.nameToBeanDef.values().forEach(beanDefinition -> {
            if (beanDefinition.getSingleton()) {
                this.doGetBean(beanDefinition.getName(), null);
            }
        });
    }


    @SuppressWarnings("unchecked")
    private <T> T doGetBean(String beanName, Class<T> beanClass) {
        Object bean;
        // 尝试从已有的单例Bean获取
        if (null != beanName && !beanName.isEmpty() && (bean = singleBeanInstance.get(beanName)) != null) {
            return (T) bean;
        }

        // 根据name或者type找到BeanDef
        BeanDefinition beanDefinition = this.findBeanDefinition(beanName, beanClass);
        // 如果已经加载过单例了
        if (beanDefinition.getSingleton() && beanDefinition.isCompleted()) {
            return (T) this.singleBeanInstance.get(beanDefinition.getName());
        }
        // 如果是正在创建 // TODO: 2021-04-01 优雅解决循环依赖
        if (beanDefinition.isPending()) {
            return (T) this.singleBeanInstance.get(beanDefinition.getName());
        } else {
            beanDefinition.pending();
        }

        // 根据BeanDef解析出来对应的Class
        Class<?> currentClass = this.loadClassByDef(beanDefinition, beanClass);

        // Bean实例化
        bean = this.createInstance(beanDefinition, currentClass);

        // 加入到SingleBean
        this.singleBeanInstance.put(beanName, bean);

        // 加载依赖的Bean
        Map<BeanDependsOn, Object> referenceToObject = this.loadDependsOn(beanDefinition);

        // 注入依赖的Bean
        this.parserDependsOn(bean, referenceToObject);

        // 初始化完成
        beanDefinition.completed();
        // Bean初始化之后的回调
        this.beanInitCallback(bean);

        return (T) bean;
    }

    private void beanInitCallback(Object bean) {
        // Bean初始化回调
        if (bean instanceof BeanInitListener) {
            ((BeanInitListener) bean).init();
        }
    }

    protected Class<?> loadClassByDef(BeanDefinition beanDefinition, Class<?> beanClass) {
        Class<?> currentClass;
        try {
            currentClass = beanDefinition.getBeanClass() != null ? beanDefinition.getBeanClass() :
                    Class.forName(beanDefinition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
        if (beanClass != null && !beanClass.isAssignableFrom(currentClass)) {
            throw new IllegalArgumentException("error type between:" + beanClass.getName() + ":" + currentClass.getName());
        }
        return currentClass;
    }

    protected <T> BeanDefinition findBeanDefinition(String beanName, Class<T> clazz) {
        if (null == beanName && null == clazz) {
            throw new IllegalArgumentException("beanName and class can not be all");
        }

        BeanDefinition beanDefinition = null;
        // 根据name查找
        if (beanName != null && !beanName.isEmpty()) {
            beanDefinition = nameToBeanDef.get(beanName);
        } else {
            // 根据type查找
            List<BeanDefinition> defList = typeToBeanDef.find(clazz.getName());
            if (defList != null && !defList.isEmpty()) {
                // 只有一个，直接返回
                if (defList.size() == 1) {
                    beanDefinition = defList.get(0);
                } else {
                    // 有多个，查找带primary的
                    List<BeanDefinition> primary = defList.stream().filter(BeanDefinition::getPrimary).collect(Collectors.toList());
                    // 没有primary，不知道选哪个，报错
                    if (primary.isEmpty()) {
                        throw new IllegalArgumentException("there is more than one bean def of:" + clazz.getName());
                    } else {
                        // primary有多个，不知道选哪个，报错
                        if (primary.size() > 1) {
                            throw new IllegalArgumentException("there is more than one primary bean def of:" + clazz.getName());
                        } else {
                            // primary只有一个，就它了
                            beanDefinition = primary.get(0);
                        }
                    }
                }
            }
        }

        // 没有找到
        if (null == beanDefinition) {
            throw new IllegalArgumentException("can not found bean definition: " + (clazz != null ? "class:" + clazz.getName() :
                    "name:" + beanName));
        }

        return beanDefinition;
    }

    protected void parserBeanDefinition(List<BeanDefinition> beanDefinitions) {
        if (null != beanDefinitions && !beanDefinitions.isEmpty()) {
            Map<String, List<BeanDefinition>> typeToDef = new HashMap<>(beanDefinitions.size());
            for (BeanDefinition beanDefinition : beanDefinitions) {
                String className = beanDefinition.getBeanClassName() != null ? beanDefinition.getBeanClassName() :
                        beanDefinition.getBeanClass().getName();

                // name(唯一建)到BeanDef的映射
                // TODO: 2021-03-31 检查name的唯一
                this.nameToBeanDef.put(beanDefinition.getName(), beanDefinition);

                // 类型到BeanDef的映射
                typeToBeanDef.put(className, beanDefinition);
                List<String> parentList = beanDefinition.getParent();
                if (parentList != null && !parentList.isEmpty()) {
                    parentList.forEach(parent -> typeToBeanDef.put(parent, beanDefinition));
                }

                // 注解到BeanDef的映射
                List<String> annotationList = beanDefinition.getAnnotation();
                if (null != annotationList && !annotationList.isEmpty()) {
                    annotationList.forEach(annotation -> annotationToBeanDef.put(annotation, beanDefinition));
                }
            }
        }
    }


    private void parserDependsOn(Object bean, Map<BeanDependsOn, Object> referenceToObject) {
        // 装配依赖的bean
        if (null != referenceToObject && !referenceToObject.isEmpty()) {
            for (Map.Entry<BeanDependsOn, Object> entry : referenceToObject.entrySet()) {
                BeanDependsOn beanDependsOn = entry.getKey();
                Object reference = entry.getValue();
                BeanUtil.setFieldValueByFieldName(beanDependsOn.getFiledName(), reference, bean);
            }
        }
    }

    private Object createInstance(BeanDefinition beanDefinition, Class<?> currentClass) {
        // TODO: 2021-03-31 支持构造器注入
        Object bean;
        try {
            bean = currentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        return bean;
    }

    private Map<BeanDependsOn, Object> loadDependsOn(BeanDefinition beanDefinition) {
        Map<BeanDependsOn, Object> referenceToObject = null;
        if (null != beanDefinition.getDependsOn() && !beanDefinition.getDependsOn().isEmpty()) {
            referenceToObject = new HashMap<>(beanDefinition.getDependsOn().size());
            Map<BeanDependsOn, Object> finalReferenceToObject = referenceToObject;
            beanDefinition.getDependsOn().forEach(beanDependsOn -> {
                Object reference;
                try {
                    reference = this.doGetBean(beanDependsOn.getReferenceName(), Class.forName(beanDependsOn.getFiledType()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new IllegalArgumentException("can find reference class:" + beanDependsOn.getFiledType());
                }
                finalReferenceToObject.put(beanDependsOn, reference);
            });
        }
        return referenceToObject;
    }
}
