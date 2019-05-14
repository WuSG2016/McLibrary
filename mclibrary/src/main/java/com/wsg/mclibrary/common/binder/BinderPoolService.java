package com.wsg.mclibrary.common.binder;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wsg.mclibrary.common.CommonConstants;


/**
 * 连接池服务已在onTransact中加入权限验证
 *
 * @author WuSG
 *         Create at 2018/12/3 17:45
 **/
public class BinderPoolService extends Service {
    private BinderPool.BinderPoolImpl binderPool = new BinderPool.BinderPoolImpl(this);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int check = this.checkCallingOrSelfPermission(CommonConstants.McConstants.BINDER_SERVICE_PERMISSION);
        if (check == PackageManager.PERMISSION_DENIED) {
            Log.e("onTransact: ", "服务已拒绝,未加入权限");
            return null;
        }
        return  binderPool;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
