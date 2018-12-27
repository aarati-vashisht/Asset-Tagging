package com.assettagging.view.assetdisposer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.assettagging.MyApplication;
import com.assettagging.R;
import com.assettagging.controller.DataBaseHelper;
import com.assettagging.model.asset_disposal.AssetDisposal;
import com.assettagging.model.asset_disposal.CreatedDisposalList;
import com.assettagging.model.asset_disposal.UserAssetDisposal;
import com.assettagging.preference.Preferance;
import com.assettagging.view.custom_control.CustomDialogForMessages;
import com.assettagging.view.custom_control.CustomProgress;
import com.assettagging.view.navigation.NavigationActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreatedAssetsFragment extends Fragment {
    public static CreatedAssetsFragment instance;
    View view;
    @BindView(R.id.recyclerViewAllSchedules)
    RecyclerView recyclerViewAllSchedules;
    @BindView(R.id.textViewNoSchedule)
    TextView textViewNoSchedule;
    private YetToSubmitDisposerAdapter scheduleAdapter;
    boolean firstTime = false;
    String sortedDate = "";
    int mMonth, mYear, mDay;
    private DataBaseHelper dataBaseHelper;
    private Calendar myCalendar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_asset_disposer, container, false);
        ButterKnife.bind(this, view);
        instance = this;
        dataBaseHelper = new DataBaseHelper(getActivity());
        myCalendar = Calendar.getInstance();
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        return view;
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getAssetDisposalData();
            NavigationActivity.getInstance().menuitemfilter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(true);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.TOP | Gravity.RIGHT);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    View view = getLayoutInflater().inflate(R.layout.filter_meny, null);
                    dialog.setContentView(view, layoutParams);
                    final TextView textViewCreatedOn = dialog.findViewById(R.id.textViewCreatedOn);
                    final TextView textViewClearAllFileter = dialog.findViewById(R.id.textViewClearAllFileter);
                    if (!sortedDate.equals("")) {
                        textViewCreatedOn.setText(getResources().getString(R.string.createdon) + ": " + sortedDate);
                    }

                    textViewCreatedOn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {
                                            try {
                                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                                String stringDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                                Date newDate = format.parse(stringDate);
                                                sortedDate = sdf.format(newDate);
                                                textViewCreatedOn.setText(getResources().getString(R.string.createdon) + ": " + sortedDate);
                                                sortListAccToDate(sdf.format(newDate));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, mYear, mMonth, mDay);
                            datePickerDialog.show();
                        }
                    });
                    textViewClearAllFileter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            sortedDate = "";
                            textViewCreatedOn.setText(getResources().getString(R.string.createdon));
                            setAdapter(createddisposalList);
                            scheduleAdapter.sortList();
                        }
                    });

                    dialog.show();
                    return false;
                }
            });
        }
    }

    private void sortListAccToDate(String format) throws ParseException {
        List<CreatedDisposalList> tempList = new ArrayList<>();
        if (createddisposalList.size() > 0) {
            for (int i = 0; i < createddisposalList.size(); i++) {
                SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyyhh:mm:ss a");
                Date newDate = null;
                newDate = format2.parse(createddisposalList.get(i).getDisposalDate());
                SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy");
                if (format.equals(format1.format(newDate))) {
                    tempList.add(createddisposalList.get(i));
                }
            }
            setAdapter(tempList);
        }
    }

    public static CreatedAssetsFragment getInstance() {
        return instance;
    }

    public void getAssetDisposalData() {
        if (firstTime == false) {
            CustomProgress.startProgress(getActivity());
        }
        String userId = Preferance.getUserId(getActivity());
        UserAssetDisposal userAssetDisposal = new UserAssetDisposal(userId);
        Call<AssetDisposal> call = MyApplication.apiInterface.getGetDisposalSchedule(userAssetDisposal);
        call.enqueue(new Callback<AssetDisposal>() {
            @Override
            public void onResponse(Call<AssetDisposal> call, Response<AssetDisposal> response) {
                if (firstTime == false) {
                    CustomProgress.endProgress();
                }
                setScheduleResponse(response.body());

            }

            @Override
            public void onFailure(Call<AssetDisposal> call, Throwable t) {
                if (firstTime == false) {
                    CustomProgress.endProgress();
                }
                setScheduleResponse(null);
            }
        });
    }

    private void setScheduleResponse(AssetDisposal body) {
        if (body == null) {
            CustomDialogForMessages.showMessageAlert(getActivity(), getString(R.string.failure), getString(R.string.something_bad_happened));
        } else {
            if (body.getStatus().equals("success")) {
                createddisposalList.clear();
                createddisposalList.addAll(body.getCreatedDisposalList());
                setAdapter(body.getCreatedDisposalList());
                DisposerFragmnet.getInstance().CompledeDisposalAssetList.addAll(body.getSubmittedDisposalList());
                if (CompletedAssetsFragment.getInstance() != null) {
                    DisposerFragmnet.getInstance().CompledeDisposalAssetList.addAll(body.getSubmittedDisposalList());
                    CompletedAssetsFragment.getInstance().setAdapter(body.getSubmittedDisposalList());
                } else {
                    DisposerFragmnet.getInstance().CompledeDisposalAssetList.addAll(body.getSubmittedDisposalList());
                }

                CustomDialogForMessages.closeDialog();
            } else {
                CustomDialogForMessages.showMessageAlert(getActivity(), body.getStatus(), body.getMessage());
            }

        }
    }

    List<CreatedDisposalList> createddisposalList = new ArrayList<>();

    private void setAdapter(List<CreatedDisposalList> createdDisposalList) {
        if (createdDisposalList.size() > 0) {

            recyclerViewAllSchedules.setVisibility(View.VISIBLE);
            textViewNoSchedule.setVisibility(View.GONE);
            scheduleAdapter = new YetToSubmitDisposerAdapter(getActivity(), createdDisposalList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerViewAllSchedules.setLayoutManager(mLayoutManager);
            recyclerViewAllSchedules.setItemAnimator(new DefaultItemAnimator());
            recyclerViewAllSchedules.setAdapter(scheduleAdapter);
        } else {
            recyclerViewAllSchedules.setVisibility(View.GONE);
            textViewNoSchedule.setVisibility(View.VISIBLE);
        }
    }


}