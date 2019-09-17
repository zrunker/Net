package cc.ibooker.net;

import android.app.Application;

import cc.ibooker.netlib.base.ZNet;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ZNet.init(this, "http://ibooker.cc");
    }
}
