package cc.ibooker.net.service;


import cc.ibooker.netlib.dto.NullData;
import cc.ibooker.netlib.dto.ResultData;
import cc.ibooker.netlib.request.MySubscriber;
import cc.ibooker.netlib.service.ServiceCreateFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Retrofit 弊端对非JSON数据处理存在很多不便
 * OkHttp 弊端适合对键值对缓存（Get），不能对加密数据直接缓存
 * Created by 邹峰立 on 2016/9/17.
 */
public class HttpMethods {
    private MyService myService;

    // 构造方法私有
    private HttpMethods() {
        myService = ServiceCreateFactory.createRetrofitService(MyService.class);
    }

    //在访问HttpMethods时创建单例
    private static HttpMethods INSTANCE;

    //获取单例
    public static HttpMethods getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HttpMethods();
        }
        return INSTANCE;
    }

    /**
     * 测试接口
     */
    public void userTest(MySubscriber<ResultData<NullData>> subscriber, String value) {
        myService.userTest(value)
                //指定subscribe()发生在io调度器（读写文件、读写数据库、网络信息交互等）
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                //指定subscriber的回调发生在主线程
                .observeOn(AndroidSchedulers.mainThread())
                //实现订阅关系
                .subscribe(subscriber);
    }

}