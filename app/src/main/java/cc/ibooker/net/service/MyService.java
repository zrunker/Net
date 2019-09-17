package cc.ibooker.net.service;

import cc.ibooker.netlib.dto.NullData;
import cc.ibooker.netlib.dto.ResultData;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 请求数据接口
 * Created by 邹峰立 on 2016/9/17.
 */
interface MyService {

    /**
     * 测试接口
     */
    @POST("user/test/")
    Observable<ResultData<NullData>> userTest(@Query("value") String value);

}