package cn.mine.learning.spring.context.annotation.impl;

import cn.mine.learning.spring.context.annotation.Provider;
import cn.mine.learning.spring.context.annotation.api.Engine;
import cn.mine.learning.spring.core.BeanFactory;
import cn.mine.learning.spring.core.FactoryRefreshedListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 丁星（镜月）
 * @since 2021-04-01
 */
@Provider
public class EngineProvider implements FactoryRefreshedListener {
    private Map<String, Engine> nameToEngine;

    public void fire(String engineName) {
        Engine engine = nameToEngine.get(engineName);
        if (null != engine) {
            engine.fire();
        }
    }

    @Override
    public void afterRefreshed(BeanFactory beanFactory) {
        List<Engine> engines = beanFactory.listBeanByClass(Engine.class);
        nameToEngine = new HashMap<>();
        engines.forEach(e -> nameToEngine.put(e.getClass().getSimpleName(), e));
    }
}
