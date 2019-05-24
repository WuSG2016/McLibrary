package com.wsg.mclib;

import android.util.Log;

import com.wsg.mclibrary.common.serial.AbstractUsbSerial;
import com.wsg.mclibrary.common.serial.ISerialListener;
import com.wsg.mclibrary.common.serial.SerialConfig;

/**
 * @author WuSG
 */
public class UsbSerialThread extends AbstractUsbSerial implements ISerialListener {
    @Override
    protected void onSubmitRunnable() {

    }

    @Override
    protected SerialConfig onSerialConfig() {
        return new SerialConfig.Builder()
                .setSerialListener(this)
                .setBaudRate(9600)
                .setVendorId(1659)
                .setProductId(8963)
                .setContext(App.application)
                .builder();

    }

    @Override
    public void onSerialInitComplete(int openCode) {
        Log.e("onSerialInitComplete: ", "加载完成");

    }

    @Override
    public void onSerialError(int code, String errorMsg) {
        Log.e("onSerialError: ", errorMsg);
    }


    @Override
    protected boolean onTerminationReceive() {
        return false;
    }

    @Override
    protected void onReceiverSerialData(byte[] data) {
        Log.e("onReceiverSerialData: ",ByteUtils.byte2hex(data) );
    }

    @Override
    protected boolean onTerminationSend() {
        return false;
    }


}
