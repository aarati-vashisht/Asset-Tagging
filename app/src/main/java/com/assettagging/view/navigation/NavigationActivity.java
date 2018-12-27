package com.assettagging.view.navigation;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.assettagging.MyApplication;
import com.assettagging.R;
import com.assettagging.controller.CheckInternetConnection;
import com.assettagging.controller.Constants;
import com.assettagging.controller.DataBaseHelper;
import com.assettagging.model.all_data.ActvityCount;
import com.assettagging.model.all_data.AllData;
import com.assettagging.model.login.ChangePassword;
import com.assettagging.model.login.UserChangePass;
import com.assettagging.model.schedule.ScheduleData;
import com.assettagging.model.schedule.UserSchedule;
import com.assettagging.model.schedule_detail.SaveTracking;
import com.assettagging.model.schedule_detail.ScheduleDetail_;
import com.assettagging.model.schedule_detail.UserScannedList;
import com.assettagging.preference.Preferance;
import com.assettagging.view.BaseActivity;
import com.assettagging.view.assetdisposer.CompletedAssetsFragment;
import com.assettagging.view.assetdisposer.YetToSubmitDisposerFragment;
import com.assettagging.view.assetdisposer.CreatedAssetsFragment;
import com.assettagging.view.assetdisposer.DisposerFragmnet;
import com.assettagging.view.custom_control.CustomDialogForMessages;
import com.assettagging.view.custom_control.CustomProgress;
import com.assettagging.view.custom_control.CustomToast;
import com.assettagging.view.locationwise.LocationWiseActivity;
import com.assettagging.view.login.LoginActivity;
import com.assettagging.view.dashboard.DashBoardFragment;
import com.assettagging.view.schedule.ScheduleFragmnet;
import com.assettagging.view.schedule_detail.ScheduleDetailActivity;
import com.assettagging.view.tag_update.TagUpdatedFragment;
import com.assettagging.view.taskLocationWise.TaskWiseActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

