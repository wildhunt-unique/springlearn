package cn.mine.learning.spring.context.annotation.impl;

import cn.mine.learning.spring.context.annotation.Provider;
import cn.mine.learning.spring.context.annotation.api.Engine;
import cn.mine.learning.spring.core.BeanInitListener;

/**
 * @author 丁星（镜月）
 * @since 2021-03-29
 */
@Provider(name = "SteamEngine")
public class SteamEngine implements Engine, BeanInitListener {
    private String name;

    @Override
    public void fire() {
        System.out.println(name + " wu wu wu ~~");
    }

    @Override
    public void init() {
        this.name = "Init SteamEngine";
    }
}
