package com.dpuntu.downloader;

import okhttp3.OkHttpClient;

/**
 * Created on 2017/10/27.
 *
 * @author dpuntu
 */

public class Downloader {
    /** 任务ID */
    private String taskId;
    /** 任务下载的地址 */
    private String url;
    /** 下载设置的文件名 */
    private String fileName;
    /** 下载文件路劲 */
    private String filePath;
    /** 下载文件的长度 */
    private long totalSize;
    /** 下载文件已经下载的长度 */
    private long loadedSize;
    /** 设置的client */
    private OkHttpClient httpClient;

    public Downloader() {
        new Downloader(new Builder());
    }

    private Downloader(Builder builder) {
        this.taskId = builder.taskId;
        this.url = builder.url;
        this.fileName = builder.fileName;
        this.filePath = builder.filePath;
        this.httpClient = builder.httpClient;
        this.totalSize = builder.totalSize;
        this.loadedSize = builder.loadedSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public long getLoadedSize() {
        return loadedSize;
    }

    protected void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    protected void setLoadedSize(long loadedSize) {
        this.loadedSize = loadedSize;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public OkHttpClient getClient() {
        return httpClient;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != Downloader.class) {
            return false;
        }
        Downloader downloader = (Downloader) obj;
        return taskId.equals(downloader.taskId)
                && url.equals(downloader.url)
                && fileName.equals(fileName)
                && filePath.equals(filePath);
    }

    public static class Builder {
        private String taskId;
        private String url;
        private String fileName;
        private long totalSize = 0;
        private long loadedSize = 0;
        private String filePath = Utils.getFilePath();
        private OkHttpClient httpClient = new OkHttpClient();

        public Builder() {
        }

        public Builder taskId(String taskId) {
            this.taskId = taskId;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder client(OkHttpClient client) {
            this.httpClient = client;
            return this;
        }

        public Builder totalSize(long totalSize) {
            this.totalSize = totalSize;
            return this;
        }

        public Builder loadedSize(long loadedSize) {
            this.loadedSize = loadedSize;
            return this;
        }

        public Downloader build() {
            return new Downloader(this);
        }
    }
}
