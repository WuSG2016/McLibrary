package com.wsg.mclibrary.common;

/**机器工厂
 * @author WuSG
 */
public class MachineFactory extends AbstractMachineFactory {
    private MachineFactory() {
    }

    public static MachineFactory getInstance() {
        return MachineFactoryHolder.MACHINE_FACTORY;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends AbstractMachine> T createMachine(Class<T> tClass) {
        AbstractMachine abstractMachine = null;
        String className = tClass.getName();
        try {
            abstractMachine = (AbstractMachine) Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (T) abstractMachine;
    }
    @SuppressWarnings("unchecked")
    @Override
    public <T extends AbstractTreaty> T createTreaty(Class<T> tClass) {
        AbstractTreaty abstractTreaty = null;
        String className = tClass.getName();
        try {
            abstractTreaty = (AbstractTreaty) Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (T) abstractTreaty;
    }

    private static class MachineFactoryHolder {
        private final static MachineFactory MACHINE_FACTORY = new MachineFactory();
    }
}
