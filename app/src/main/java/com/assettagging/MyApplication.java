package com.assettagging;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.assettagging.controller.CheckInternetConnection;
import com.assettagging.view.navigation.NavigationActivity;
import com.crashlytics.android.Crashlytics;
import com.assettagging.model.WebServer.APIClient;
import com.assettagging.model.WebServer.APIInterface;
import com.assettagging.preference.Preferance;
import com.assettagging.view.login.LoginActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import io.fabric.sdk.android.Fabric;

import static com.assettagging.view.navigation.NavigationActivity.handler;


public class MyApplication extends Application {

    public static APIInterface apiInterface;
    public static MyApplication instance;
    String ip = "", port = "";
    Handler handler;
    public static Context contextOfApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        contextOfApplication = getApplicationContext();
        instance = this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        if (Preferance.getTheme(getApplicationContext()).equals("ORANGE")) {
            changeTheme(R.style.AppTheme);
        } else if (Preferance.getTheme(getApplicationContext()).equals("BLUE")) {
            changeTheme(R.style.AppThemeBlue);
        }
        // getAllData();
    }

    private void getAllData() {
        handler = new Handler();
        try {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (CheckInternetConnection.isInternetConnected(getApplicationContext()))
                        if (NavigationActivity.getInstance() != null)
                            NavigationActivity.getInstance().getAllData();
                }
            }, 20000);
        } catch (Exception e) {
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                handler = null;
            }
            //handler = add_new Handler();
        }
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public void changeTheme(int appTheme) {
        this.setTheme(appTheme);
    }
}
