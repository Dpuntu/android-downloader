package com.dpuntu.downloader;

/**
 * Created on 2017/9/20.
 *
 * @author dpuntu
 */

interface Subject {
    /**
     * 给某一个任务注册一个监听器
     *
     * @param taskId
     *         任务ID
     * @param observer
     *         监听器
     */
    void registerObserver(String taskId, Observer observer);

    /**
     * 给某一个任务移除某一个监听器
     *
     * @param taskId
     *         任务ID
     * @param observer
     *         监听器
     */
    void removeObserver(String taskId, Observer observer);

    /**
     * 给某一个任务的所有监听者反馈消息
     *
     * @param taskId
     *         任务ID
     */
    void notifyObservers(String taskId);
}

