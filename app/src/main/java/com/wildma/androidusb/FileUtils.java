package com.wildma.androidusb;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Author   wildma
 * Date     2019/07/27
 * Desc     ${文件相关工具类}
 */
public class FileUtils {

    /**
     * 获取 U 盘临时根目录（一体机会在临时目录下再创建多个包含 "udisk" 的目录，所以临时目录并不是 U 盘真正的根目录）
     *
     * @param context Context
     * @return U 盘临时根目录集合
     */
    public static List<String> getUSBTempRootDirectory(Context context) {
        List<String> usbTempRootDirectory = new ArrayList<>();
        try {
            StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            Class<StorageManager> storageManagerClass = StorageManager.class;
            String[] paths = (String[]) storageManagerClass.getMethod("getVolumePaths").invoke(storageManager);
            for (String path : paths) {
                Object volumeState = storageManagerClass.getMethod("getVolumeState", String.class).invoke(storageManager, path);
                //路劲包含 internal 一般是内部存储，例如 /mnt/internal_sd，需要排除
                if (!path.contains("emulated") && !path.contains("internal") && Environment.MEDIA_MOUNTED.equals(volumeState)) {
                    usbTempRootDirectory.add(path);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usbTempRootDirectory;
    }

    /**
     * 获取 U 盘真正根目录
     *
     * @param usbTempRootDirectory U 盘临时根目录
     * @return U 盘真正根目录
     */
    public static String getUSBRealRootDirectory(String usbTempRootDirectory) {
        String realUSBRootDirectory = "";
        File dir = new File(usbTempRootDirectory);
        File[] files = dir.listFiles();

        /**
         * 注意：
         * 经测试，
         * TV 直接是 usbTempRootDirectory 作为 U 盘的根目录，例如：/storage/577F-85CA
         * 一体机会在 U 盘的根目录（usbTempRootDirectory=/mnt/usb_storage/USB_DISK4）下再创建多个包含 "udisk" 的目录，然后其中一个作为 U 盘的根目录，例如：/mnt/usb_storage/USB_DISK4/udisk0
         */
        if (files != null) {
            for (File file : files) {
                //如果根目录下还有包含 "udisk" 的目录，则该包含 "udisk" 的目录才是 U 盘真正的根目录
                if (file.isDirectory() && file.list().length > 0 && file.getAbsolutePath().contains("udisk")) {
                    realUSBRootDirectory = file.getAbsolutePath();
                    break;
                } else { // 如果根目录下没有包含 "udisk" 的目录，说明 dir 就是根目录
                    realUSBRootDirectory = dir.getAbsolutePath();
                }
            }
        }
        return realUSBRootDirectory;
    }
}
