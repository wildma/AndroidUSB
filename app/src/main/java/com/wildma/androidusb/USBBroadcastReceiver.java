package com.wildma.androidusb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Author   wildma
 * Date     2019/07/27
 * Desc     ${USB 插入拔出监听}
 */
public class USBBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "USBBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        switch (intent.getAction()) {
            case Intent.ACTION_MEDIA_MOUNTED://扩展介质被插入，而且已经被挂载。
                Log.d(TAG, "ACTION_MEDIA_MOUNTED:" + intent);
                if (intent.getData() != null) {
                    String path = intent.getData().getPath();
                    String usbRealRootDirectory = FileUtils.getUSBRealRootDirectory(path);
                    Log.d(TAG, "usbRealRootDirectory: " + usbRealRootDirectory);
                }
                break;
            case Intent.ACTION_MEDIA_EJECT://用户想要移除扩展介质（扩展介质将要被拔出）
                Log.d(TAG, "ACTION_MEDIA_EJECT:" + intent);
                //这里释放所有占用的资源
                break;
            case Intent.ACTION_MEDIA_UNMOUNTED://扩展介质存在，但是还没有被挂载。（扩展介质已被拔出）
                Log.d(TAG, "ACTION_MEDIA_EJECT:" + intent);
                //这里做一些拔出 U 盘后的其他操作
                break;
        }
    }
}
