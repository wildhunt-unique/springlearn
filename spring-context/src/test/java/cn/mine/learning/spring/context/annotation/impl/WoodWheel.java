package cn.mine.learning.spring.context.annotation.impl;

import cn.mine.learning.spring.context.annotation.Provider;
import cn.mine.learning.spring.context.annotation.api.Wheel;

/**
 * @author 丁星（镜月）
 * @since 2021-03-29
 */
@Provider(name = "WoodWheel")
public class WoodWheel implements Wheel {
    @Override
    public void run() {
        System.out.println("Wood roll roll roll～～～");
    }
}
