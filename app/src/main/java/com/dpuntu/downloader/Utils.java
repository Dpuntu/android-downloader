package com.dpuntu.downloader;

import android.os.Environment;
import android.os.Looper;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created on 2017/10/27.
 *
 * @author dpuntu
 */

class Utils {
    /**
     * 速度计算
     *
     * @param speed
     *         原长度
     *
     * @return
     */
    protected static String formatSpeed(float speed) {
        String unit = "b/s";
        DecimalFormat decimalFormat;
        if (speed >= 1024 * 1024) {
            speed = speed / 1024F / 1024F;
            unit = "mb/s";
        } else if (speed >= 1024) {
            speed = speed / 1024F;
            unit = "kb/s";
        }
        decimalFormat = new DecimalFormat("0.00");
        String speedNew = decimalFormat.format(speed);
        return speedNew + unit;
    }

    /**
     * 判断主线程
     *
     * @return 是否为主线程
     */
    protected static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 判断SD卡是否存在
     *
     * @return 是否存在
     */
    protected static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断外部SD卡是否存在
     *
     * @return 是否存在
     */
    protected static boolean isExternalStorage() {
        if (isSdCardExist() || !Environment.isExternalStorageRemovable()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得文件存储路径
     *
     * @return 保存的路径
     */
    protected static String getFilePath() {
        if (isExternalStorage()) {
            return DownloadContext.getContext().getExternalFilesDir(null).getPath();
        } else {
            return DownloadContext.getContext().getFilesDir().getPath();
        }
    }

//    protected static boolean deleteFile(String filePath, String fileName) {
//        if (!delete(filePath, fileName)) {
//            File file = new File(filePath, fileName);
//            if (file.exists()) {
//                return file.delete();
//            } else {
//                return true;
//            }
//        } else {
//            return true;
//        }
//    }

    /**
     * 删除文件
     *
     * @param filePath
     *         文件路径
     * @param fileName
     *         文件名
     */
    protected static boolean deleteFile(String filePath, String fileName) {
        File dic = new File(filePath);
        if (dic.exists()) {
            File file = new File(filePath, fileName);
            if (file.exists()) {
                return file.delete();
            }
        }
        return true;
    }

    /**
     * 新建文件
     *
     * @param filePath
     *         文件路径
     * @param fileName
     *         文件名
     */
    protected static boolean createFile(String filePath, String fileName) {
        boolean isCreate = true;
        File dic = new File(filePath);
        File newFile = new File(filePath + fileName);
        try {
            if (!dic.exists()) {
                isCreate = dic.mkdirs();
            }
            if (!newFile.exists()) {
                isCreate = isCreate && newFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return isCreate;
    }
}
