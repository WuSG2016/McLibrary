package com.wsg.mclibrary.common.serial;

import android.content.Context;

/**
 * 默认加载官方串口
 *
 * @author WuSG
 */
public class SerialConfig {

    /**
     * 串口监听器
     */
    private ISerialListener serialListener;
    /**
     * 串口文件的XML路径
     */
    private String serialXmlPath = "";

    /**
     * 端口
     */
    private String port;
    /**
     * su路径
     */
    private String suPath;

    /**
     * 波特率
     */
    private int baudRate;
    /**
     * 绑定binder的权限
     */
    private String permission;
    private Context context;
    /**
     * USB的设备信息 对应driver_XML文件
     */
    private int vendorId;
    private int productId;

    /**
     * 数据位
     */
    private int dataBits;
    /**
     * 停止位
     */
    private int stopBits;
    /**
     * 校验位
     */
    private int parity;

    public Context getContext() {
        return context;
    }

    public String getPermission() {
        return permission;
    }

    public int getDataBits() {
        return dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public int getParity() {
        return parity;
    }

    public String getSuPath() {
        return suPath;
    }

    public int getVendorId() {
        return vendorId;
    }

    public int getProductId() {
        return productId;
    }

    public SerialConfig(Builder builder) {
        this.baudRate = builder.baudRate;
        this.port = builder.port;
        this.serialListener = builder.serialListener;
        this.serialXmlPath = builder.serialXmlPath;
        this.permission = builder.permission;
        this.context = builder.context;
        this.stopBits = builder.stopBits;
        this.dataBits = builder.dataBits;
        this.parity = builder.parity;
        this.vendorId = builder.vendorId;
        this.productId = builder.productId;
        this.suPath = builder.suPath;
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


    public static class Builder {
        /**
         * 串口监听器
         */
        private ISerialListener serialListener;
        /**
         * 串口文件的XML路径
         */
        private String serialXmlPath = "";

        /**
         * 端口
         */
        private String port;
        /**
         * su路径
         */
        private String suPath;
        /**
         * 波特率
         */
        private int baudRate;
        /**
         * 绑定binder的权限
         */
        private String permission;


        private Context context;
        /**
         * 数据位
         */
        private int dataBits;
        /**
         * 停止位
         */
        private int stopBits;
        /**
         * 校验位
         */
        private int parity;
        private int vendorId;
        private int productId;

        public Builder setVendorId(int vendorId) {
            this.vendorId = vendorId;
            return this;
        }

        public Builder setProductId(int productId) {
            this.productId = productId;
            return this;
        }

        public Builder setSuPath(String suPath) {
            this.suPath = suPath;
            return this;
        }

        public Builder setDataBits(int dataBits) {
            this.dataBits = dataBits;
            return this;
        }

        public Builder setStopBits(int stopBits) {
            this.stopBits = stopBits;
            return this;
        }

        public Builder setParity(int parity) {
            this.parity = parity;
            return this;
        }


        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setSerialListener(ISerialListener serialListener) {
            this.serialListener = serialListener;
            return this;
        }

        public Builder setSerialXmlPath(String serialXmlPath) {
            this.serialXmlPath = serialXmlPath;
            return this;
        }

        public Builder setPermission(String permission) {
            this.permission = permission;
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


        public SerialConfig builder() {
            return new SerialConfig(this);
        }
    }


}
