package com.wuzp.netlib.rpc;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author wuzhenpeng03
 */
public class RetrofitHelper {

    private static Retrofit retrofit;

    private static OkHttpClient httpClient;

    public static Retrofit getRetrofit() {
       if(retrofit == null){
           retrofit = new Retrofit.Builder()
               .baseUrl(FinalPath.BaseUrl)
               .client(getHttpClient())
               .addConverterFactory( GsonConverterFactory.create())
               .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
               .build();
       }
       return retrofit;
    }

    private static OkHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                //相关的一些设置项 超时时间 缓存 拦截器 等等
                .build();
        }

        return httpClient;
    }

}
