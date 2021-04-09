package cn.mine.learning.spring.context.annotation.impl;

import cn.mine.learning.spring.context.annotation.Provider;
import cn.mine.learning.spring.context.annotation.api.Wheel;

/**
 * @author 丁星（镜月）
 * @since 2021-03-29
 */
@Provider(isPrimary = true)
public class PlasticWheel implements Wheel {
    @Override
    public void run() {
        System.out.println("Plastic roll roll roll～～～");
    }
}
