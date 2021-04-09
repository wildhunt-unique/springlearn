package cn.mine.learning.spring.context.annotation;


import cn.mine.learning.spring.context.annotation.impl.EngineProvider;

/**
 * @author 丁星（镜月）
 * @since 2021-03-29
 */
public class AnnotationApplicationContextTest {
    public static void main(String[] args) {
        AnnotationApplicationContext context = new AnnotationApplicationContext("cn.mine.learning.spring.context.annotation");
        EngineProvider provider = context.getBean(EngineProvider.class);
        provider.fire("RollsRoyceEngine");
    }
}
