package cn.mine.learning.spring.core;

import java.util.Objects;

/**
 * @author 丁星（镜月）
 * @since 2021-03-28
 */
public class BeanDependsOn {
    /**
     * 字段名
     */
    private String filedName;

    /**
     * reference的名字
     */
    private String referenceName;

    /**
     * 字段类型
     */
    private String filedType;

    /**
     * 通过setter方法构造
     */
    private String setterMethod;

    public String getFiledName() {
        return filedName;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getFiledType() {
        return filedType;
    }

    public void setFiledType(String filedType) {
        this.filedType = filedType;
    }

    public String getSetterMethod() {
        return setterMethod;
    }

    public void setSetterMethod(String setterMethod) {
        this.setterMethod = setterMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BeanDependsOn dependsOn = (BeanDependsOn) o;
        return filedName.equals(dependsOn.filedName) &&
                referenceName.equals(dependsOn.referenceName) &&
                filedType.equals(dependsOn.filedType) &&
                setterMethod.equals(dependsOn.setterMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filedName, referenceName, filedType, setterMethod);
    }
}
