package com.dpuntu.downloader;

import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created on 2017/9/21.
 *
 * @author dpuntu
 */

public class Md5Utils {

    /**
     * 计算文件的 MD5值
     *
     * @param file
     *         待计算文件
     *
     * @return 计算出的MD5值
     */
    public static String getMd5ByFile(@Nullable File file) {
        if (file == null || !file.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            return null;
        }
        return bytesToHexString(digest.digest());
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * @param val
     *         需要加密的字符串
     *
     * @return 加密后的结果
     */
    public static String getMD5(String val) {
        byte[] m = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(val.getBytes("UTF-8"));
            m = md5.digest();//加密
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (m != null && m.length > 0) {
            return getString(m);
        } else {
            return null;
        }
    }

    private static String getString(byte[] result) {
        StringBuilder sb = new StringBuilder();
        for (byte b : result) {
            int i = b & 0xff;
            if (i <= 0xf) {
                sb.append(0);
            }
            sb.append(Integer.toHexString(i));
        }
        return sb.toString().toLowerCase();
    }
}
