package com.dpuntu.downloader;

import java.util.List;

/**
 * Created on 2017/10/27.
 *
 * @author dpuntu
 */

class TaskManager {

    protected TaskManager() {
        throw new AssertionError("No instance.");
    }

    /**
     * 开始任务
     *
     * @param taskId
     *         任务的ID
     */
    protected static void startTask(String taskId) {
        DownloadBean downloadBean = TaskQueue.queryDownloadBean(taskId);
        if (downloadBean != null) {
            start(downloadBean);
        } else {
            Logger.e("you add " + taskId + " to TaskQueue ?");
        }
    }

    /**
     * 开始任务
     *
     * @param taskIdList
     *         任务的ID集合
     */
    protected static void startTaskList(List<String> taskIdList) {
        for (String taskId : taskIdList) {
            startTask(taskId);
        }
    }

    /**
     * 开始任务
     *
     * @param downloadBean
     *         任务下载的bean
     */
    protected static void start(DownloadBean downloadBean) {
        if (downloadBean.getState() == State.LOADING || downloadBean.getState() == State.FINISH) {
            return;
        }
        downloadBean.setState(State.READY);
        ObserverManager.getInstance().notifyObservers(downloadBean.getDownloader().getTaskId());
        PoolManager.start(downloadBean.getDownloadTask());
    }

    /**
     * 移除任务
     *
     * @param taskId
     *         任务的ID
     */
    protected static void removeTask(String taskId) {
        DownloadBean downloadBean = TaskQueue.queryDownloadBean(taskId);
        if (downloadBean != null) {
            remove(downloadBean);
        } else {
            Logger.e("you add " + taskId + " to TaskQueue ?");
        }
        TaskQueue.removeTask(taskId);
    }

    /**
     * 移除任务
     *
     * @param taskIdList
     *         任务的ID集合
     */
    protected static void removeTaskList(List<String> taskIdList) {
        for (String taskId : taskIdList) {
            removeTask(taskId);
        }
    }

    /**
     * 移除任务
     *
     * @param downloadBean
     *         任务下载的bean
     */
    protected static void remove(DownloadBean downloadBean) {
        downloadBean.setHintMsg("It's remove !!!");
        if (downloadBean.getState() != State.FINISH) {
            downloadBean.setState(State.ERROR);
            ObserverManager.getInstance().notifyObservers(downloadBean.getDownloader().getTaskId());
        }
        ObserverManager.getInstance().removeObservers(downloadBean.getDownloader().getTaskId());
        PoolManager.remove(downloadBean.getDownloadTask());
    }

    /**
     * 暂停任务
     *
     * @param taskId
     *         任务的ID
     */
    protected static void pauseTask(String taskId) {
        DownloadBean downloadBean = TaskQueue.queryDownloadBean(taskId);
        if (downloadBean != null) {
            pause(downloadBean);
        } else {
            Logger.e("you add " + taskId + " to TaskQueue ?");
        }
    }

    /**
     * 暂停任务
     *
     * @param taskIdList
     *         任务的ID集合
     */
    protected static void pauseTaskList(List<String> taskIdList) {
        for (String taskId : taskIdList) {
            pauseTask(taskId);
        }
    }

    /**
     * 暂停任务
     *
     * @param downloadBean
     *         任务下载的bean
     */
    protected static void pause(DownloadBean downloadBean) {
        if (downloadBean.getState() != State.LOADING) {
            return;
        }
        downloadBean.setState(State.PAUSE);
        ObserverManager.getInstance().notifyObservers(downloadBean.getDownloader().getTaskId());
        PoolManager.remove(downloadBean.getDownloadTask());
    }
}
