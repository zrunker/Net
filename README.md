#### 类别：功能组件
#### 功能：网络请求封装
#### 名称：
  NetLib（XiZhi+功能+lib）
  ApplicationId：cc.ibooker.netlib

#### 依赖：
OkHttp3/Retrofit2/RxJava RxAndroid/Gson/Glide3

#### 混淆：
```
##---------------Begin: proguard configuration for OkHttp3  ----------

# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

# okio
-dontwarn okio.**
##---------------End: proguard configuration for OkHttp3  ----------




##---------------Begin: proguard configuration for Retrofit2  ----------

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

##---------------End: proguard configuration for Retrofit2  ----------




##---------------Begin: proguard configuration for RxJava RxAndroid  ----------

-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-dontnote rx.internal.util.PlatformDependent

##---------------End: proguard configuration for RxJava RxAndroid  ----------




##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

##---------------End: proguard configuration for Gson  ----------




##---------------Begin: proguard configuration for Glide v3  ----------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
##---------------End: proguard configuration for Glide v3  ----------


-keep class cc.ibooker.netlib.dto.** { *; }
```

#### 权限
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

#### 引用
在build.gradle中添加如下代码：
```
implementation project(':netlib')
```

或者
```
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
    implementation 'com.github.zrunker:Net:v1.0'
}
```
#### 使用
1、在Application中进行初始化
```
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ZNet.init(this, "http://ibooker.cc");
    }
}
```

2、构建接口类interface MyService
```
interface MyService {

    /**
     * 测试接口
     */
    @POST("user/test/")
    Observable<ResultData<NullData>> userTest(@Query("value") String value);

}
```

3、构建MyService实现类
```
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
```

4、使用
```
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
```