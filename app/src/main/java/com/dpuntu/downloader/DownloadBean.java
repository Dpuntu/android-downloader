package com.dpuntu.downloader;

/**
 * Created on 2017/10/27.
 *
 * @author dpuntu
 */

class DownloadBean {
    /** 下载文件请求体 */
    private Downloader downloader;
    /** 下载任务线程 */
    private DownloadTask downloadTask;
    /** 下载速度 */
    private float downloadSpeed;
    /** 下载状态 */
    private State mState;
    /** 下载提示语 ，主要用在下载错误提示 */
    private String hintMsg;

    protected DownloadBean() {
        throw new AssertionError("No instance.");
    }

    protected DownloadBean(Builder builder) {
        setDownloader(builder.downloader);
        setDownloadTask(builder.downloadTask);
        setDownloadSpeed(builder.downloadSpeed);
        setState(builder.mState);
    }

    protected State getState() {
        return mState;
    }

    protected void setState(State state) {
        this.mState = state;
    }

    protected Downloader getDownloader() {
        return downloader;
    }

    protected void setDownloader(Downloader downloader) {
        this.downloader = downloader;
    }

    protected DownloadTask getDownloadTask() {
        return downloadTask;
    }

    protected void setDownloadTask(DownloadTask downloadTask) {
        this.downloadTask = downloadTask;
    }

    protected float getDownloadSpeed() {
        return downloadSpeed;
    }

    protected void setDownloadSpeed(float downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

    protected String getHintMsg() {
        return hintMsg;
    }

    protected void setHintMsg(String hintMsg) {
        this.hintMsg = hintMsg;
    }

    @Override
    public String toString() {
        return " taskId = " + downloader.getTaskId()
                + " url = " + downloader.getUrl()
                + " totalSize = " + downloader.getTotalSize()
                + " loadedSize = " + downloader.getLoadedSize()
                + " downloadSpeed = " + downloadSpeed
                + " mState = " + mState
                + " fileName = " + downloader.getFileName()
                + " filePath = " + downloader.getFilePath();
    }

    static class Builder {
        private Downloader downloader;
        private DownloadTask downloadTask;
        private float downloadSpeed = 0.00F;
        private State mState = State.CREATE;

        protected Builder downloader(Downloader downloader) {
            this.downloader = downloader;
            return this;
        }

        protected Builder downloadTask(DownloadTask downloadTask) {
            this.downloadTask = downloadTask;
            return this;
        }

        protected Builder downloadSpeed(float downloadSpeed) {
            this.downloadSpeed = downloadSpeed;
            return this;
        }

        protected Builder downloadState(State state) {
            this.mState = state;
            return this;
        }

        protected DownloadBean build() {
            return new DownloadBean(this);
        }
    }
}
