package com.dpuntu.downloader;

import java.util.concurrent.TimeUnit;

/**
 * Created on 2017/10/27.
 *
 * @author dpuntu
 */

class PoolManager {
    private static DownloadPool mDownloadPool;

    protected static void createDownloadPool() {
        mDownloadPool = DownloadPool.getDownloadPool();
    }

    protected static int getCorePoolSize() {
        return mDownloadPool.getCorePoolSize();
    }

    protected static void setCorePoolSize(int corePoolSize) {
        mDownloadPool.setCorePoolSize(corePoolSize);
    }

    protected static int getMaxPoolSize() {
        return mDownloadPool.getMaxPoolSize();
    }

    protected static void setMaxPoolSize(int maxPoolSize) {
        mDownloadPool.setMaximumPoolSize(maxPoolSize);
    }

    protected static long getKeepAliveTime() {
        return mDownloadPool.getKeepAliveTime();
    }

    protected static void setKeepAliveTime(long keepAliveTime) {
        mDownloadPool.setKeepAliveTime(keepAliveTime, TimeUnit.MILLISECONDS);
    }

    protected static void start(DownloadTask downloadTask) {
        mDownloadPool.execute(downloadTask);
    }

    protected static void remove(DownloadTask downloadTask) {
        mDownloadPool.remove(downloadTask);
    }
}
