package com.wsg.mclibrary.common.serial;

/**
 * @author WuSG
 */
public class SerialConfig {
    /**
     * 串口工具
     */
    public static final int CUSTOM_SERIAL = 1001;
    /**
     * 串口监听器
     */
    private ISerialListener serialListener;
    /**
     * 串口文件的XML路径
     */
    private String serialXmlPath;

    /**
     * 端口
     */
    private String port;

    /**
     * 波特率
     */
    private int baudRate;

    /**
     * 串口加载的类型
     * 1000 Android官方
     * 1001串口工具
     */
    private int serialInitType;

    public SerialConfig(Builder builder) {
        this.baudRate = builder.baudRate;
        this.port = builder.port;
        this.serialListener = builder.serialListener;
        this.serialInitType = builder.serialInitType;
        this.serialXmlPath = builder.serialXmlPath;
    }

    public ISerialListener getSerialListener() {
        return serialListener;
    }

    public String getSerialXmlPath() {
        return serialXmlPath;
    }

    public String getPort() {
        return port;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public int getSerialInitType() {
        return serialInitType;
    }

    public static class Builder {
        /**
         * 串口监听器
         */
        private ISerialListener serialListener;
        /**
         * 串口文件的XML路径
         */
        private String serialXmlPath;

        /**
         * 端口
         */
        private String port;

        /**
         * 波特率
         */
        private int baudRate;

        /**
         * 串口加载的类型
         * 1000 Android官方
         * 1001串口工具
         */
        private int serialInitType;

        public Builder setSerialListener(ISerialListener serialListener) {
            this.serialListener = serialListener;
            return this;
        }

        public Builder setSerialXmlPath(String serialXmlPath) {
            this.serialXmlPath = serialXmlPath;
            return this;
        }

        public Builder setPort(String port) {
            this.port = port;
            return this;
        }

        public Builder setBaudRate(int baudRate) {
            this.baudRate = baudRate;
            return this;
        }

        public Builder setSerialInitType(int serialInitType) {
            this.serialInitType = serialInitType;
            return this;
        }

        public SerialConfig builder() {
            return new SerialConfig(this);
        }
    }


}