public class NavigationActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    TextView textViewName;
    TextView textViewEmployeeName;
    LinearLayout linearLayoutNav;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;

    private DataBaseHelper dataBaseHelper;
    private Dialog dialogChangePassword;
    private Call<ChangePassword> call;
    public static boolean firstTime = false;
    public static Handler handler = new Handler();
    static NavigationActivity instance;
    private LinearLayout linearLayoutContainer;
    public static MenuItem menuitem;
    public static MenuItem menuitemfilter;
    private String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        UserId = Preferance.getUserId(this);
        View header = navigationView.getHeaderView(0);
        textViewEmployeeName = header.findViewById(R.id.textViewEmployeeName);
        textViewName = header.findViewById(R.id.textViewName);
        linearLayoutNav = header.findViewById(R.id.linearLayoutNav);
        scheduleData = new ScheduleData();
        instance = this;
        dataBaseHelper = new DataBaseHelper(this);
        setAppBar();
        showFragment(new DashBoardFragment(), getString(R.string.dashboard));
        navigationView.getMenu().getItem(0).setChecked(true);
        if (CheckInternetConnection.isInternetConnected(getApplicationContext())) {
            if (DashBoardFragment.getInstance() != null || ScheduleFragmnet.getInstance() != null)
                getScheduleData();
        } else {
            getDataFromDatabase();
        }

        onRefreshListener();
    }

    public void getDataFromDatabase() {
        Gson gson = new Gson();
        String json = Preferance.getAllDAta(NavigationActivity.this);
        AllData allData = gson.fromJson(json, AllData.class);
        for (int i = 0; i < allData.getOngoingSchedule().size(); i++) {
            if (allData.getOngoingSchedule().get(0).getEmpId().equals(UserId)) {
            }
        }
        for (int i = 0; i < allData.getUpcomingSchedule().size(); i++) {
            if (allData.getUpcomingSchedule().get(0).getEmpId().equals(UserId)) {
            }
        }
        scheduleData.setActvityCount(allData.getActvityCount());
        scheduleData.setUpcomingSchedule(allData.getUpcomingSchedule());
        scheduleData.setOngoingSchedule(allData.getOngoingSchedule());
        setupViewPager(scheduleData);
    }

    public static NavigationActivity getInstance() {
        return instance;
    }

    public void doWork() {
        DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String checkintime = df.format(c);
        List<ScheduleDetail_> scheduleDetail_s = new ArrayList<>();
        scheduleDetail_s.addAll(dbHelper.getAllAssets());
        List<ScheduleDetail_> tempList = null;
        for (int i = 0; i < scheduleDetail_s.size(); i++) {
            tempList = new ArrayList<>();
            for (int j = 0; j < scheduleDetail_s.size(); j++) {
                if (scheduleDetail_s.get(i).getSCHEDULEID().equals(scheduleDetail_s.get(j).getSCHEDULEID())) {
                    tempList.add(scheduleDetail_s.get(j));

                }

            }
            for (int j = 0; j < tempList.size(); j++) {
                tempList.get(j).setITEMS("");
            }
            Gson gson = new GsonBuilder().create();
            JsonArray myCustomArray = gson.toJsonTree(tempList).getAsJsonArray();
            String newString = myCustomArray.toString().replace("\"", "\'");
            String userId = Preferance.getUserId(getApplicationContext());
            if (CheckInternetConnection.isInternetConnected(getApplicationContext())) {
                UserScannedList userScheduleDetail = new UserScannedList(userId, "", checkintime, scheduleDetail_s.get(i).getSCHEDULEID(), newString);
                Call<SaveTracking> call = MyApplication.apiInterface.getSaveTracking(userScheduleDetail);
                call.enqueue(new Callback<SaveTracking>() {
                    @Override
                    public void onResponse(Call<SaveTracking> call, Response<SaveTracking> response) {
                        CustomProgress.endProgress();

                    }

                    @Override
                    public void onFailure(Call<SaveTracking> call, Throwable t) {
                        CustomProgress.endProgress();
                    }
                });
            }

        }
    }

    private void onRefreshListener() {
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckInternetConnection.isInternetConnected(getApplicationContext())) {
                    if (handler != null) {
                        handler.removeCallbacksAndMessages(null);
                        handler = null;
                    }
                    if (DashBoardFragment.getInstance() != null || ScheduleFragmnet.getInstance() != null)
                        if (ScheduleDetailActivity.getInstance() == null)
                            getScheduleData();
                    if (CreatedAssetsFragment.getInstance() != null) {
                        CreatedAssetsFragment.getInstance().getAssetDisposalData();
                    }
                } else {
                }
                pullToRefresh.setRefreshing(false);

            }
        });
    }

    public void getScheduleData() {
        if (!firstTime) {
            CustomProgress.startProgress(NavigationActivity.this);
        }
        String userId = Preferance.getUserId(NavigationActivity.this);
        UserSchedule userSchedule = new UserSchedule(userId);
        Call<ScheduleData> call = MyApplication.apiInterface.getUserSchedule(userSchedule);
        call.enqueue(new Callback<ScheduleData>() {
            @Override
            public void onResponse(Call<ScheduleData> call, Response<ScheduleData> response) {
                if (!firstTime) {
                    CustomProgress.endProgress();
                }
                setScheduleResponse(response.body());

            }

            @Override
            public void onFailure(Call<ScheduleData> call, Throwable t) {
                if (!firstTime) {
                    CustomProgress.endProgress();
                }

                setScheduleResponse(null);
            }
        });
    }


    private void setScheduleResponse(ScheduleData body) {
        if (body == null) {
            if (!firstTime) {
                CustomDialogForMessages.showMessageAlert(NavigationActivity.this, getString(R.string.failure), getString(R.string.something_bad_happened));
            }
        } else {
            Gson gson = new Gson();
            Type type = new TypeToken<List<ActvityCount>>() {
            }.getType();
            String json = gson.toJson(body.getActvityCount(), type);
            Preferance.saveActionCount(this, json.toString());
            setupViewPager(body);

            if (handler == null) {
                handler = new Handler();
                if (CheckInternetConnection.isInternetConnected(getApplicationContext())) {
                    doTheAutoRefresh();
                }
            }
            if (body.getStatus().equals("success")) {
                CustomDialogForMessages.closeDialog();
                firstTime = true;
            } else {
                if (!firstTime) {
                    CustomDialogForMessages.showMessageAlert(NavigationActivity.this, body.getStatus(), body.getMessage());
                }
                firstTime = true;

            }

        }
    }

    public ScheduleData scheduleData;

    public ScheduleData setupViewPager(ScheduleData body) {
        scheduleData = body;
        if (ScheduleFragmnet.getInstance() != null) {
            ScheduleFragmnet.getInstance().setupViewPager(scheduleData, firstTime);
        }
        if (DashBoardFragment.getInstance() != null) {
            DashBoardFragment.getInstance().setdata(scheduleData);
        }

        return scheduleData;
    }

    public void doTheAutoRefresh() {
        if (CheckInternetConnection.isInternetConnected(getApplicationContext())) {
            CustomDialogForMessages.closeDialog();
            try {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (DashBoardFragment.getInstance() != null || ScheduleFragmnet.getInstance() != null)
                            if (ScheduleDetailActivity.getInstance() == null) getScheduleData();
                        CustomProgress.endProgress();
                        doTheAutoRefresh();


                    }
                }, 10000);
            } catch (Exception e) {
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                    handler = null;
                }
                //handler = add_new Handler();
            }
        } else {
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                handler = null;
            }
        }
    }


    private void setAppBar() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                if (newState == drawer.getMeasuredState()) {
                    if (DashBoardFragment.getInstance() != null) {
                        if (DashBoardFragment.getInstance().popupWindow != null) {
                            DashBoardFragment.getInstance().popupWindow.dismiss();
                            DashBoardFragment.getInstance().popupWindow = null;
                            DashBoardFragment.getInstance().opened = false;
                        }
                    }
                }
            }
        });
        if (Preferance.getTheme(getApplicationContext()).equals("ORANGE")) {
            linearLayoutNav.setBackground(getResources().getDrawable(R.drawable.nav_header_drawable));
        } else if (Preferance.getTheme(getApplicationContext()).equals("BLUE")) {
            linearLayoutNav.setBackground(getResources().getDrawable(R.drawable.nav_header_drawable_blue));
        }
        navigationView.setNavigationItemSelectedListener(this);
        //  textViewName.setText(Preferance.getUserName(this));
        textViewEmployeeName.setText(Preferance.getUserName(this));
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    @Override
    public void onPause() {
        Log.d("onPause", "");
        super.onPause();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    @Override
    public void onResume() {
        Log.d("onResume", "");
        super.onResume();
        handler = new Handler();
        if (CheckInternetConnection.isInternetConnected(getApplicationContext())) {
            doTheAutoRefresh();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        menuitem = menu.findItem(R.id.action_add_new);
        menuitemfilter = menu.findItem(R.id.action_filter);

        menuitemfilter.setVisible(false);
        menuitem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Preferance.clearPreference(this);
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.action_change_password:
                if (CheckInternetConnection.isInternetConnected(getApplicationContext())) {
                    changePassword();
                } else {
                    CustomToast.showToast(this, getString(R.string.if_you_want_to_change_pass_connect_net));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void changePassword() {
        final String userId = Preferance.getUserId(this);

        final EditText edtoldpassword, edtnewpassword, edtconfirmpassword;
        Button tvChangepass;

        dialogChangePassword = new Dialog(this);
        dialogChangePassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogChangePassword.setContentView(R.layout.dialog_changepassword);

        Window window = dialogChangePassword.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        edtoldpassword = dialogChangePassword.findViewById(R.id.edt_oldpassword);
        edtnewpassword = dialogChangePassword.findViewById(R.id.edt_oldnewpassword);
        edtconfirmpassword = dialogChangePassword.findViewById(R.id.edt_confirmpassword);
        linearLayoutContainer = dialogChangePassword.findViewById(R.id.linearLayoutContainer);
        tvChangepass = dialogChangePassword.findViewById(R.id.tv_changepassword);
        if (Preferance.getTheme(this).equals("ORANGE")) {
            linearLayoutContainer.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            edtoldpassword.setBackground(getResources().getDrawable(R.drawable.edittext_background_not_round));
            edtnewpassword.setBackground(getResources().getDrawable(R.drawable.edittext_background_not_round));
            edtconfirmpassword.setBackground(getResources().getDrawable(R.drawable.edittext_background_not_round));
            tvChangepass.setBackground(getResources().getDrawable(R.drawable.button_background));
        } else if (Preferance.getTheme(getApplicationContext()).equals("BLUE")) {
            linearLayoutContainer.setBackgroundColor(getResources().getColor(R.color.colorAccentBlue));
            edtoldpassword.setBackground(getResources().getDrawable(R.drawable.edittext_background_not_round_blue));
            edtnewpassword.setBackground(getResources().getDrawable(R.drawable.edittext_background_not_round_blue));
            edtconfirmpassword.setBackground(getResources().getDrawable(R.drawable.edittext_background_not_round_blue));
            tvChangepass.setBackground(getResources().getDrawable(R.drawable.button_background_blue));
        }
        tvChangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtoldpassword.getText().toString().trim().equals("")) {
                    edtoldpassword.setError(getString(R.string.fill_old_pass));
                } else if (edtnewpassword.getText().toString().trim().equals("")) {
                    edtnewpassword.setError(getString(R.string.fill_new_password));
                } else if (edtconfirmpassword.getText().toString().trim().equals("")) {
                    edtconfirmpassword.setError(getString(R.string.fill_confirm_pass));
                } else if (!edtnewpassword.getText().toString().trim().equals(edtconfirmpassword.getText().toString().trim())) {
                    edtconfirmpassword.setError(getString(R.string.password_not_motached));
                } else {
                    changepassword(userId, edtnewpassword.getText().toString().trim());
                }
            }

        });
        dialogChangePassword.show();
    }

    private void changepassword(String UserId, String Password) {
        CustomProgress.startProgress(this);
        UserChangePass userChangePass = new UserChangePass(UserId, Password);
        if (call != null) {
            call.cancel();
            call = null;
        }
        call = MyApplication.apiInterface.getChangePassword(userChangePass);
        call.enqueue(new Callback<ChangePassword>() {
            @Override
            public void onResponse(Call<ChangePassword> call, Response<ChangePassword> response) {
                CustomProgress.endProgress();
                setChangePasswordResponse(response.body());
            }

            @Override
            public void onFailure(Call<ChangePassword> call, Throwable t) {
                CustomProgress.endProgress();
                setChangePasswordResponse(null);

            }
        });
    }

    private void setChangePasswordResponse(ChangePassword body) {
        if (body != null) {
            dialogChangePassword.dismiss();
            if (body.getStatus().equals("Success")) {
                CustomDialogForMessages.showMessageAlert(this, body.getStatus(), body.getMessage());
            } else {
                CustomDialogForMessages.showMessageAlert(this, body.getStatus(), body.getMessage());
            }
        } else {
            CustomDialogForMessages.showMessageAlert(this, getString(R.string.failure), getString(R.string.something_bad_happened));
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (DashBoardFragment.getInstance() != null) {
            if (DashBoardFragment.getInstance().popupWindow != null) {
                DashBoardFragment.getInstance().popupWindow.dismiss();
                DashBoardFragment.getInstance().popupWindow = null;
                DashBoardFragment.getInstance().opened = false;
            }
        }
        int id = item.getItemId();
        if (id == R.id.nav_Dashboard) {
            showFragment(new DashBoardFragment(), getString(R.string.dashboard));
            ScheduleFragmnet.instance = null;
            DisposerFragmnet.instance = null;
            YetToSubmitDisposerFragment.instance = null;
            CreatedAssetsFragment.instance = null;
            CompletedAssetsFragment.instance = null;
            menuitem.setVisible(false);
        } else if (id == R.id.nav_Schedules) {
            showFragment(new ScheduleFragmnet(), getString(R.string.daily_schedule));
            DisposerFragmnet.instance = null;
            DashBoardFragment.instance = null;
            YetToSubmitDisposerFragment.instance = null;
            CreatedAssetsFragment.instance = null;
            CompletedAssetsFragment.instance = null;
            menuitem.setVisible(false);
        }
// else if (id == R.id.nav_theme) {
//            showFragment(add_new ThemesFragment(), getString(R.string.theme));
//            DashBoardFragment.instance = null;
//            ScheduleFragmnet.instance = null;
//            DisposerFragmnet.instance = null;
//            YetToSubmitDisposerFragment.instance = null;
//            CreatedAssetsFragment.instance = null;
//            menuitem.setVisible(false);
//        }
        else if (id == R.id.nav_disposer) {
            showFragment(new DisposerFragmnet(), getString(R.string.assetDisposer));
            DashBoardFragment.instance = null;
            ScheduleFragmnet.instance = null;
            menuitem.setVisible(true);
        } else if (id == R.id.tag_update) {
            showFragment(new TagUpdatedFragment(), getString(R.string.tagupdate));
            DashBoardFragment.instance = null;
            ScheduleFragmnet.instance = null;
            DisposerFragmnet.instance = null;
            YetToSubmitDisposerFragment.instance = null;
            CreatedAssetsFragment.instance = null;
            CompletedAssetsFragment.instance = null;
            menuitem.setVisible(false);
            menuitemfilter.setVisible(false);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(Fragment fragment, String title) {
        getSupportActionBar().setTitle(title);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

}
