package cn.mine.learning.spring.context.annotation.impl;

import cn.mine.learning.spring.context.annotation.Provider;
import cn.mine.learning.spring.context.annotation.Reference;

/**
 * @author 丁星（镜月）
 * @since 2021-04-01
 */
@Provider
public class ClassA {
    @Reference
    private ClassB classB;
}
