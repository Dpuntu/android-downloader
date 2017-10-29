package com.dpuntu.downloader;

/**
 * Created on 2017/10/27.
 *
 * @author dpuntu
 */

enum State {
    /**
     * 创建下载任务时
     */
    CREATE,
    /**
     * 准备就绪
     */
    READY,
    /**
     * 正在下载中
     */
    LOADING,
    /**
     * 暂停下载
     */
    PAUSE,
    /**
     * 下载完成
     */
    FINISH,
    /**
     * 下载错误
     */
    ERROR
}
