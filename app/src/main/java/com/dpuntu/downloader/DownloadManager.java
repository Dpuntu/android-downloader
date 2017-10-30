package com.dpuntu.downloader;

import android.content.Context;

import java.util.List;

import static com.dpuntu.downloader.TaskQueue.queryDownloadBean;

/**
 * Created on 2017/10/27.
 *
 * @author dpuntu
 */

public class DownloadManager {

    public DownloadManager() {
        throw new AssertionError("No instance.");
    }

    /**
     * 初始化下载器
     */
    public static void initDownloader(Context context) {
        DownloadContext.setContext(context);
        PoolManager.createDownloadPool();
    }

    /**
     * 获取下载器中核心线程数量
     *
     * @return 核心线程数量
     */
    public static int getCorePoolSize() {
        return PoolManager.getCorePoolSize();
    }

    /**
     * 初始化下载器最大线程数量
     *
     * @return 最大线程数量
     */
    public static int getMaxPoolSize() {
        return PoolManager.getMaxPoolSize();
    }

    /**
     * 初始化下载器线程存活时间，单位是毫秒
     *
     * @return 线程存活时间，单位是毫秒
     */
    public static long getKeepAliveTime() {
        return PoolManager.getKeepAliveTime();
    }

    /**
     * 设置下载器中核心线程数量
     *
     * @param corePoolSize
     *         核心线程数量
     */
    public static void setCorePoolSize(int corePoolSize) {
        PoolManager.setCorePoolSize(corePoolSize);
    }

    /**
     * 初始化下载器最大线程数量
     *
     * @param maxPoolSize
     *         最大线程数量
     */
    public static void setMaxPoolSize(int maxPoolSize) {
        PoolManager.setMaxPoolSize(maxPoolSize);
    }

    /**
     * 初始化下载器线程存活时间，单位是毫秒
     *
     * @param keepAliveTime
     *         线程存活时间，单位是毫秒
     */
    public static void setKeepAliveTime(int keepAliveTime) {
        PoolManager.setKeepAliveTime(keepAliveTime);
    }

    /**
     * 添加下载任务到下载队列
     *
     * @param task
     *         下载任务
     */
    private static void addDownloader(Downloader task) {
        DownloadBean bean = TaskQueue.queryDownloadBean(task.getTaskId());
        if (bean != null) {
            if (bean.getDownloader().equals(task)) {
                bean.getDownloader().setLoadedSize(task.getLoadedSize());
                bean.getDownloader().setTotalSize(task.getTotalSize());
                bean.setState(State.CREATE);
                ObserverManager.getInstance().notifyObservers(task.getTaskId());
            } else {
                remove(task.getTaskId());
                addNewDownloader(task);
            }
        } else {
            addNewDownloader(task);
        }
    }

    /**
     * 添加下载任务到下载队列
     *
     * @param task
     *         下载任务
     */
    private static void addNewDownloader(Downloader task) {
        DownloadBean downloadBean = new DownloadBean.Builder().downloader(task).build();
        downloadBean.setDownloadTask(new DownloadTask(downloadBean));
        downloadBean.setState(State.CREATE);
        TaskQueue.addTask(downloadBean);
    }

    /**
     * 添加下载任务到下载队列
     *
     * @param t
     *         下载任务
     */
    @SuppressWarnings("unchecked")
    public static <T> void addDownloader(T t) {
        if (t == null) {
            return;
        }
        if (t.getClass() == Downloader.class) {
            addDownloader((Downloader) t);
        } else if (t instanceof List) {
            List list = (List) t;
            if (list.size() > 0 && list.get(0).getClass() == Downloader.class) {
                for (Downloader downloader : (List<Downloader>) list) {
                    addDownloader(downloader);
                }
            } else {
                throw new IllegalArgumentException("argument of collect is only Downloader ");
            }
        } else {
            throw new IllegalArgumentException("argument is only Downloader or List ");
        }
    }

