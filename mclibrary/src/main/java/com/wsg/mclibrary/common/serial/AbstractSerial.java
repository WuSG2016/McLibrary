package com.wsg.mclibrary.common.serial;


import com.mdeveloper.serialtool.serial;
import com.wsg.mclibrary.common.CommonConstants;
import com.wsg.mclibrary.common.config.ParseSerialFile;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author WuSG
 */
public abstract class AbstractSerial extends Thread {
    /**
     * 波特率
     */
    public static int SERIAL_BAUD_RATE = 9600;
    /**
     * 串口配置
     */
    private String[] serialCfg;

    private SerialMonitorRunnable serialMonitorRunnable = new SerialMonitorRunnable();

    private SendMsgRunnable sendMsgRunnable = new SendMsgRunnable();
    /**
     * 串口
     */
    public static String PORT = "ttyS3";

    public serial serialCom;


    public ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 15, 30, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>(), new SerialPortThreadFactory());


    @Override
    public void run() {
        initSerial();
    }

    private void initSerial() {
        init();
        initSerialXml();
        connectSerialPort();


    }

    private void init() {
        serialCfg = new String[]{"ttyS3", "9600", "8", "1", "None"};
        serialCom = new serial();
    }

    /**
     * 加载XML串口配置文件
     */
    private void initSerialXml() {
        Boolean isComplete = ParseSerialFile.parseXml(CommonConstants.BOOT_PATH, "serial.xml");
        if (isComplete) {
            SERIAL_BAUD_RATE = Integer.parseInt(ParseSerialFile.baudrate);
            PORT = ParseSerialFile.port;
        }
        serialCfg[0] = PORT;
        serialCfg[1] = String.valueOf(SERIAL_BAUD_RATE);
        int code = serialCom.OpenPort(serialCfg);

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
                while (true) {
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
     * 发送处理
     */
    protected abstract void onSendMessage();

    /**
     * 关闭串口
     */
    private void closeSerialPort() {
        if (serialCom != null) {
            serialCom.Close();
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
                while (true) {
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
