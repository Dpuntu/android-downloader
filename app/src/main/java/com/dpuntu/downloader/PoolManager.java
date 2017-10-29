package com.dpuntu.downloader;

/**
 * Created on 2017/10/27.
 *
 * @author dpuntu
 */

class PoolManager {

    private static DownloadPool mDownloadPool = DownloadPool.getDownloadPool();

    protected static int getCorePoolSize() {
        if (mDownloadPool == null) {
            mDownloadPool = DownloadPool.getDownloadPool();
        }
        return mDownloadPool.getCorePoolSize();
    }

    protected static void setCorePoolSize(int corePoolSize) {
        if (mDownloadPool == null) {
            mDownloadPool = DownloadPool.getDownloadPool();
        }
        mDownloadPool.setCorePoolSize(corePoolSize);
    }

    protected static int getMaxPoolSize() {
        if (mDownloadPool == null) {
            mDownloadPool = DownloadPool.getDownloadPool();
        }
        return mDownloadPool.getMaxPoolSize();
    }

    protected static void setMaxPoolSize(int maxPoolSize) {
        if (mDownloadPool == null) {
            mDownloadPool = DownloadPool.getDownloadPool();
        }
        mDownloadPool.setMaxPoolSize(maxPoolSize);
    }

    protected static long getKeepAliveTime() {
        if (mDownloadPool == null) {
            mDownloadPool = DownloadPool.getDownloadPool();
        }
        return mDownloadPool.getKeepAliveTime();
    }

    protected static void setKeepAliveTime(long keepAliveTime) {
        if (mDownloadPool == null) {
            mDownloadPool = DownloadPool.getDownloadPool();
        }
        mDownloadPool.setKeepAliveTime(keepAliveTime);
    }

    protected static void start(DownloadTask downloadTask) {
        mDownloadPool.execute(downloadTask);
    }

    protected static void remove(DownloadTask downloadTask) {
        mDownloadPool.remove(downloadTask);
    }
}
