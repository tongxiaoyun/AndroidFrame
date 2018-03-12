package com.risenb.expand;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.risenb.expand.floatwindow.interfaces.LayoutInitCallback;
import com.risenb.expand.loading.model.LoadingM;
import com.risenb.expand.network.MyOkHttp;
import com.risenb.expand.network.cookie.ClearableCookieJar;
import com.risenb.expand.network.cookie.PersistentCookieJar;
import com.risenb.expand.network.cookie.cache.SetCookieCache;
import com.risenb.expand.network.cookie.persistence.SharedPrefsCookiePersistor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * ================================================
 * 作    者：tongxiaoyun
 * 版    本：1.0
 * 创建日期：2017/3/6
 * 描    述：框架控制器
 * 修订历史：
 * ================================================
 */
public class m {
    private static m instance;
    private LoadingM loadingM;
    private View floatViewLayout;
    private LayoutInitCallback callback;
    private MyOkHttp netUtils;
    private Application application;

    private m() {
        loadingM = new LoadingM();
    }

    public static m getInstance() {
        if (instance == null) {
            instance = new m();
        }
        return instance;
    }

    public MyOkHttp getNetUtils() {
        if (netUtils == null) {
            com.risenb.expand.utils.Log.e("------------> you dont init");
        }
        return netUtils;
    }

    public Application getApplication() {
        return application;
    }

    /**
     * 初始化debug
     */
    public m initDebug(boolean debug) {
        com.risenb.expand.utils.Log.debug = debug;
        return instance;
    }

    public m setApplication(Application application) {
        this.application = application;
        return instance;
    }


    /**
     * 初始化网络控制器
     */
    public m initNetWorkWithCookie(Context context) {
        //持久化存储cookie
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));

        //log拦截器
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //自定义OkHttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)       //设置开启cookie
                .addInterceptor(logging)            //设置开启log
                .build();
        netUtils = new MyOkHttp(okHttpClient);
        return instance;

    }


    /**
     * 初始化网络控制器
     */
    public m initNetWorkDefault(Context context) {
        return initNetWorkDefault(context, 10000l, 10000l);
    }


    /**
     * 初始化网络控制器
     */
    public m initNetWorkDefault(Context context, long connetTimeout, long readTimeout) {
        //自定义OkHttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(connetTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .build();
        netUtils = new MyOkHttp(okHttpClient);
        return instance;

    }


    public LoadingM getLoadingM() {
        return loadingM;
    }

    public View getFloatView() {
        return floatViewLayout;
    }

    public void setFloatView(View floatViewLayout) {
        this.floatViewLayout = floatViewLayout;
    }

    public LayoutInitCallback getCallback() {
        return callback;
    }

    public void setCallback(LayoutInitCallback callback) {
        this.callback = callback;
    }
}
