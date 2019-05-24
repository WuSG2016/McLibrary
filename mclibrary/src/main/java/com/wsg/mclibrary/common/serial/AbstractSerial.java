package com.wsg.mclibrary.common.serial;

import android.text.TextUtils;

import com.wsg.mclibrary.common.config.ParseSerialFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


import android_serialport_api.SerialPort;


/**
 * @author WuSG
 */
public abstract class AbstractSerial extends BaseSerial {
    private SerialMonitorRunnable serialMonitorRunnable;
    private SendMsgRunnable sendMsgRunnable;
    /**
     * 波特率
     */
    private static int SERIAL_BAUD_RATE;
    /**
     * 串口
     */
    private static String PORT;
    private SerialPort serialPort;
    private boolean isOpen;
    protected OutputStream mOutputStream;
    protected BufferedInputStream mInputStream;



    @Override
    public void run() {
        super.run();
        try {
            initAndroidOfficial();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


    /**
     * Android官方串口
     */
    private void initAndroidOfficial() {
        if (!TextUtils.isEmpty(serialConfig.getSerialXmlPath())) {
            initSerialXml();
        } else if (serialConfig.getPort() != null && serialConfig.getBaudRate() > 0) {
            PORT = serialConfig.getPort();
            SERIAL_BAUD_RATE = serialConfig.getBaudRate();
        } else {
            serialError(103, SerialError.getCodeErrorInfo(102));
            return;
        }
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
            initRunnable();
        } catch (IOException e) {
            serialError(102, SerialError.getCodeErrorInfo(102));
            isOpen = false;
            e.printStackTrace();
        }
    }


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
     * 初始化线程
     */
    private void initRunnable() {
        serialMonitorRunnable = new SerialMonitorRunnable();
        sendMsgRunnable = new SendMsgRunnable();
        poolExecutor.submit(serialMonitorRunnable);
        poolExecutor.submit(sendMsgRunnable);
        onSubmitRunnable();

    }

    /**
     * 发送数据给串口
     */
    public class SendMsgRunnable implements Runnable {

        @Override
        public void run() {
            try {
                while (!onTerminationSend()) {
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
     * 是否终止发送线程
     *
     * @return
     */
    protected abstract boolean onTerminationSend();

    /**
     * 是否终止接收线程
     *
     * @return
     */
    protected abstract boolean onTerminationReceive();

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
                while (!onTerminationReceive()) {
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
