package com.wsg.mclibrary.common.binder;

import android.os.IBinder;

/** 用于查询Binder的接口
 * @author WuSG
 */
public interface IBinderInterface {
    /**
     * 返回IBinder
     *
     * @param binderCode 标识
     * @return
     */
    IBinder onBinder(int binderCode);
}
