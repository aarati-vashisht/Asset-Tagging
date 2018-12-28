package com.assettagging.model.WebServer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.assettagging.MyApplication;
import com.assettagging.controller.Constants;
import com.assettagging.preference.Preferance;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class APIClient {

    private static Retrofit retrofit = null;
    private static Activity activity;



    public static final String ipaddressPrefrence = "IPAddress Prefrence";
    private static String BaseUrl = "http://192.168.200.199:804/service1.svc/";
    //  private static String BaseUrl = "http://180.179.221.43:8096/service1.svc/";
    //  private static String BaseUrl = "http://180.179.221.43:8096/service1.svc/";
    private static OkHttpClient client;

    public static Retrofit getClient() {
        Log.d("BaseUrl",BaseUrl);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (client != null) {
            client = null;
        }
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        client = okHttpClient.addInterceptor(interceptor).readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES).build();
        if (retrofit != null) {
            retrofit = null;
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit;
    }
}