package com.dpuntu.downloader;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2017/10/27.
 *
 * @author dpuntu
 */

class DownloadPool extends ThreadPoolExecutor {
    private static DownloadPool mDownloadPool;
    private static int corePoolSize = 1;
    private static int maxPoolSize = 5;
    private static long keepAliveTime = 500;

    protected static void createPool() {
        BlockingQueue<Runnable> workers = new LinkedBlockingQueue<>();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        mDownloadPool = new DownloadPool(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                workers,
                Executors.defaultThreadFactory(),
                handler);
    }

    protected static DownloadPool getDownloadPool() {
        if (mDownloadPool == null) {
            createPool();
        }
        return mDownloadPool;
    }

    protected static void setDownloadPool(DownloadPool downloadPool) {
        if (mDownloadPool != null) {
            mDownloadPool.getQueue().clear();
            mDownloadPool.shutdownNow();
            mDownloadPool = null;
        }
        mDownloadPool = downloadPool;
    }

    @Override
    public int getCorePoolSize() {
        return corePoolSize;
    }

    @Override
    public void setCorePoolSize(int corePoolSize) {
        DownloadPool.corePoolSize = corePoolSize;
    }

    protected int getMaxPoolSize() {
        return maxPoolSize;
    }

    protected void setMaxPoolSize(int maxPoolSize) {
        DownloadPool.maxPoolSize = maxPoolSize;
        if (mDownloadPool != null) {
            mDownloadPool.setMaxPoolSize(DownloadPool.maxPoolSize);
        }
    }

    protected long getKeepAliveTime() {
        return keepAliveTime;
    }

    protected void setKeepAliveTime(long keepAliveTime) {
        DownloadPool.keepAliveTime = keepAliveTime;
        if (mDownloadPool != null) {
            mDownloadPool.setKeepAliveTime(DownloadPool.keepAliveTime);
        }
    }

    protected DownloadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    protected DownloadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    protected DownloadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    protected DownloadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

}
