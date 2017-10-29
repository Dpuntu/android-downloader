package com.dpuntu.downloader;

import android.support.v4.util.SimpleArrayMap;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created on 2017/10/27.
 *
 * @author dpuntu
 */

class ObserverManager implements Subject {
    private static ObserverManager mManager = new ObserverManager();

    protected static ObserverManager getInstance() {
        return mManager;
    }

    /** 下载任务对应的监听器集合 */
    private SimpleArrayMap<String, CopyOnWriteArrayList<Observer>> taskObservers = new SimpleArrayMap<>();

    /**
     * 给某一个任务注册一个监听器
     *
     * @param taskId
     *         任务ID
     * @param observer
     *         监听器
     */
    @Override
    public void registerObserver(String taskId, Observer observer) {
        CopyOnWriteArrayList<Observer> observerList;
        if (taskObservers.containsKey(taskId)) {
            observerList = taskObservers.get(taskId);
            if (observerList == null) {
                observerList = new CopyOnWriteArrayList<>();
            }
        } else {
            observerList = new CopyOnWriteArrayList<>();
        }
        if (observerList.contains(observer)) {
            return;
        }
        observerList.add(observer);
        taskObservers.put(taskId, observerList);
    }

    /**
     * 给某一个任务移除某一个监听器
     *
     * @param taskId
     *         任务ID
     * @param observer
     *         监听器
     */
    @Override
    public void removeObserver(String taskId, Observer observer) {
        if (taskObservers.containsKey(taskId)) {
            CopyOnWriteArrayList<Observer> observerList = taskObservers.get(taskId);
            if (observerList != null && observerList.contains(observer)) {
                observerList.remove(observer);
            }
        }
    }

    /**
     * 给某一个任务的所有监听者反馈消息
     *
     * @param taskId
     *         任务ID
     */
    @Override
    public void notifyObservers(String taskId) {
        if (taskObservers.containsKey(taskId)) {
            final CopyOnWriteArrayList<Observer> observerList = taskObservers.get(taskId);
            if (observerList != null) {
                final DownloadBean bean = TaskQueue.queryDownloadBean(taskId);
                notifyTaskObservers(bean, observerList);
            }
        }
    }

    protected void removeObservers(String taskId) {
        if (taskObservers.containsKey(taskId)) {
            CopyOnWriteArrayList<Observer> observerList = taskObservers.get(taskId);
            if (observerList != null) {
                taskObservers.get(taskId).clear();
            }
        }
    }

    /**
     * 给某一个任务的所有监听者反馈消息
     *
     * @param bean
     *         任务ID对应的bean
     * @param observerList
     *         监听者集合
     */
    private void notifyTaskObservers(DownloadBean bean, CopyOnWriteArrayList<Observer> observerList) {
        switch (bean.getState()) {
            case CREATE:
                for (Observer observer : observerList) {
                    observer.onCreate(bean.getDownloader().getTaskId());
                }
                break;
            case READY:
                for (Observer observer : observerList) {
                    observer.onReady(bean.getDownloader().getTaskId());
                }
                break;
            case LOADING:
                for (Observer observer : observerList) {
                    observer.onLoading(bean.getDownloader().getTaskId(),
                                       Utils.formatSpeed(bean.getDownloadSpeed()), bean.getDownloader().getTotalSize(),
                                       bean.getDownloader().getLoadedSize());
                }
                break;
            case PAUSE:
                for (Observer observer : observerList) {
                    observer.onPause(bean.getDownloader().getTaskId(), bean.getDownloader().getTotalSize(),
                                     bean.getDownloader().getLoadedSize());
                }
                break;
            case FINISH:
                for (Observer observer : observerList) {
                    observer.onFinish(bean.getDownloader().getTaskId());
                    removeObserver(bean.getDownloader().getTaskId(), observer);
                }
                break;
            case ERROR:
                for (Observer observer : observerList) {
                    observer.onError(bean.getDownloader().getTaskId(), bean.getHintMsg(), bean.getDownloader().getTotalSize(),
                                     bean.getDownloader().getLoadedSize());
                }
                break;
            default:
                break;
        }
    }
}
