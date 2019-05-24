package com.wsg.mclib;

import android.os.SystemClock;
import android.util.Log;


import com.wsg.mclibrary.common.serial.AbstractSerial;
import com.wsg.mclibrary.common.serial.ISerialListener;
import com.wsg.mclibrary.common.serial.SerialConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author WuSG
 */
public class SerialThread extends AbstractSerial implements ISerialListener {
    @Override
    protected SerialConfig onSerialConfig() {
        return new SerialConfig.Builder()
                .setBaudRate(9600)
                .setPort("/dev/ttyO3")
                .setSerialListener(this)
                .builder();

    }

    @Override
    protected void onSubmitRunnable() {

    }



    private byte[] bytes = new byte[]{(byte) 0x01, (byte) 0x05, (byte) 0x55, (byte) 0x5B, (byte) 0xff};

    @Override
    protected boolean onTerminationSend() {
        return false;
    }

    @Override
    protected boolean onTerminationReceive() {
        return false;
    }

    @Override
    protected void onSendMessage() {
        if (mOutputStream != null) {
            try {
                mOutputStream.write(bytes);
                Log.e("onSendMessage: ", "发送数据" + ByteUtils.byte2hex(bytes));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SystemClock.sleep(2000);

    }


    @Override
    protected void onReceiverSerialData() {
        byte[] received = new byte[1024];
        int size;
        while (true) {
            if (currentThread().isInterrupted()) {
                break;
            }
            try {

                int available = mInputStream.available();

                if (available > 0) {
                    size = mInputStream.read(received);
                    if (size > 0) {
                        byte[] bytes = Arrays.copyOf(received, size);
                        Log.e("收到数据", ByteUtils.byte2hex(bytes));
                    }
                } else {
                    // 暂停一点时间，免得一直循环造成CPU占用率过高
                    SystemClock.sleep(1);
                }
            } catch (IOException e) {

            }

        }

    }



    @Override
    public void onSerialInitComplete(int openCode) {


    }


    @Override
    public void onSerialError(int code, String errorMsg) {
        Log.e("onSerialError: ", code + errorMsg);

    }


}
