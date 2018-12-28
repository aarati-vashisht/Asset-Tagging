package com.assettagging.view.assetdisposer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.assettagging.MyApplication;
import com.assettagging.R;
import com.assettagging.controller.CheckInternetConnection;
import com.assettagging.controller.DataBaseHelper;
import com.assettagging.model.asset_detai.BarcodeWiseDataList;
import com.assettagging.model.asset_detai.SaveAssets;
import com.assettagging.model.asset_detai.SaveDisposalTrack;
import com.assettagging.model.asset_disposal.DisposalWiseDataList;
import com.assettagging.model.schedule_detail.ItemCurentStatusList;
import com.assettagging.model.schedule_detail.ScheduleDetail_;
import com.assettagging.preference.Preferance;
import com.assettagging.view.custom_control.CustomProgress;
import com.assettagging.view.custom_control.CustomToast;
import com.assettagging.view.dashboard.DashBoardFragment;
import com.assettagging.view.schedule_detail.ScheduleDetailActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisposalAssetsAdapter extends RecyclerView.Adapter<DisposalAssetsAdapter.MyViewHolder> {

    public List<DisposalWiseDataList> disposalAssets;
    private Activity activity;


    public DisposalAssetsAdapter(Activity activity, List<DisposalWiseDataList> disposalAssets) {
        this.disposalAssets = disposalAssets;
        this.activity = activity;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.textViewAssetId)
        public TextView textViewAssetId;

        @BindView(R.id.textViewAssetName)
        public TextView textViewAssetName;
        @BindView(R.id.textViewAssetStatus)
        public TextView textViewAssetStatus;

        @BindView(R.id.imageViewAssetIcon)
        public ImageView imageViewAssetIcon;
        @BindView(R.id.imageViewClose)
        public ImageView imageViewClose;
        @BindView(R.id.card_view)
        public CardView card_view;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            if (Preferance.getTheme(activity).equals("ORANGE")) {
                card_view.setForeground(activity.getResources().getDrawable(R.drawable.cardview_background,null));
            } else if (Preferance.getTheme(activity).equals("BLUE")) {
                card_view.setForeground(activity.getResources().getDrawable(R.drawable.cardview_background_blue,null));
            }
        }


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rowadddisposal, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.textViewAssetId.setText(disposalAssets.get(position).getASSETID());
        holder.textViewAssetName.setText(disposalAssets.get(position).getNAME());
        holder.textViewAssetStatus.setText(disposalAssets.get(position).getStatus());
        if (DisposerFragmnet.position == 0) {
            holder.imageViewClose.setVisibility(View.VISIBLE);
        } else {
            holder.imageViewClose.setVisibility(View.GONE);
        }
        holder.imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForTwoButton("Are you sure you want to delete?", position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return disposalAssets.size();
    }

    public void showAlertForTwoButton(String message, final int position) {
        AlertDialog alertDialog = null;
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        alertDialog = new AlertDialog.Builder(
                activity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();

        alertDialog.setMessage(message);

        // Setting Icon to Dialog

        // Setting OK Button
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DataBaseHelper dataBaseHelper = new DataBaseHelper(activity);
                dataBaseHelper.dropASSETSROwTable(activity, AddAssetDetailActivity.getInstance().SCHEDULE_ID, disposalAssets.get(position).getASSETID());
                disposalAssets.remove(position);
                notifyDataSetChanged();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}