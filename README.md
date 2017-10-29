### android-downloader
###### **一个简单的支持多任务下载、多界面管理、可断点下载的网络下载器**

**1.下载器简介**

这个下载器是我在写AppStore的时候自己搞的玩意，当时为了让多界面同步更新下载所做的东西，但是后来想想觉得应该可以单独拿出来封装，以后用到了就可以直接使用，不需要再做修改什么了，所以才有了这个项目，下载器是以okhttp为基础的，不懂okhttp也没关系，不过最好还是熟悉下，网上资源很多的
嗯，那么说下它目前的功能吧：

* a.支持多任务下载，具体同时下载几个，最大添加几个任务都是自己可配的
* b.支持多界面监听某个任务，比如像应用市场，多个界面都可以同步下载数据
* c.支持断点下载，我只是实现下载的逻辑，具体怎么断点保存需要开发人员自己做

**2.项目引入**

> 在项目的 build.gradle 中加入</br>
>     repositories{</br>
>             ...</br>
>             maven {url 'https://jitpack.io' }</br>
>     ｝
> 
> 主模块 build.gradle 中引入</br>
>       compile 'com.github.Dpuntu:android-downloader:1.0.0' 
      
**3.使用下载器**

下载器提供了三个比较重要的类DownloadManager、Downloader和Observer</br>
其实很简单，我说下怎么用，大家就明白了</br>
比如说，我现在要下载一个视频叫【韩国美女.mov】,下载的地址是【http://www.dpuntu.com/movie/hgmv.mov】

*1.初始化下载器，一般在Application中初始化*
>         DownloadManager.getInstance().initDownloader(this);

*2.如果你的网络请求需要额外的配置，比如说请求头需要额外添加信息，你可以初始化OkHttpClient*
>         OkHttpClient client = new OkHttpClient.Builder()
>                                                 .addInterceptor(你的请求拦截器)
>                                                 .build();
当然如果你没有额外的要求，也可以不配置，这一步省略即可

*3.配置Downloader*
>         Downloader mDownloader =new Downloader.Builder()
>                             .client(client) //这就是第二部配置的OkHttpClient，你也可以不配置，下载器内部有个默认的OkHttpClient
>                             .fileName("韩国美女.mov")  // 这是你下载的文件需要存储的磁盘上的名字，必须设置项
>                             .filePath("xxx") // 设置文件存储的路径，可省略，默认为根目录下 Android/data/你的app applicationId/files
>                             .loadedSize(mLoadedSize)  // 这是下砸文件已经下载的大小，可以不设置，默认是0，如果是断点的话就必须设置，否则无法断点
>                             .totalSize(mTotalSize) // 这是下载文件的文件总大小，可以不设置，默认是0，如果是断点的话就必须设置，否则无法断点
>                             .taskId(taskId) //下载文件的任务对应的id，用于标识单一任务，不可重复，必须设置
>                             .url(“http://www.dpuntu.com/movie/hgmv.mov”) //下载文件的下载地址，必须配置
>                             .build();

*4.将第三步设置好的Downloader添加到任务列表中*
>         DownloadManager.getInstance().addDownloader(mDownloader);

*5.开始下载任务*
>         DownloadManager.getInstance().start(taskId); // 这个taskId就是设置Downloader时候的taskId

*6.如果你关心任务下载情况的话[一般都关心吧...]，可设置监听器*
>         DownloadManager.getInstance().subjectTask(taskId,mStartObserver);// 这个taskId就是设置Downloader时候的taskId ,  mStartObserver是对>应taskId的某一个Observer，下文具体分析Observer

从上文来看，建立任务到开始下载是一条链式结构，大概是4-6个步骤就完成了，而且DownloadManager、Downloader和Observer这三个都有使用到，DownloadManager、Downloader都好理解，大家应该都看得懂，下面讲解下Observer这个接口

**4.Observer介绍**

其实Observer就是观察者模式，也很简单，只是用起来要自己new一个而已，稍显复杂，但是当你在多界面监听莫一个任务的时候，你会发现Observer的好处，Observer一共有6个方法，下面逐一介绍[注意了  Observer的回掉均是在 子线程 中] 

* void onCreate (String taskId)
这个方法是在前面说到的第四步addDownloader的时候调用，但是请注意下，如果该任务是第一次创建，则不会触发该方法，所以如果有时候该方法没被调用，不必惊讶

* void onReady (String taskId)
这个方法是当你执行上面的第五步的start时候调用

* void onLoading (String taskId, String speed, long totalSize, long loadedSize)
正在下载过程中回掉</br>
说下参数 </br>
        taskId (下载任务的id，就是建立Downloader的时候配置的)</br>
        speed (下载速度)</br>
        totalSize (下载文件的总大小)</br>
        loadedSize (下载文件已经下载的大小)</br>
    返回totalSize和loadedSize是为了方便开发者做数据存储，方便以后断点，也可以用来配置下载的百分比

* void onPause(String taskId,  long totalSize, long loadedSize)
暂停时调用</br>
        参数含义同onLoading方法

* void onFinish(String taskId)
下载任务完成时调用

* void onError(String taskId, String error, long totalSize,  long loadedSize)
任务下载出现错误时候调用</br>
        参数含义同onLoading</br>
        参数 error 是错误的提示信息