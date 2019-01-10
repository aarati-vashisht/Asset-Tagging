package com.assettagging.controller;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.assettagging.MyApplication;
import com.assettagging.model.all_data.ActvityCount;
import com.assettagging.model.all_data.AllData;
import com.assettagging.view.custom_control.CustomProgress;
import com.assettagging.view.dashboard.DashBoardFragment;
import com.assettagging.view.locationwise.LocationWiseActivity;
import com.assettagging.view.navigation.NavigationActivity;
import com.assettagging.view.schedule.ScheduleFragmnet;
import com.assettagging.view.schedule_detail.ScheduleDetailActivity;
import com.assettagging.view.taskLocationWise.TaskWiseActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkHandleService extends IntentService {

    private Handler handler;

    public NetworkHandleService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle extras = intent.getExtras();
        handler = new Handler();
        boolean isNetworkConnected = extras.getBoolean("isNetworkConnected");
        if (isNetworkConnected) {
            if (NavigationActivity.getInstance() != null) {
                NavigationActivity.getInstance().doWork();
                NavigationActivity.getInstance().getScheduleData();
            }
            if (LocationWiseActivity.instance != null) {
                LocationWiseActivity.getInstance().doWork();
                LocationWiseActivity.getInstance().getLocationData();
            }
            if (TaskWiseActivity.instance != null) {
                TaskWiseActivity.getInstance().doWork();
                TaskWiseActivity.getInstance().getTaskLocationWiseData();
            }
            if (ScheduleDetailActivity.instance != null) {
                ScheduleDetailActivity.getInstance().doWork();
                ScheduleDetailActivity.getInstance().getScheduleDetailData();
            }

        }
        getAllData();
    }

    private void getAllData() {
        NavigationActivity.getInstance().getAllData();
    }


}
