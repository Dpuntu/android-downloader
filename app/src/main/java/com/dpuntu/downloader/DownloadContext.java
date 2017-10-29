package com.dpuntu.downloader;

import android.content.Context;

/**
 * Created on 2017/10/27.
 *
 * @author dpuntu
 */

class DownloadContext {

    private static Context sContext;

    protected static void setContext(Context context) {
        sContext = context;
    }

    protected static Context getContext() {
        return sContext;
    }
}
