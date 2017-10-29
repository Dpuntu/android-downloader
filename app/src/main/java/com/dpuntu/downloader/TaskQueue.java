package com.dpuntu.downloader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2017/10/27.
 *
 * @author dpuntu
 */

class TaskQueue {
    /** 下载队列 */
    private static Map<String, DownloadBean> mTaskMap = new HashMap<>();

    /**
     * 添加下载任务到队列
     *
     * @param bean
     *         下载任务的bean集合
     */
    protected static void addTask(DownloadBean bean) {
        if (mTaskMap.containsKey(bean.getDownloader().getTaskId())) {
            return;
        }
        mTaskMap.put(bean.getDownloader().getTaskId(), bean);
    }

    /**
     * 移除队列中下载任务
     *
     * @param taskId
     *         下载任务的id
     */
    protected static void removeTask(String taskId) {
        if (mTaskMap.containsKey(taskId)) {
            mTaskMap.remove(taskId);
        }
    }

    /**
     * 清空下载队列
     */
    protected static void removeAllTask() {
        mTaskMap.clear();
    }

    /**
     * 查询下载队列
     *
     * @param taskId
     *         下载任务的id
     */
    protected static DownloadBean queryDownloadBean(String taskId) {
        if (mTaskMap.containsKey(taskId)) {
            return mTaskMap.get(taskId);
        }
        return null;
    }

    /**
     * 查询下载队列中所有的任务
     */
    protected static List<DownloadBean> queryDownloadBeans() {
        return (List<DownloadBean>) mTaskMap.values();
    }
}
