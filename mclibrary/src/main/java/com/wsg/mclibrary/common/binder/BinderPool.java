package com.wsg.mclibrary.common.binder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import com.wsg.mclibrary.IBinderPool;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


/**
 * binder 连接池
 *
 * @author WuSG
 *         Create at 2018/12/3 18:55
 **/
public class BinderPool {
    private CountDownLatch mConnectBinderPoolCountDownLatch;
    private IBinderPool mIBinderPool;

    private WeakReference<Context> mContextWeakReference;
    private List<BindServiceConnectListener> bindServiceConnectListenerList = new ArrayList<>();
    public IBinderInterface iBinderInterface;

    private BinderPool() {
    }

    public static BinderPool getInstance() {
        return BinderPoolHolder.BINDER_POOL;
    }

    private static class BinderPoolHolder {
        private static BinderPool BINDER_POOL = new BinderPool();
    }

    /**
     * 连接绑定池服务
     *
     * @param context
     * @param listener
     */
    public synchronized void connectBinderPoolService(Context context, BindServiceConnectListener listener, IBinderInterface iBinderInterface) {
        if (mContextWeakReference == null || mContextWeakReference.get() == null) {
            mContextWeakReference = new WeakReference<>(context);
        }
        this.bindServiceConnectListenerList.add(listener);
        this.iBinderInterface = iBinderInterface;
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(mContextWeakReference.get(), BinderPoolService.class);
        mContextWeakReference.get().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);


    }

    public IBinder queryBinder(int code) {
        IBinder binder = null;
        if (mIBinderPool != null) {
            try {
                binder = mIBinderPool.queryBinder(code);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return binder;
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mIBinderPool = IBinderPool.Stub.asInterface(iBinder);
            try {
                mIBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
                if (bindServiceConnectListenerList.size() > 0) {
                    for (int i = 0; i < bindServiceConnectListenerList.size(); i++) {
                        BindServiceConnectListener listener = bindServiceConnectListenerList.get(i);
                        listener.onConnected(mIBinderPool);
                    }

                }
            } catch (RemoteException e) {
                e.printStackTrace();
                if (bindServiceConnectListenerList.size() > 0) {
                    for (int i = 0; i < bindServiceConnectListenerList.size(); i++) {
                        BindServiceConnectListener listener = bindServiceConnectListenerList.get(i);
                        listener.onError(e);
                    }
                }
            }
            mConnectBinderPoolCountDownLatch.countDown();
        }

        private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {
                mIBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
                mIBinderPool = null;
                if (bindServiceConnectListenerList.size() > 0) {
                    for (int i = 0; i < bindServiceConnectListenerList.size(); i++) {
                        BindServiceConnectListener listener = bindServiceConnectListenerList.get(i);
                        connectBinderPoolService(mContextWeakReference.get(), listener, iBinderInterface);
                    }
                }
            }
        };

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (bindServiceConnectListenerList.size() > 0) {
                for (int i = 0; i < bindServiceConnectListenerList.size(); i++) {
                    BindServiceConnectListener listener = bindServiceConnectListenerList.get(i);
                    listener.onDisconnected();
                }
            }
        }
    };


    /**
     * 服务绑定监听接口
     */
    public interface BindServiceConnectListener {
        /**
         * 绑定服务连接成功
         *
         * @param binderPool
         */
        void onConnected(IBinderPool binderPool);

        /**
         * 异常
         *
         * @param e
         */
        void onError(Exception e);

        /**
         * 连接断开
         */
        void onDisconnected();
    }

    public static class BinderPoolImpl extends IBinderPool.Stub {
        WeakReference<Context> contextWeakReference;

        public BinderPoolImpl(Context context) {
            contextWeakReference = new WeakReference<>(context);
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {

            String packageName = null;
            String[] packages = contextWeakReference.get().getPackageManager().getPackagesForUid(getCallingUid());
            if (packages != null) {
                packageName = packages[0];
            }
            if (!packageName.startsWith("com.ubox")) {
                Log.e("onTransact: ", "服务已拒绝");
                return false;
            }

            Log.e("onTransact: ", "服务已接收");
            return super.onTransact(code, data, reply, flags);
        }

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            return BinderPool.getInstance().iBinderInterface.onBinder(binderCode);
        }

    }
}
