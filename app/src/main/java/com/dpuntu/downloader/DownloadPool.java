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
    private static long keepAliveTime = 3000;

    protected synchronized static void createPool() {
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

    protected synchronized static DownloadPool getDownloadPool() {
        if (mDownloadPool == null) {
            synchronized (DownloadPool.class) {
                if (mDownloadPool == null) {
                    createPool();
                }
            }
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
        super.setCorePoolSize(corePoolSize);
    }

    protected int getMaxPoolSize() {
        return maxPoolSize;
    }

    @Override
    public void setMaximumPoolSize(int maxPoolSize) {
        DownloadPool.maxPoolSize = maxPoolSize;
        super.setMaximumPoolSize(maxPoolSize);
    }

    protected long getKeepAliveTime() {
        return keepAliveTime;
    }

    @Override
    public void setKeepAliveTime(long keepAliveTime, TimeUnit unit) {
        DownloadPool.keepAliveTime = keepAliveTime;
        super.setKeepAliveTime(keepAliveTime, unit);
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
