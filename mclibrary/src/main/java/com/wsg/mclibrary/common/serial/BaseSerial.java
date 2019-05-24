package com.wsg.mclibrary.common.serial;

import com.wsg.mclibrary.common.CommonConstants;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android_serialport_api.SerialPortFinder;

/**
 * 串口基类
 *
 * @author WuSG
 */
public abstract class BaseSerial extends Thread {
    protected String[] serialDevices;
    /**
     * 串口配置
     */
    protected SerialConfig serialConfig;

    protected ThreadPoolExecutor poolExecutor;

    @Override
    public void run() {
        super.run();
        serialConfig = onSerialConfig();
        if (serialConfig == null) {
            serialError(102, SerialError.getCodeErrorInfo(102));
            return;
        }
        CommonConstants.serialConfig = serialConfig;
        if (checkSerial()) {
            return;
        }
        poolExecutor = new ThreadPoolExecutor(10, 15, 30, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>(), new SerialPortThreadFactory());

    }


    protected void serialError(int code, String errorMsg) {
        if (serialConfig.getSerialListener() != null) {
            serialConfig.getSerialListener().onSerialError(code, errorMsg);
        }
    }

    /**
     * 用于添加线程
     */
    protected abstract void onSubmitRunnable();

    /**
     * 串口配置加载
     *
     * @return
     */
    protected abstract SerialConfig onSerialConfig();

    /**
     * 检查设备是否包含串口
     */
    private boolean checkSerial() {
        serialDevices = SerialPortFinder.getInstance().getAllDevices();
        if (serialDevices.length <= 0) {
            serialError(105, SerialError.getCodeErrorInfo(105));
            return true;
        }
        return false;
    }
}
