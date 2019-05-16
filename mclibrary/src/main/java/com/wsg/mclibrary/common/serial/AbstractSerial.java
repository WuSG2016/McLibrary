package com.wsg.mclibrary.common.serial;


import android.text.TextUtils;
import android.util.Log;

import com.wsg.mclibrary.common.CommonConstants;
import com.wsg.mclibrary.common.config.ParseSerialFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

/**
 * @author WuSG
 */
public abstract class AbstractSerial extends Thread {
    /**
     * 波特率
     */
    private static int SERIAL_BAUD_RATE;


    private SerialMonitorRunnable serialMonitorRunnable;

    private SendMsgRunnable sendMsgRunnable;
    protected BufferedInputStream mInputStream;

    /**
     * 串口
     */
    private static String PORT;


    private SerialPort serialPort;

    private String[] serialDevices;
    private boolean isOpen;
    public OutputStream mOutputStream;

    public SerialPort getSerialPort() {
        return serialPort;
    }


    protected ThreadPoolExecutor poolExecutor;
    /**
     * 串口配置
     */
    private SerialConfig serialConfig;

    @Override
    public void run() {
        try {
            if (checkSerial()) {
                return;
            }
            initSerial();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * 检查设备是否包含串口
     */
    private boolean checkSerial() {
        serialDevices = SerialPortFinder.getInstance().getAllDevices();
        Log.e("checkSerial: ", Arrays.toString(serialDevices));
        if (serialDevices.length <= 0) {
            serialError(105, SerialError.getCodeErrorInfo(105));
            return true;
        }
        return false;
    }

    private void initSerial() throws Throwable {
        init();
        connectSerialPort();
    }

    private void init() throws Throwable {
        serialConfig = onSerialConfig();
        if (serialConfig == null) {
            serialError(102, SerialError.getCodeErrorInfo(102));
        } else {
            if (!TextUtils.isEmpty(serialConfig.getSerialXmlPath())) {
                initSerialXml();
            } else if (serialConfig.getPort() != null && serialConfig.getBaudRate() > 0) {
                PORT = serialConfig.getPort();
                SERIAL_BAUD_RATE = serialConfig.getBaudRate();
            } else {
                serialError(103, SerialError.getCodeErrorInfo(102));
                return;
            }
            initAndroidOfficial();

        }

    }

    /**
     * Android官方串口
     */
    private void initAndroidOfficial() {

        try {
            if (isOpen) {
                closeSerialPort();
                isOpen = false;
            }
            serialPort = new SerialPort(new File(PORT), SERIAL_BAUD_RATE, 0);
            mInputStream = new BufferedInputStream(serialPort.getInputStream());
            mOutputStream = serialPort.getOutputStream();
            isOpen = true;
            if (serialConfig.getSerialListener() != null) {
                serialConfig.getSerialListener().onSerialInitComplete(0);
            }
        } catch (IOException e) {
            serialError(102, SerialError.getCodeErrorInfo(102));
            isOpen = false;
            e.printStackTrace();
        }
    }

    private void serialError(int code, String errorMsg) {
        if (serialConfig.getSerialListener() != null) {
            serialConfig.getSerialListener().onSerialError(code, errorMsg);
        }
    }


    /**
     * 串口配置加载
     *
     * @return
     */
    protected abstract SerialConfig onSerialConfig();

    /**
     * 加载XML串口配置文件
     */
    private void initSerialXml() {
        if (TextUtils.isEmpty(serialConfig.getSerialXmlPath())) {
            serialError(104, SerialError.getCodeErrorInfo(104));
            return;
        }
        Boolean isComplete = ParseSerialFile.parseXml(serialConfig.getSerialXmlPath(), "serial.xml");
        if (isComplete) {
            SERIAL_BAUD_RATE = Integer.parseInt(ParseSerialFile.baudrate);
            PORT = ParseSerialFile.port;
        }
    }

    @Override
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh) {
        super.setUncaughtExceptionHandler(eh);
        closeSerialPort();
    }

    /**
     * 连接到串口
     */
    private void connectSerialPort() {
        serialMonitorRunnable = new SerialMonitorRunnable();
        sendMsgRunnable = new SendMsgRunnable();
        poolExecutor = new ThreadPoolExecutor(10, 15, 30, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>(), new SerialPortThreadFactory());
        poolExecutor.submit(serialMonitorRunnable);
        poolExecutor.submit(sendMsgRunnable);
        onSubmitRunnable();

    }

    /**
     * 用于添加线程
     */
    protected abstract void onSubmitRunnable();


    /**
     * 发送数据给串口
     */
    public class SendMsgRunnable implements Runnable {

        @Override
        public void run() {
            try {
                while (onTerminationSendRunnable()) {
                    onSendMessage();
                }
            } catch (Exception e) {
                if (poolExecutor != null) {
                    poolExecutor.submit(sendMsgRunnable);
                }
            }

        }
    }

    /**
     * 终止线程
     *
     * @return
     */
    protected abstract boolean onTerminationSendRunnable();

    /**
     * 终止接收线程
     *
     * @return
     */
    protected abstract boolean onTerminationReceiveRunnable();

    /**
     * 发送处理
     */
    protected abstract void onSendMessage();

    /**
     * 关闭串口
     */
    private void closeSerialPort() {
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (serialPort != null) {
            serialPort.close();
        }
        if (poolExecutor != null) {
            poolExecutor.shutdown();
        }
    }


    /**
     * 监听串口数据线程
     */
    private class SerialMonitorRunnable implements Runnable {
        @Override
        public void run() {
            try {
                while (onTerminationReceiveRunnable()) {
                    onReceiverSerialData();
                }
            } catch (Exception e) {
                if (poolExecutor != null) {
                    poolExecutor.submit(serialMonitorRunnable);
                }
            }


        }

    }

    /**
     * 接收处理
     */
    protected abstract void onReceiverSerialData();


}