    /**
     * 移除任务
     *
     * @param t
     *         只支持String和List<String>
     *         String即为单个任务的Id
     *         List<String>为任务Id的集合
     */
    @SuppressWarnings("unchecked")
    public static <T> void remove(T t) {
        if (t == null) {
            return;
        }
        if (t.getClass() == String.class) {
            TaskManager.removeTask((String) t);
        } else if (t instanceof List) {
            List list = (List) t;
            if (list.size() > 0 && list.get(0).getClass() == String.class) {
                TaskManager.removeTaskList(list);
            } else {
                throw new IllegalArgumentException("argument of collect is only String ");
            }
        } else {
            throw new IllegalArgumentException("argument is only String or List ");
        }
    }

    /**
     * 移除所有下载任务
     */
    public static void removeAll() {
        List<DownloadBean> downloadBeanList = TaskQueue.queryDownloadBeans();
        for (DownloadBean downloadBean : downloadBeanList) {
            TaskManager.remove(downloadBean);
        }
        TaskQueue.removeAllTask();
    }

    /**
     * 暂停任务
     *
     * @param t
     *         只支持String和List<String>
     *         String即为单个任务的Id
     *         List<String>为任务Id的集合
     */
    @SuppressWarnings("unchecked")
    public static <T> void pause(T t) {
        if (t == null) {
            return;
        }
        if (t.getClass() == String.class) {
            TaskManager.pauseTask((String) t);
        } else if (t instanceof List) {
            List list = (List) t;
            if (list.size() > 0 && list.get(0).getClass() == String.class) {
                TaskManager.pauseTaskList(list);
            } else {
                throw new IllegalArgumentException("argument of collect is only String ");
            }
        } else {
            throw new IllegalArgumentException("argument is only String or List ");
        }
    }

    /**
     * 暂停所有在下载的任务
     */
    public static void pauseAll() {
        List<DownloadBean> downloadBeanList = TaskQueue.queryDownloadBeans();
        for (DownloadBean downloadBean : downloadBeanList) {
            TaskManager.pause(downloadBean);
        }
    }

    /**
     * 开始任务
     *
     * @param t
     *         只支持String和List<String>
     *         String即为单个任务的Id
     *         List<String>为任务Id的集合
     */
    @SuppressWarnings("unchecked")
    public static <T> void start(T t) {
        if (t == null) {
            return;
        }
        if (t.getClass() == String.class) {
            TaskManager.startTask((String) t);
        } else if (t instanceof List) {
            List list = (List) t;
            if (list.size() > 0 && list.get(0).getClass() == String.class) {
                TaskManager.startTaskList(list);
            } else {
                throw new IllegalArgumentException("argument of collect is only String ");
            }
        } else {
            throw new IllegalArgumentException("argument is only String or List ");
        }
    }

    /**
     * 开始所有任务
     */
    public static void startAll() {
        List<DownloadBean> downloadBeanList = TaskQueue.queryDownloadBeans();
        for (DownloadBean downloadBean : downloadBeanList) {
            TaskManager.start(downloadBean);
        }
    }

    /**
     * 设置监听
     *
     * @param taskId
     *         下载任务的id
     * @param observer
     *         下载任务状态监听
     */
    public static void subjectTask(String taskId, Observer observer) {
        ObserverManager.getInstance().registerObserver(taskId, observer);
    }

    /**
     * 移除监听
     *
     * @param taskId
     *         下载任务的id
     * @param observer
     *         下载任务状态监听 该对象必须与subjectTask方法中设置同一个 否则无法移除
     */
    public static void removeTaskObserver(String taskId, Observer observer) {
        ObserverManager.getInstance().removeObserver(taskId, observer);
    }

    /**
     * 获取任务信息
     *
     * @param taskId
     *         任务id
     *
     * @return 任务信息
     */
    public static Downloader getDownloader(String taskId) {
        DownloadBean bean = queryDownloadBean(taskId);
        if (bean != null) {
            return bean.getDownloader();
        }
        return null;
    }
}
