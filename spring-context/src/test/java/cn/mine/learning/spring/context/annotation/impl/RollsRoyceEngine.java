package cn.mine.learning.spring.context.annotation.impl;

import cn.mine.learning.spring.context.annotation.Provider;
import cn.mine.learning.spring.context.annotation.api.Engine;

/**
 * @author 丁星（镜月）
 * @since 2021-03-29
 */
@Provider(isPrimary = true)
public class RollsRoyceEngine implements Engine {
    @Override
    public void fire() {
        System.out.println("Rolls Royce: wu wu wu !!!");
    }
}
