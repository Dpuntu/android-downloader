package com.dpuntu.downloader;

/**
 * Created on 2017/9/20.
 *
 * @author dpuntu
 */

public interface Observer {

    /**
     * 任务创建回调
     *
     * @param taskId
     *         任务Id
     */
    void onCreate(String taskId);

    /**
     * 任务准备回调
     *
     * @param taskId
     *         任务Id
     */
    void onReady(String taskId);

    /**
     * 任务下载过程中回调
     *
     * @param taskId
     *         任务Id
     * @param speed
     *         下载速度
     * @param totalSize
     *         待下载文件的大小
     * @param loadedSize
     *         已下载大小
     */
    void onLoading(String taskId, String speed, long totalSize, long loadedSize);

    /**
     * 任务暂停回调
     *
     * @param taskId
     *         任务Id
     * @param totalSize
     *         待下载文件的大小
     * @param loadedSize
     *         已下载大小
     */
    void onPause(String taskId, long totalSize, long loadedSize);

    /**
     * 任务完成回调
     *
     * @param taskId
     *         任务Id
     */
    void onFinish(String taskId);

    /**
     * 任务相爱在错误回调
     *
     * @param taskId
     *         任务Id
     * @param error
     *         错误提示语
     * @param totalSize
     *         待下载文件的大小
     * @param loadedSize
     *         已下载大小
     */
    void onError(String taskId, String error, long totalSize, long loadedSize);
}
