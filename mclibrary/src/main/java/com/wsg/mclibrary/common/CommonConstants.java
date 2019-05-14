package com.wsg.mclibrary.common;

import android.os.Environment;

/**
 * @author WuSG
 */
public class CommonConstants {

    /**
     * 根目录
     */
    public static String BOOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static abstract class McConstants {
        /**
         * 咖啡机
         */
        public static final int BINDER_XN_COFFEE_VMC_CODE = 1001;
        /**
         * 富士冰山
         */
        public static final int BINDER_FSBS_VMC_CODE = 1002;
        /**
         * 权限
         */
        public static final String BINDER_SERVICE_PERMISSION = "permission";

    }
}
