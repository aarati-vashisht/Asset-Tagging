package com.assettagging.view.schedule;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.assettagging.R;
import com.assettagging.controller.CheckInternetConnection;
import com.assettagging.controller.DataBaseHelper;
import com.assettagging.model.login.ChangePassword;
import com.assettagging.model.schedule.Schedule;
import com.assettagging.model.schedule.ScheduleData;
import com.assettagging.view.custom_control.CustomProgress;
import com.assettagging.view.navigation.NavigationActivity;
import com.assettagging.view.schedule.Completed.CompletedFragment;
import com.assettagging.view.schedule.Ongoing.OngoingFragment;
import com.assettagging.view.schedule.upcoming.UpcomingFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class ScheduleFragmnet extends Fragment {

    public static ScheduleFragmnet instance;
    @BindView(R.id.tabs)
    public
    TabLayout tabLayout;
    @BindView(R.id.pager)
    public
    ViewPager pager;


    private ScheduleAdapter scheduleAdapter;
    private Dialog dialogChangePassword;
    Call<ChangePassword> call;
    public static int position = 0;
    private DataBaseHelper dataBaseHelper;

    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_schedule, container, false);
        ButterKnife.bind(this, view);
        instance = this;
        CustomProgress.startProgress(getActivity());
        dataBaseHelper = new DataBaseHelper(getActivity());
        setupViewPager(NavigationActivity.getInstance().scheduleData, NavigationActivity.getInstance().firstTime);
        tabLayout.setupWithViewPager(ScheduleFragmnet.getInstance().pager);

        if (CheckInternetConnection.isInternetConnected(getActivity().getApplicationContext())) {
            NavigationActivity.getInstance().getScheduleData();
        } else {
            NavigationActivity.getInstance().getDataFromDatabase();
        }
        return view;
    }

    ScheduleData scheduleData;

    public void setupViewPager(ScheduleData body, boolean firstTime) {
        scheduleData = body;
        if (body != null) {
            if (body.getUpcomingSchedule() == null) {
                List<Schedule> schedules = new ArrayList<>();
                body.setUpcomingSchedule(schedules);
            }
            if (body.getOngoingSchedule() == null) {
                List<Schedule> schedules = new ArrayList<>();
                body.setOngoingSchedule(schedules);
            }
            if (body.getCompSchedule() == null) {
                List<Schedule> schedules = new ArrayList<>();
                body.setCompSchedule(schedules);
            }
            pager.removeAllViews();
            ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
            adapter.addFragment(new CompletedFragment(), getString(R.string.Completed), body.getCompSchedule());
            adapter.addFragment(new OngoingFragment(), getString(R.string.ongoing), body.getOngoingSchedule());
            adapter.addFragment(new UpcomingFragment(), getString(R.string.upcomung), body.getUpcomingSchedule());
            pager.setAdapter(adapter);
            pager.setOffscreenPageLimit(3);
            if (position == 0) {
                pager.setCurrentItem(0, true);
            } else if (position == 1) {
                pager.setCurrentItem(1, true);
            } else if (position == 1) {
                pager.setCurrentItem(2, true);
            }
            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    position = i;

                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }
        CustomProgress.endProgress();


    }

    public static ScheduleFragmnet getInstance() {
        return instance;
    }


}
