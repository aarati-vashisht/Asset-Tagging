package com.assettagging.model.all_data;

import com.assettagging.model.locationwise.ScheduleLocation;
import com.assettagging.model.schedule.Schedule;
import com.assettagging.model.schedule_detail.ItemCurentStatusList;
import com.assettagging.model.schedule_detail.ScheduleDetail_;
import com.assettagging.model.tasklocationwise.ScheduleLocationTask;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllData {

    @SerializedName("ItemCurentStatusList")
    @Expose
    private List<ItemCurentStatusList> itemCurentStatusList = null;
    @SerializedName("OngoingSchedule")
    @Expose
    private List<Schedule> ongoingSchedule = null;
    @SerializedName("ScheduleDetail")
    @Expose
    private List<ScheduleDetail_> scheduleDetail = null;
    @SerializedName("ScheduleLocationTask")
    @Expose
    private List<ScheduleLocationTask> scheduleLocationTask = null;
    @SerializedName("UpcomingSchedule")
    @Expose
    private List<Schedule> upcomingSchedule = null;
    @SerializedName("UserList")
    @Expose
    private List<UserList> userList = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("scheduleLocation")
    @Expose
    private List<ScheduleLocation> scheduleLocation = null;


    @SerializedName("ActvityCount")
    @Expose
    private List<ActvityCount> ActvityCount = null;

    public List<ActvityCount> getActvityCount() {
        return ActvityCount;
    }

    public void setActvityCount(List<ActvityCount> ActvityCount) {
        this.ActvityCount = ActvityCount;
    }



    @SerializedName("status")
    @Expose
    private String status;

    public List<ItemCurentStatusList> getItemCurentStatusList() {
        return itemCurentStatusList;
    }

    public void setItemCurentStatusList(List<ItemCurentStatusList> itemCurentStatusList) {
        this.itemCurentStatusList = itemCurentStatusList;
    }

    public List<Schedule> getOngoingSchedule() {
        return ongoingSchedule;
    }

    public void setOngoingSchedule(List<Schedule> ongoingSchedule) {
        this.ongoingSchedule = ongoingSchedule;
    }

    public List<ScheduleDetail_> getScheduleDetail() {
        return scheduleDetail;
    }

    public void setScheduleDetail(List<ScheduleDetail_> scheduleDetail) {
        this.scheduleDetail = scheduleDetail;
    }

    public List<ScheduleLocationTask> getScheduleLocationTask() {
        return scheduleLocationTask;
    }

    public void setScheduleLocationTask(List<ScheduleLocationTask> scheduleLocationTask) {
        this.scheduleLocationTask = scheduleLocationTask;
    }

    public List<Schedule> getUpcomingSchedule() {
        return upcomingSchedule;
    }

    public void setUpcomingSchedule(List<Schedule> upcomingSchedule) {
        this.upcomingSchedule = upcomingSchedule;
    }

    public List<UserList> getUserList() {
        return userList;
    }

    public void setUserList(List<UserList> userList) {
        this.userList = userList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ScheduleLocation> getScheduleLocation() {
        return scheduleLocation;
    }

    public void setScheduleLocation(List<ScheduleLocation> scheduleLocation) {
        this.scheduleLocation = scheduleLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}


