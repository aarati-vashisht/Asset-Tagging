package com.assettagging.controller;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.assettagging.MyApplication;
import com.assettagging.model.all_data.ActvityCount;
import com.assettagging.model.all_data.AllData;
import com.assettagging.view.locationwise.LocationWiseActivity;
import com.assettagging.view.navigation.NavigationActivity;
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

    public NetworkHandleService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle extras = intent.getExtras();
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
            getAllData();
        }
    }

    private void getAllData() {
        Call<AllData> call = MyApplication.apiInterface.getAllData();
        call.enqueue(new Callback<AllData>() {
            @Override
            public void onResponse(Call<AllData> call, Response<AllData> response) {
                try {
                    setAllDataResponse(response.body());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //  progressBar2.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<AllData> call, Throwable t) {
                //  progressBar2.setVisibility(View.GONE);

                try {
                    setAllDataResponse(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setAllDataResponse(AllData body) throws IOException {
        if (body == null) {
            SaveDatainDataBase(null);
        } else {
            SaveDatainDataBase(body);

        }

    }

    private void SaveDatainDataBase(AllData body) throws IOException {
        if (body != null) {
            DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
            dbHelper.dropTable();
            dbHelper.insertUSER(body.getUserList());
            dbHelper.insertOngoingSCHEDULE(body.getOngoingSchedule());
            dbHelper.insertUpcomingSCHEDULE(body.getUpcomingSchedule());
            dbHelper.insertScheduleDetail(body.getScheduleDetail(), body.getItemCurentStatusList());
            dbHelper.insertScheduleLocationTask(body.getScheduleLocationTask());
            dbHelper.insertscheduleLocation(body.getScheduleLocation());
            dbHelper.updateScheduleDetail(body.getScheduleDetail());
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.Action_Count, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            List<ActvityCount> textList = new ArrayList<ActvityCount>();
            textList.addAll(body.getActvityCount());
            String jsonText = gson.toJson(textList);
            editor.putString(Constants.Action_Count, jsonText);
            editor.apply();
        }


    }

}
