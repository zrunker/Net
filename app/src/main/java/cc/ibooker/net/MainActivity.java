package cc.ibooker.net;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cc.ibooker.net.service.HttpMethods;
import cc.ibooker.netlib.dto.ErrorData;
import cc.ibooker.netlib.dto.NullData;
import cc.ibooker.netlib.dto.ResultData;
import cc.ibooker.netlib.request.MySubscriber;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {
    private MySubscriber<ResultData<NullData>> subscriber;
    private CompositeSubscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 请求数据
        subscriber = new MySubscriber<ResultData<NullData>>() {
            @Override
            protected void onError(ErrorData errorData) {

            }

            @Override
            protected void onLogin(ErrorData errorData) {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(ResultData<NullData> nullDataResultData) {

            }
        };
        HttpMethods.getInstance().userTest(subscriber, "111");
        if (mSubscription == null)
            mSubscription = new CompositeSubscription();
        mSubscription.add(subscriber);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscriber != null)
            subscriber.unsubscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null)
            mSubscription.unsubscribe();
    }
}
