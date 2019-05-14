package com.wsg.mclibrary.common;

/**
 * 抽象机器工厂 用于生产协议和机器
 * @author WuSG
 */
public abstract class AbstractMachineFactory {
    /**
     * 创建机器
     * @param <T>
     * @return
     */
    public abstract  <T extends AbstractMachine> T createMachine(Class<T> tClass);

    /**
     * 创建具体协议
     * @param <T>
     * @return
     */
    public abstract  <T extends AbstractTreaty> T createTreaty(Class<T> tClass);

}
