package com.wsg.mclibrary.common.serial;

public enum SerialError {
    /**
     * 线程异常
     */
    RUNNABLE_ERROR_SEND(100, "send_runnable_error"),
    RUNNABLE_ERROR_RECEIVER(101, "receiver_runnable_error"),
    /**
     * 串口打开出错
     */
    SERIAL_OPEN_ERROR(102, "serial_open_error"),
    /**
     * 配置出错
     */
    CONFIGURATION_ERROR_PORT(103, "configuration_error_port_baud_rate"),
    /**
     * XML文件出错
     */
    CONFIGURATION_ERROR_XML(104, "configuration_error_XML"),
    /**
     * 串口设备未找到
     */
    SERIAL_DEVICE_ERROR(105, "serial_device_error");
    private int code;
    private String errorInfo;

    SerialError(int code, String errorInfo) {
        this.code = code;
        this.errorInfo = errorInfo;
    }

    public static String getCodeErrorInfo(int code) {
        for (SerialError command : SerialError.values()) {
            if (command.code == code) {
                return command.errorInfo;
            }
        }
        return "未知指令：[" + code + "]";
    }

    public static SerialError getCode(int code) {
        for (SerialError command : SerialError.values()) {
            if (command.code == code) {
                return command;
            }
        }
        return null;
    }

}
