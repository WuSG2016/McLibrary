package com.wsg.mclibrary.common.binder;

import android.os.SystemClock;
import android.util.Log;


import com.wsg.mclibrary.common.serial.AbstractSerial;
import com.wsg.mclibrary.common.serial.ISerialListener;
import com.wsg.mclibrary.common.serial.SerialConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WuSG
 */
public class SerialThread extends AbstractSerial implements ISerialListener {
    @Override
    protected SerialConfig onSerialConfig() {
        return new SerialConfig.Builder()
                .setBaudRate(9600)
                .setPort("/dev/ttyS3")
                .setSerialListener(this)
                .builder();

    }

    @Override
    protected void onSubmitRunnable() {

    }

    private byte[] bytes = new byte[]{(byte) 0x01, (byte) 0x05, (byte) 0x55, (byte) 0x5B, (byte) 0xff};

    @Override
    protected void onSendMessage() {
        if (getSerialPort() != null) {
            try {
                getSerialPort().getOutputStream().write(bytes);
//                Log.e("onSendMessage: ", "发送数据" + ByteUtils.byte2hex(bytes));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private List<Byte> byteLists = new ArrayList<>();

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
                        onDataReceive(received, size);
                    }
                } else {
                    // 暂停一点时间，免得一直循环造成CPU占用率过高
                    SystemClock.sleep(100);
                }
            } catch (IOException e) {

            }
            //Thread.yield();
        }


//        while (true) {
//            final byte[] read = getSerialCom().Read();
//            Log.e("onReceiverSerialData: ", ByteUtils.byte2hex(read));
//        }
    }
    /**
     * 处理获取到的数据
     *
     * @param received
     * @param size
     */
    private void onDataReceive(byte[] received, int size) {
        // TODO: 2018/3/22 解决粘包、分包等
//        Log.e("收到数据",ByteUtils.byte2hex(received));

    }

    @Override
    public void onSerialInitComplete(int openCode) {


    }

    @Override
    public void onSerialInitFail(int openCode) {

    }

    @Override
    public void onSerialError(int code, String errorMsg) {
        Log.e("onSerialError: ", code + errorMsg);

    }
}
