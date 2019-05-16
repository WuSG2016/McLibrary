package com.wsg.mclibrary.common.serial;

/**
 * 串口监听接口
 * @author WuSG
 */
public interface ISerialListener {
    /**
     * 串口加载完成
     *
     * @param openCode
     */
    void onSerialInitComplete(int openCode);


    /**
     * 串口错误信息
     *
     * @param code
     * @param errorMsg
     */
    void onSerialError(int code, String errorMsg);

}
