package cn.mine.learning.spring.context.annotation;

import cn.mine.learning.spring.core.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 丁星（镜月）
 * @since 2021-03-28
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class AnnotationApplicationContext extends AbstractBeanFactory {

    private final String packagePath;

    public AnnotationApplicationContext(String packagePath) {
        if (null == packagePath || packagePath.isEmpty()) {
            throw new IllegalArgumentException("packagePath cant be null or empty");
        }
        this.packagePath = packagePath;
        refresh();
    }

    @Override
    protected List<BeanDefinition> loadBeanDefinition() {
        List<Class<?>> classList = BeanUtil.findClass(packagePath);
        List<BeanDefinition> beanDefinitions = new ArrayList<>(classList.size());

        classList.forEach(clazz -> {
            Annotation[] annotations = clazz.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Provider) {
                    beanDefinitions.add(getBeanDefinition(clazz, (Provider) annotation));
                    break;
                }
            }
        });

        return beanDefinitions;
    }

    private BeanDefinition getBeanDefinition(Class<?> clazz, Provider provider) {
        String name = provider.name();
        if (name.isEmpty()) {
            name = clazz.getName();
        }

        // 从字段获取依赖
        List<BeanDependsOn> dep = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            for (Annotation annotation : field.getAnnotations()) {
                if (annotation instanceof Reference) {
                    String referenceName = ((Reference) annotation).name();
                    BeanDependsOn dependsOn = new BeanDependsOn();
                    dependsOn.setFiledName(field.getName());
                    dependsOn.setFiledType(field.getType().getName());
                    dependsOn.setReferenceName(referenceName);
                    dep.add(dependsOn);
                }
            }
        }


        List<String> parents = new ArrayList<>(4);
        // 获取父类或者接口，排除Object
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        if (null != genericInterfaces && genericInterfaces.length > 0) {
            for (Type genericInterface : genericInterfaces) {
                parents.add(((Class) genericInterface).getName());
            }
        }

        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass != Object.class) {
            parents.add(((Class) genericSuperclass).getName());
        }

        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(clazz.getName());
        beanDefinition.setBeanClass(clazz);
        beanDefinition.setDependsOn(dep);
        beanDefinition.setSingleton(provider.isSingleton());
        beanDefinition.setPrimary(provider.isPrimary());

        beanDefinition.setName(name);
        beanDefinition.setParent(parents);

        return beanDefinition;
    }


}
