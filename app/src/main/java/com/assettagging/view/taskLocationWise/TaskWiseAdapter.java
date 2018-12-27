package com.assettagging.view.taskLocationWise;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.assettagging.R;
import com.assettagging.controller.Constants;
import com.assettagging.model.tasklocationwise.ScheduleLocationTask;
import com.assettagging.view.schedule_detail.ScheduleDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskWiseAdapter extends RecyclerView.Adapter<TaskWiseAdapter.MyViewHolder> {

    private List<ScheduleLocationTask> locationWiseTasks;
    private Activity activity;
    String location;

    public TaskWiseAdapter(Activity activity, List<ScheduleLocationTask> locationWiseTasks, String location) {
        this.locationWiseTasks = locationWiseTasks;
        this.activity = activity;
        this.location = location;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewActivityType)
        public TextView textViewActivityType;
        @BindView(R.id.linearLayoutRow)
        public LinearLayout linearLayoutRow;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.textViewActivityType.setText(locationWiseTasks.get(position).getACTIVITYTYPE());
//        holder.textViewStartTime.setText(locationWiseTasks.get(position).getSTARTTIME());
//        holder.textViewEndTime.setText(locationWiseTasks.get(position).getSTARTTIME());
        if (locationWiseTasks.get(position).getACTIVITYTYPE().equals("Inspection")) {
            holder.linearLayoutRow.setBackground(activity.getResources().getDrawable(R.mipmap.inspection));
        }else if(locationWiseTasks.get(position).getACTIVITYTYPE().equals("tagging")) {
            holder.linearLayoutRow.setBackground(activity.getResources().getDrawable(R.mipmap.tagging_icon));
            holder.textViewActivityType.setText("Tagging");
        } else {
            holder.linearLayoutRow.setBackground(activity.getResources().getDrawable(R.mipmap.movement));
        }
        holder.linearLayoutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, ScheduleDetailActivity.class).putExtra(Constants.SCHEDULE_ID, locationWiseTasks.get(position).getSCHEDULEID()).putExtra(Constants.EmpID, locationWiseTasks.get(position).getEMPID()).putExtra(Constants.LOCATION, location).putExtra(Constants.ACTIVITYTYPE, locationWiseTasks.get(position).getACTIVITYTYPE()).putExtra(Constants.STARTTIME, locationWiseTasks.get(position).getSTARTTIME()).putExtra(Constants.ENDTIME, locationWiseTasks.get(position).getENDTIME()));
                activity.overridePendingTransition(0, 0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return locationWiseTasks.size();
    }
}