package cn.mine.learning.spring.context.annotation.impl;

import cn.mine.learning.spring.context.annotation.Provider;
import cn.mine.learning.spring.context.annotation.Reference;
import cn.mine.learning.spring.context.annotation.api.Car;
import cn.mine.learning.spring.context.annotation.api.Engine;
import cn.mine.learning.spring.context.annotation.api.Wheel;

/**
 * @author 丁星（镜月）
 * @since 2021-03-29
 */
@Provider
public class AudiCar implements Car {
    @Reference
    private Engine engine;

    @Reference
    private Wheel wheel;

    @Override
    public void start() {
        System.out.println("Audi car start...");
        this.engine.fire();
        this.wheel.run();
    }
}
