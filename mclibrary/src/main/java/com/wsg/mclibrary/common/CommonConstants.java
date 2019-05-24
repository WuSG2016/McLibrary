package com.wsg.mclibrary.common;

import android.os.Environment;

import com.wsg.mclibrary.common.serial.SerialConfig;

/**
 * @author WuSG
 */
public class CommonConstants {
    public static SerialConfig serialConfig;
    /**
     * 根目录
     */
    public static String BOOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static abstract class McConstants {
        /**
         * 权限
         */
        public static final String BINDER_SERVICE_PERMISSION = serialConfig != null ? "" : serialConfig.getPermission();

    }
}
