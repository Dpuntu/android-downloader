package com.dpuntu.downloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created on 2017/10/27.
 *
 * @author dpuntu
 */

class DownloadTask implements Runnable {
    private DownloadBean mDownloadBean;
    private Downloader mDownloader;
    private OkHttpClient mOkHttpClient;
    private long totalSize;
    private InputStream downloadStream;
    private static final int BUFFER_SIZE = 1024 * 1024;
    private File downloadFile;
    private long oldTime = -1, oldLength = -1;

    protected DownloadTask() {
    }

    protected void setDownloadBean(DownloadBean downloadBean) {
        this.mDownloadBean = downloadBean;
    }

    protected DownloadTask(DownloadBean downloadBean) {
        this.mDownloadBean = downloadBean;
        this.mDownloader = mDownloadBean.getDownloader();
        this.mOkHttpClient = mDownloader.getClient();
        this.downloadFile = new File(mDownloader.getFilePath(), mDownloader.getFileName());
        oldTime = -1;
        oldLength = -1;
    }

    @Override
    public void run() {
        Logger.e("---------------- start for set totalSize ----------------");
        initTotalSize();
        Logger.e("---------------- end for set totalSize ----------------");
        initDownloadTask();
        Logger.e("---------------- start for set stream ----------------");
        initDownloadStream();
        Logger.e("---------------- end for set stream ----------------");
        startDownloadTask();
    }

    /**
     * 下载过程
     */
    private void startDownloadTask() {
        if (downloadStream == null) {
            returnError("the download stream is null !!!");
            return;
        }
        mDownloadBean.setState(State.LOADING);
        ObserverManager.getInstance().notifyObservers(mDownloader.getTaskId());
        RandomAccessFile raf = null;
        BufferedInputStream buffer = new BufferedInputStream(downloadStream);
        byte[] buf = new byte[BUFFER_SIZE];
        try {
            raf = new RandomAccessFile(downloadFile, "rwd");
            int len;
            long completed = mDownloader.getLoadedSize();
            raf.seek(completed);
            Logger.i(mDownloader.getTaskId() + " is start ! " + mDownloadBean.toString());
            while (mDownloader.getLoadedSize() < mDownloader.getTotalSize()
                    && mDownloader.getTotalSize() > 0 && (len = buffer.read(buf, 0, BUFFER_SIZE)) != -1
                    && mDownloadBean.getState() == State.LOADING) {
                raf.write(buf, 0, len);
                completed += len;
                mDownloader.setLoadedSize(completed);
                downLoadSpeedUpdate();
                ObserverManager.getInstance().notifyObservers(mDownloader.getTaskId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
                buffer.close();
                downloadStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (downloadFile.length() == mDownloader.getTotalSize()
                    && mDownloader.getLoadedSize() == mDownloader.getTotalSize()
                    && mDownloadBean.getState() == State.LOADING) {
                mDownloadBean.setState(State.FINISH);
                ObserverManager.getInstance().notifyObservers(mDownloader.getTaskId());
                Logger.i(mDownloader.getTaskId() + " is over ! " + mDownloadBean.toString());
            }
        }
    }

    /**
     * 根据是否为断点来获取下载的流文件
     */
    private void initDownloadStream() {
        Response response = getCall(true);
        if (response == null) {
            downloadStream = null;
            return;
        }
        if (response.isSuccessful() && response.body() != null) {
            downloadStream = response.body().byteStream();
        } else {
            downloadStream = null;
        }
    }

    /**
     * 初始化下载任务，主要判断是否为断点
     */
    private void initDownloadTask() {
        boolean isError = false;
        if (mDownloader.getTotalSize() != 0 && mDownloader.getTotalSize() == totalSize) {
            if (downloadFile.exists()) {
                if (downloadFile.length() <= mDownloader.getLoadedSize()) {
                    mDownloader.setLoadedSize(downloadFile.length());
                } else {
                    isError = true;
                }
            } else {
                isError = true;
            }
        } else {
            isError = true;
        }
        if (isError) {
            if (Utils.deleteFile(mDownloader.getFilePath(), mDownloader.getFileName())) {
                mDownloader.setTotalSize(totalSize);
                mDownloader.setLoadedSize(0);
            } else {
                returnError("delete old file error");
                return;
            }
            if (!Utils.createFile(mDownloader.getFilePath(), mDownloader.getFileName())) {
                returnError("create file error");
            }
        }
    }

    /**
     * 根据是否需要添加range请求头返回请求体
     *
     * @param isRangeHead
     *         是否需要添加range请求头
     *
     * @return 请求体
     */
    private Response getCall(boolean isRangeHead) {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        }
        Request request = new Request.Builder().get()
                .url(mDownloader.getUrl())
                .build();
        if (isRangeHead) {
            request = request.newBuilder()
                    .addHeader("Range", "bytes=" + mDownloader.getLoadedSize() + "-" + mDownloader.getTotalSize())
                    .build();
        }
        Call call = mOkHttpClient.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 初始化请求的资源大小
     */
    private void initTotalSize() {
        Response response = getCall(false);
        if (response == null) {
            returnError("okHttp call error!!!");
            return;
        }
        if (response.isSuccessful()) {
            String contentLength = response.header("Content-Length");
            if (contentLength == null || !contentLength.isEmpty()) {
                if (response.body() != null) {
                    totalSize = response.body().contentLength();
                } else {
                    returnError("the download file size is less than 0 ");
                }
            } else {
                totalSize = Long.parseLong(contentLength);
            }
        }
    }

    /**
     * 每隔一秒测量一次下载速度
     */
    private static final long SPACE_TIME = 500;

    private void downLoadSpeedUpdate() {
        if ((System.currentTimeMillis() - oldTime) < SPACE_TIME) {
            return;
        }
        if (oldTime > 0) {
            if ((mDownloader.getTotalSize() - mDownloader.getLoadedSize())
                    < mDownloadBean.getDownloadSpeed() / 2f) {
                mDownloadBean.setDownloadSpeed(0);
            } else {
                float speed = (mDownloader.getLoadedSize() - oldLength)
                        / ((System.currentTimeMillis() - oldTime) / SPACE_TIME);
                mDownloadBean.setDownloadSpeed(speed);
            }
        } else {
            mDownloadBean.setDownloadSpeed(0.00f);
        }
        oldTime = System.currentTimeMillis();
        oldLength = mDownloader.getLoadedSize();
    }

    /**
     * 返回错误提示
     *
     * @param errorMsg
     *         错误信息
     */
    private void returnError(String errorMsg) {
        Logger.i(errorMsg + " , " + mDownloader.getTaskId() + " has a error ! " + mDownloadBean.toString());
        mDownloadBean.setHintMsg(errorMsg);
        mDownloadBean.setState(State.ERROR);
        ObserverManager.getInstance().notifyObservers(mDownloader.getTaskId());
    }
}
