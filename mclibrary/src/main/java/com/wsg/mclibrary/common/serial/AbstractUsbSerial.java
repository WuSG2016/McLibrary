package com.wsg.mclibrary.common.serial;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;


import com.hoho.android.usbserial.driver.UsbSerialDriver;

import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;


import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * USB 串口线程
 *
 * @author WuSG
 */
public abstract class AbstractUsbSerial extends BaseSerial {
    private static final String ACTION_USB_PERMISSION = "ACTION_USB_PERMISSION_PERMISSION";
    private UsbManager usbManager;

    private UsbSerialDriver driver;
    protected UsbSerialPort port;

    private SerialInputOutputManager mSerialIoManager;
    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {
                @Override
                public void onRunError(Exception e) {
                    serialError(109, "serial run is stop");
                }

                @Override
                public void onNewData(final byte[] data) {
                    if (!onTerminationReceive()) {
                        onReceiverSerialData(data);
                    }

                }
            };

    @Override
    public void run() {
        super.run();
        initUsbSerial();
    }

    /**
     * 加载USB串口
     */
    private void initUsbSerial() {
        if (null == serialConfig.getContext()) {
            return;
        }
        usbManager = (UsbManager) serialConfig.getContext().getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);
        if (availableDrivers.isEmpty()) {
            return;
        }
        for (UsbSerialDriver usbSerialDriver : availableDrivers) {
            Log.e("设备信息：VendorId=", +usbSerialDriver.getDevice().getVendorId() + "；ProductId=" + usbSerialDriver.getDevice().getProductId());
            if (usbSerialDriver.getDevice().getVendorId() == serialConfig.getVendorId() && usbSerialDriver.getDevice().getProductId() == serialConfig.getProductId()) {
                driver = usbSerialDriver;
            }else {
                serialError(110,"driver is not found");
            }
        }
        if (null == driver) {
            return;
        }
        if (usbManager.hasPermission(driver.getDevice())) {
            openUsbDevice();
        } else {
            requestPermission(usbManager, driver.getDevice());
        }
    }

    @Override
    public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh) {
        super.setUncaughtExceptionHandler(eh);
        if(port!=null){
            try {
                port.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检测USB权限
     *
     * @param manager
     * @param driver
     */
    private void requestPermission(UsbManager manager, UsbDevice driver) {
        UsbPermissionReceiver mUsbPermissionActionReceiver = new UsbPermissionReceiver();
        Intent intent = new Intent(ACTION_USB_PERMISSION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(serialConfig.getContext(), 0, intent, 0);
        IntentFilter intentFilter = new IntentFilter(ACTION_USB_PERMISSION);
        serialConfig.getContext().registerReceiver(mUsbPermissionActionReceiver, intentFilter);
        manager.requestPermission(driver, pendingIntent);
    }

    private class UsbPermissionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        // user choose YES for your previously popup window asking for grant permission for this usb device
                        if (null != usbDevice) {
                            openUsbDevice();
                        }
                    } else {
                        //user choose NO for your previously popup window asking for grant permission for this usb device
                        serialError(108, "Permission denied for device" + usbDevice);
                    }
                }
            }
        }
    }

    private void openUsbDevice() {
        assert usbManager != null;
        UsbDeviceConnection connection = usbManager.openDevice(driver.getDevice());
        if (connection == null) {
            // You probably need to call UsbManager.requestPermission(driver.getDevice(), ..)
            serialError(107, SerialError.getCodeErrorInfo(107));
            return;
        }
        port = driver.getPorts().get(0);
        try {
            port.open(connection);
            int baudRate = serialConfig.getBaudRate() == 0 ? 9600 : serialConfig.getBaudRate();
            int dataBits = serialConfig.getDataBits() == 0 ? 8 : serialConfig.getDataBits();
            int stopBits = serialConfig.getStopBits() == 0 ? UsbSerialPort.STOPBITS_1 : serialConfig.getStopBits();
            int parity = serialConfig.getParity() == 0 ? UsbSerialPort.PARITY_NONE : serialConfig.getParity();
            port.setParameters(baudRate, dataBits, stopBits, parity);
            if (serialConfig.getSerialListener() != null) {
                serialConfig.getSerialListener().onSerialInitComplete(0);
            }
            onDeviceStateChange();
        } catch (IOException e) {
            // Deal with error.
            serialError(107, SerialError.getCodeErrorInfo(107));
        }


    }

    private void stopIoManager() {
        if (mSerialIoManager != null) {
            Log.e(TAG, "Stopping io manager ..");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }

    private void startIoManager() {
        if (port != null) {
            Log.e(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(port, mListener);
            poolExecutor.submit(mSerialIoManager);
        }
    }

    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }

    /**
     * 用于终止接收
     *
     * @return
     */
    protected abstract boolean onTerminationReceive();

    /**
     * 接收处理
     * * @param data
     */
    protected abstract void onReceiverSerialData(byte[] data);

    /**
     * 用于往串口发送数据
     *
     * @param
     */
    protected void onSendSerialData(byte[] bytes, int timeoutMillis) {
        if (port != null&&!onTerminationSend()) {
            try {
                port.write(bytes, timeoutMillis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 是否终止发送
     * @return
     */
    protected abstract boolean onTerminationSend();
}
