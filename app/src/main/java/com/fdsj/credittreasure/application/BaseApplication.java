package com.fdsj.credittreasure.application;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.activities.MainActivity;
import com.fdsj.credittreasure.constant.Constants;
import com.lzy.okgo.OkGo;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.utils.AppUtil;
import com.utils.Config;
import com.utils.FileUtil;
import com.utils.LogUtil;
import com.utils.Utilities;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.MemoryCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.payadd.majia.constant.ResourceConstants;
import cn.payadd.majia.entity.aistore.UserInfo;
import cn.payadd.majia.im.presentation.business.InitBusiness;
import cn.payadd.majia.im.tls.service.TLSService;
import cn.payadd.majia.im.tls.service.TlsBusiness;
import cn.payadd.majia.util.CertTrust;
import cn.payadd.majia.util.FileUtils;
import cn.payadd.majia.util.MyLifecycleHandler;
import cn.payadd.majia.util.ZIP;
import okhttp3.OkHttpClient;

import static android.support.v4.app.ActivityCompat.requestPermissions;

/**
 * Created by 冷佳兴 on 2016-08-16.
 */
public class BaseApplication extends Application {

    private static Application appContext;

    private static final String TAG = "Init";

    private static String SessionToKen, VersionName, MerchantKey;

    private static MainActivity mainActivity = null;

    private static IWXAPI iwxapi;

    private final int REQUEST_PHONE_PERMISSIONS = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        this.appContext = this;
        Config.application = this;
        CertTrust.allowAllSSL();
        setOkHttpUtils();
        SessionToKen = Utilities.getSessionToKen(this);
        LogUtil.info("SessionToKen", SessionToKen + "");
        MerchantKey = Utilities.getMerchantKey(this);
        LogUtil.info("MerchantKey", MerchantKey + "");
        VersionName = AppUtil.getVersionName(this);
        LogUtil.info("当前版本号", VersionName);
        ZXingLibrary.initDisplayOpinion(this);

        initCloudChannel(this);
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());

        OkGo.getInstance().init(this);

        regToWX();

        initImageLoader();

//        NldLoanManager.init(this);

        init();

        InputStream input = null;
        try {

            File resFile = new File(getApplicationContext().getExternalFilesDir(null),"app_res");
            if(!resFile.exists()){
                input = getAssets().open(ResourceConstants.RESOURCE_ZIP_NAME);
                File file = getApplicationContext().getExternalFilesDir(null);
                ZIP.UnZipFolderByInputStream(input,file.getAbsolutePath());
            }
            //读取资源包配置文件
            try {
                FileUtils.readResourceInfo(getApplicationContext().getExternalFilesDir(
                          ResourceConstants.RES_INFO_PATH).getAbsolutePath()
                        , ResourceConstants.RES_INFO_PATH_TEXT_NAME
                        ,this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化imageLoader
     */
    private void initImageLoader() {
        File dir  = FileUtil.createDir(Constants.SDCARD_CACHE_PATH);
//        HandlerThread

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.default_pic)
                .showImageOnFail(R.drawable.default_pic)
                .showImageOnLoading(R.drawable.default_pic)
                .considerExifParams(true) //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(800))
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
//                .discCache(new UnlimitedDiskCache(dir))
                .threadPriority(Thread.NORM_PRIORITY - 2).threadPoolSize(8)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new LRULimitedMemoryCache(2 * 1024 * 1024))
                .memoryCacheExtraOptions(1080, 1920)
                .memoryCacheSize(2 * 1024 * 1024)
                .defaultDisplayImageOptions(defaultOptions);
        ImageLoader.getInstance().init(builder.build());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Application getAppContext() {

        return appContext;
    }

    public static String getMerchantKey() {
        return MerchantKey;
    }

    //
    public static void setMerchantKey(String merchantKey) {
        MerchantKey = merchantKey;
    }

    public static String getSessionToKen() {
        return SessionToKen;
    }

    public static void setSessionToKen(String sessionToKen) {
        SessionToKen = sessionToKen;
    }

    public static String getVersionName() {
        return VersionName;
    }

    public static void setVersionName(String versionName) {
        VersionName = versionName;
    }

    public static IWXAPI getIwxapi() {
        return iwxapi;
    }

    private void regToWX() {
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);

        iwxapi.registerApp(Constants.APP_ID);
    }

    /**
     * 设置Okhttp
     */
    public void setOkHttpUtils() {
//        ClearableCookieJar cookieJar1 = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        CookieJarImpl cookieJar1 = new CookieJarImpl(new MemoryCookieStore());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor())
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .cookieJar(cookieJar1)
//                .hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                })
//                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    private List<Activity> activityList = new ArrayList<>();

    public void addActivity(Activity activity) {
        synchronized (this) {
            LogUtil.info("Activity", activity.getClass().getName());
            activityList.add(activity);
        }
    }

    public void remove(Activity activity) {
        for (int i = 0; i <= activityList.size(); i++) {
            if (activity == activityList.get(i)) {
                activityList.get(i).finish();
                activityList.remove(i);
            }
        }
    }

    public void removeAll() {
        for (int i = 0; i < activityList.size(); i++) {
            if (activityList.get(i) != null) {
                Log.v("BaseActivityRemoveAll", "关闭一次");
                activityList.get(i).finish();
            }
        }
        activityList.clear();
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        BaseApplication.mainActivity = mainActivity;
    }

    /**
     * 初始化云推送通道
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        SharedPreferences sp = getSharedPreferences(getClass().getName(), Context.MODE_PRIVATE);
        boolean receiveInstallmentPush = sp.getBoolean("receiveInstallmentPush", true);

        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "aliyun push -> init cloudchannel success");
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d(TAG, "aliyun push -> init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
        if(receiveInstallmentPush){
            pushService.turnOnPushChannel(null);
        }else{
            pushService.turnOffPushChannel(null);
        }
    }

    /**
     * 初始化IM
     */
    private void initPermission() {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if ((checkSelfPermission(Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)) permissionsList.add(Manifest.permission.READ_PHONE_STATE);
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)) permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionsList.size() == 0){
                init();
            }else{
                requestPermissions(getMainActivity(), permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_PHONE_PERMISSIONS);
            }
        }else{
            init();
        }
    }

    private void init(){
        SharedPreferences pref = appContext.getSharedPreferences("data", MODE_PRIVATE);
        int loglvl = pref.getInt("loglvl", TIMLogLevel.OFF.ordinal());
        //初始化IMSDK
        InitBusiness.start(BaseApplication.getAppContext(), loglvl);
        //初始化TLS
        TlsBusiness.init(BaseApplication.getAppContext());
        String id =  TLSService.getInstance().getLastUserIdentifier();
        UserInfo.getInstance().setId(id);
        UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));
    }
}


