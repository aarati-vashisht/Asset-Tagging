package com.assettagging.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.assettagging.MyApplication;
import com.assettagging.R;
import com.assettagging.controller.CheckInternetConnection;
import com.assettagging.controller.DataBaseHelper;
import com.assettagging.model.schedule_detail.SaveTracking;
import com.assettagging.model.schedule_detail.ScheduleDetail_;
import com.assettagging.model.schedule_detail.UserScannedList;
import com.assettagging.preference.Preferance;
import com.assettagging.view.BaseActivity;
import com.assettagging.view.custom_control.CustomProgress;
import com.assettagging.view.login.LoginActivity;
import com.assettagging.view.navigation.NavigationActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar2;
    @BindView(R.id.relativeLayoutContainer)
    RelativeLayout relativeLayoutContainer;
    private CountDownTimer cdt;
    int i = 0;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        dbHelper = new DataBaseHelper(this);
        ButterKnife.bind(this);
        if (Preferance.getTheme(this).equals("ORANGE")) {
            relativeLayoutContainer.setBackgroundResource(R.mipmap.background);
        } else if (Preferance.getTheme(this).equals("BLUE")) {
            relativeLayoutContainer.setBackgroundResource(R.mipmap.background_blue);
        }

        if (CheckInternetConnection.isInternetConnected(getApplicationContext())) {
            progressBar.setVisibility(View.GONE);
            progressBar2.setVisibility(View.VISIBLE);
            doSplashWork();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.GONE);
            doAnimation();
        }

    }

    private void doSplashWork() {
//        Date c = Calendar.getInstance().getTime();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        String checkintime = df.format(c);
//        List<ScheduleDetail_> scheduleDetail_s = new ArrayList<>();
//        scheduleDetail_s.addAll(dbHelper.getAllAssets());
//        List<ScheduleDetail_> tempList = null;
//        for (int i = 0; i < scheduleDetail_s.size(); i++) {
//            tempList = new ArrayList<>();
//            for (int j = 0; j < scheduleDetail_s.size(); j++) {
//                if (scheduleDetail_s.get(i).getSCHEDULEID().equals(scheduleDetail_s.get(j).getSCHEDULEID())) {
//                    if (scheduleDetail_s.get(j).getTRACKING().equals("1")) {
//                        tempList.add(scheduleDetail_s.get(j));
//
//                    }
//
//                }
//            }
//            for (int k = 0; k < tempList.size(); k++) {
//                tempList.get(k).setITEMS("");
//            }
//            Gson gson = new GsonBuilder().create();
//            JsonArray myCustomArray = gson.toJsonTree(tempList).getAsJsonArray();
//            String newString = myCustomArray.toString().replace("\"", "\'");
//            String userId = Preferance.getUserId(this);
//            if (CheckInternetConnection.isInternetConnected(getApplicationContext())) {
//                UserScannedList userScheduleDetail = new UserScannedList(userId, "", checkintime, scheduleDetail_s.get(i).getSCHEDULEID(), newString);
//                Call<SaveTracking> call = MyApplication.apiInterface.getSaveTracking(userScheduleDetail);
//                call.enqueue(new Callback<SaveTracking>() {
//                    @Override
//                    public void onResponse(Call<SaveTracking> call, Response<SaveTracking> response) {
//                        CustomProgress.endProgress();
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<SaveTracking> call, Throwable t) {
//                        CustomProgress.endProgress();
//                    }
//                });
//            }
//
//        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final String userId = Preferance.getUserId(SplashActivity.this);
                if (userId.equals("")) {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();

                } else {
                    Intent i = new Intent(SplashActivity.this, NavigationActivity.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();

                }
                finish();
            }
        }, 3000);


    }


    private void doAnimation() {
        progressBar.setProgress(i);
        cdt = new CountDownTimer(2800, 26) {

            @Override
            public void onTick(long millisUntilFinished) {
                i++;
                progressBar.setProgress((int) (i));
            }

            @Override
            public void onFinish() {
                //Do what you want

                progressBar.setProgress(100);
                final String userId = Preferance.getUserId(SplashActivity.this);
                if (userId.equals("")) {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();

                } else {
                    Intent i = new Intent(SplashActivity.this, NavigationActivity.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();

                }
                finish();
            }
        };
        cdt.start();


    }


}
