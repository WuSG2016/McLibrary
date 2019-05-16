package com.wsg.mclib;

import android.os.SystemClock;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

import android_serialport_api.SerialPort;


public class MainActivity extends AppCompatActivity {
    private byte[] bytes = new byte[]{(byte) 0x01, (byte) 0x05, (byte) 0x55, (byte) 0x5B, (byte) 0xff};
    private SerialPort serialPort;
    private BufferedInputStream mInputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        try {
//            serialPort = new SerialPort(new File("dev/ttyS3"), 9600, 0);
//            mInputStream = new BufferedInputStream(serialPort.getInputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        new ReceiverThread().start();
//        new SendThread().start();


        new SerialThread().start();
    }

    public class SendThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                if (serialPort != null) {
                    try {
                        serialPort.getOutputStream().write(bytes);
                        Log.e("onSendMessage: ", "发送数据" + ByteUtils.byte2hex(bytes));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public class ReceiverThread extends Thread {
        @Override
        public void run() {
            byte[] received = new byte[1024];
            int size;
            while (true) {

                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                try {

                    int available = mInputStream.available();

                    if (available > 0) {
                        size = mInputStream.read(received);
                        if (size > 0) {
                            Log.e("收到数据", ByteUtils.byte2hex(received));
                        }
                    } else {
                        // 暂停一点时间，免得一直循环造成CPU占用率过高
                        SystemClock.sleep(100);
                    }
                } catch (IOException e) {

                }
                //Thread.yield();
            }

        }
    }

}
