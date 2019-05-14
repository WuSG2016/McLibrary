package com.wsg.mclibrary.common.serial;

public enum SerialError {
    /**
     * 线程异常
     */
    RUNNABLE_ERROR(100, "runnable_error"),
    /**
     * 串口打开出错
     */
    SERIAL_OPEN_ERROR(101, "serial_open_error");

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
