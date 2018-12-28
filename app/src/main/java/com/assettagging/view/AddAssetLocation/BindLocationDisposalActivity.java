package com.assettagging.view.AddAssetLocation;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.assettagging.Banknameadapter;
import com.assettagging.MyApplication;
import com.assettagging.R;
import com.assettagging.controller.DataBaseHelper;
import com.assettagging.model.assetList.assetslistModel;
import com.assettagging.model.assetList.disposalassetlist;
import com.assettagging.model.user_tracking.Location;
import com.assettagging.model.user_tracking.TrackingStatus_;
import com.assettagging.model.user_tracking.UserAssetGroup;
import com.assettagging.model.user_tracking.UserAssetGroupDetail;
import com.assettagging.model.user_tracking.UserAssetGroupProjectWise;
import com.assettagging.model.user_tracking.UserLocation;
import com.assettagging.preference.Preferance;
import com.assettagging.view.BaseActivity;
import com.assettagging.view.assetdisposer.AddAssetDetailActivity;
import com.assettagging.view.custom_control.CustomProgress;
import com.assettagging.view.custom_control.CustomToast;
import com.assettagging.view.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.assettagging.controller.Constants.SCHEDULE_ID;

/**
 * Created by IACE on 29-Nov-18.
 */

public class BindLocationDisposalActivity extends BaseActivity {
    @BindView(R.id.recyclerViewData)
    RecyclerView recyclerViewData;
    @BindView(R.id.textViewLocation)
    TextView textViewLocation;
    @BindView(R.id.textViewProject)
    TextView textViewProject;
    @BindView(R.id.textViewAssetGroup)
    TextView textViewAssetGroup;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private DataBaseHelper dataBaseHelper;
    private MenuItem addMenu;
    private static BindLocationDisposalActivity instance;
    private GroupWIseAssetListAdapter groupWIseAssetListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        ButterKnife.bind(this);
        instance = this;
        dataBaseHelper = new DataBaseHelper(this);
        setActionBarData();
        getLocationData();
        onclickMethod();
    }

    public void banknamedialog(final List<Location> locations, final String string) {
        final Dialog dialog = new Dialog(BindLocationDisposalActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bankname_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.color.white);
        final ListView listsponser = dialog.findViewById(R.id.li_banknamelist);

        listsponser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (string.equals("Location")) {
                    if (textViewLocation.getText().toString().trim().length() > 0) {
                        textViewLocation.setText("");
                        textViewLocation.setText(locations.get(i).getLocationName());
                    }
                    LocationId = locations.get(i).getLocation();
                    textViewLocation.setText("");
                    textViewLocation.setText(locations.get(i).getLocationName());
                    getProjectData(locations.get(i).getLocation());
                } else if (string.equals("Project")) {
                    if (textViewProject.getText().toString().trim().length() > 0) {
                        textViewProject.setText("");
                        textViewProject.setText(locations.get(i).getLocationName());
                    }
                    textViewProject.setText("");
                    textViewProject.setText(locations.get(i).getLocationName());
                    ProjectId = locations.get(i).getLocation();
                    getAssetGroup(ProjectId, LocationId);

                } else if (string.equals("Asset Group")) {
                    if (textViewAssetGroup.getText().toString().trim().length() > 0) {
                        textViewAssetGroup.setText("");
                        textViewAssetGroup.setText(locations.get(i).getLocationName());
                    }
                    textViewAssetGroup.setText("");
                    textViewAssetGroup.setText(locations.get(i).getLocationName());
                    AssetGroupId = locations.get(i).getLocation();
                    getAssetGroupDetailData(locations.get(i).getLocation(), LocationId, ProjectId);
                }

                dialog.dismiss();

            }
        });

        final EditText edtname = dialog.findViewById(R.id.edt_searchbankname);
        final Banknameadapter adapter = new Banknameadapter(BindLocationDisposalActivity.this, locations);

        listsponser.setAdapter(adapter);

        edtname.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = edtname.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }
        });
        dialog.show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void onclickMethod() {
        textViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getLocation.size() == 0) {
                    getLocationData();
                } else {
                    banknamedialog(getLocation, "Location");
                }
            }
        });
        textViewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewLocation.getText().toString().trim().equals("")) {
                    CustomToast.showToast(BindLocationDisposalActivity.this, "Please select Location First");
                } else {
                    if (getProject.size() == 0) {
                        getProjectData(LocationId);

                    } else {
                        banknamedialog(getProject, "Project");
                    }
                }
            }
        });
        textViewAssetGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textViewLocation.getText().toString().trim().equals("")) {
                    CustomToast.showToast(BindLocationDisposalActivity.this, "Please select Location First");
                } else if (textViewProject.getText().toString().trim().equals("")) {
                    CustomToast.showToast(BindLocationDisposalActivity.this, "Please select Project First");
                } else {
                    if (getAssetGroup.size() == 0) {
                        getAssetGroup(ProjectId, LocationId);
                    } else {
                        banknamedialog(getAssetGroup, "Asset Group");
                    }
                }
            }
        });

    }

    public static BindLocationDisposalActivity getInstance() {
        return instance;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_assets, menu);
        addMenu = menu.findItem(R.id.action_add_new);
        addMenu.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(0, 0);
                return true;
            case R.id.action_logout:
                Preferance.clearPreference(this);
                dataBaseHelper.dropTable();
                dataBaseHelper.dropAssetandDisposalTable();
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.action_change_password:
                AddAssetDetailActivity.getInstance().changePassword();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setActionBarData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.addAsset));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    List<Location> getLocation = new ArrayList<>();
    List<Location> getProject = new ArrayList<>();
    List<Location> getAssetGroup = new ArrayList<>();

    public void getLocationData() {
        final String userId = Preferance.getUserId(this);
        CustomProgress.startProgress(this);

        Call<TrackingStatus_> call = MyApplication.apiInterface.getLocations(new UserLocation(userId));
        call.enqueue(new Callback<TrackingStatus_>() {
            @Override
            public void onResponse(Call<TrackingStatus_> call, Response<TrackingStatus_> response) {
                getLocation.clear();
                getLocation.addAll(response.body().getLocation());
                LocationId = getLocation.get(0).getLocation();
                textViewLocation.setText(getLocation.get(0).getLocationName());
                if (recyclerViewData.getVisibility() == View.VISIBLE) {
                    recyclerViewData.setVisibility(View.GONE);
                    buttonSubmit.setVisibility(View.GONE);
                }
                textViewProject.setText("");
                getProject.clear();
                getAssetGroup.clear();
                textViewAssetGroup.setText("");
                ProjectId = "";
                AssetGroupId = "";
                CustomProgress.endProgress();
            }

            @Override
            public void onFailure(Call<TrackingStatus_> call, Throwable t) {
                textViewProject.setText("");
                getProject.clear();
                getAssetGroup.clear();
                textViewAssetGroup.setText("");
                ProjectId = "";
                AssetGroupId = "";
                if (recyclerViewData.getVisibility() == View.VISIBLE) {
                    recyclerViewData.setVisibility(View.GONE);
                    buttonSubmit.setVisibility(View.GONE);
                }
                insertDataInDataBase(null);
                CustomProgress.endProgress();
            }
        });
    }

    String LocationId, ProjectId, AssetGroupId = "";

    private void insertDataInDataBase(final List<Location> response) {
        CustomToast.showToast(this, response.get(0).getLocationName());
        textViewLocation.setText(response.get(0).getLocationName());
        // getProjectData(response.body().getLocation().get(0).getLocation());


    }


    public void OnAssetGroupClick() {
//        if (textViewLocation.getText().toString().equals("")) {
//            CustomToast.showToast(this, "Please select Project");
//        } else if (textViewProject.getText().toString().equals("")) {
//            CustomToast.showToast(this, "Please select Location");
//        } else {
//            getAssetGroup(location.get(i).getLocation());
//        }
    }

    private void getAssetGroup(String projId, String locationId) {
        final String userId = Preferance.getUserId(this);
        CustomProgress.startProgress(this);
        Call<TrackingStatus_> call = MyApplication.apiInterface.GetAssetGroup(new UserAssetGroup(userId, projId, locationId));
        call.enqueue(new Callback<TrackingStatus_>() {
            @Override
            public void onResponse(Call<TrackingStatus_> call, Response<TrackingStatus_> response) {

                setAssetGroupResponse(response);
                CustomProgress.endProgress();


            }

            @Override
            public void onFailure(Call<TrackingStatus_> call, Throwable t) {
                setAssetGroupResponse(null);
                CustomProgress.endProgress();

            }
        });
    }

    private void getAssetGroupDetailData(String assetgroupId, String locationId, String projectId) {
        final String userId = Preferance.getUserId(this);
        CustomProgress.startProgress(this);
        Call<assetslistModel> call = MyApplication.apiInterface.GetAssetAssetGruopwise(new UserAssetGroupDetail(userId, assetgroupId,projectId,locationId));

        call.enqueue(new Callback<assetslistModel>() {
            @Override
            public void onResponse(Call<assetslistModel> call, Response<assetslistModel> response) {
                setAssetGroupDetailResponse(response);
                CustomProgress.endProgress();
            }

            @Override
            public void onFailure(Call<assetslistModel> call, Throwable t) {
                setAssetGroupDetailResponse(null);
                CustomProgress.endProgress();
            }
        });
    }

    private void setAssetGroupResponse(final Response<TrackingStatus_> response) {
        if (response == null) {
            CustomToast.showToast(this, getResources().getString(R.string.something_bad_happened));
        } else {
            if (response.body().getStatus().equals("success")) {
                if (response.body().getLocation().size() == 0) {
                    CustomToast.showToast(this, getResources().getString(R.string.no_data_found));
                } else {
                    getAssetGroup.clear();
                    getAssetGroup.addAll(response.body().getLocation());
                    textViewAssetGroup.setText(response.body().getLocation().get(0).getLocationName());
                    AssetGroupId = response.body().getLocation().get(0).getLocation();
                    getAssetGroupDetailData(AssetGroupId, LocationId, ProjectId);
                }
            } else {
                if (recyclerViewData.getVisibility() == View.VISIBLE) {
                    recyclerViewData.setVisibility(View.GONE);
                    buttonSubmit.setVisibility(View.GONE);
                }
                getAssetGroup.clear();
                textViewAssetGroup.setText("");
                AssetGroupId = "";
                CustomToast.showToast(this, getResources().getString(R.string.something_bad_happened));
            }
        }
    }

    private void callServiceforAssetDetail() {
        //  getAssetGroupDetailData(location.get(i).getLocation());
    }

    private void setAssetGroupDetailResponse(final Response<assetslistModel> response) {
        if (response == null) {
            CustomToast.showToast(this, getResources().getString(R.string.something_bad_happened));
        } else {
            if (response.body().getStatus().equals("success")) {
                recyclerViewData.setVisibility(View.VISIBLE);
                buttonSubmit.setVisibility(View.VISIBLE);
                setAdapter(response.body().getDisposalAssetList());
            } else {
                buttonSubmit.setVisibility(View.GONE);
                recyclerViewData.setVisibility(View.GONE);
                CustomToast.showToast(this, getResources().getString(R.string.something_bad_happened));
            }
        }
    }

    public void setAdapter(List<disposalassetlist> disposalAssetList) {
        groupWIseAssetListAdapter = new GroupWIseAssetListAdapter(this, disposalAssetList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewData.setLayoutManager(mLayoutManager);
        recyclerViewData.setItemAnimator(new DefaultItemAnimator());
        recyclerViewData.setAdapter(groupWIseAssetListAdapter);
    }

    public void OnLOcationClick() {
        if (textViewLocation.getText().toString().equals("")) {
            getLocationData();
        }
    }


    private void getProjectData(String location) {
        final String userId = Preferance.getUserId(this);
        CustomProgress.startProgress(this);
        Call<TrackingStatus_> call = MyApplication.apiInterface.GetProjectLocationWise(new UserAssetGroupProjectWise(userId, location));
        call.enqueue(new Callback<TrackingStatus_>() {
            @Override
            public void onResponse(Call<TrackingStatus_> call, Response<TrackingStatus_> response) {

                setResponse(response);
                CustomProgress.endProgress();


            }

            @Override
            public void onFailure(Call<TrackingStatus_> call, Throwable t) {
                setResponse(null);
                CustomProgress.endProgress();

            }
        });
    }

    private void setResponse(final Response<TrackingStatus_> response) {
        if (response == null) {
            CustomToast.showToast(this, getResources().getString(R.string.something_bad_happened));
        } else {
            if (response.body().getStatus().equals("success")) {
                if (response.body().getLocation().size() == 0) {
                    CustomToast.showToast(this, getResources().getString(R.string.no_data_found));
                } else {
                    getProject.clear();
                    getProject.addAll(response.body().getLocation());
                    ProjectId = response.body().getLocation().get(0).getLocation();
                    textViewProject.setText(response.body().getLocation().get(0).getLocationName());
                    if (recyclerViewData.getVisibility() == View.VISIBLE) {
                        recyclerViewData.setVisibility(View.GONE);
                        buttonSubmit.setVisibility(View.GONE);
                    }
                    getAssetGroup.clear();
                    textViewAssetGroup.setText("");
                    AssetGroupId = "";
                }
            } else {
                getAssetGroup.clear();
                textViewAssetGroup.setText("");
                AssetGroupId = "";
                if (recyclerViewData.getVisibility() == View.VISIBLE) {
                    recyclerViewData.setVisibility(View.GONE);
                    buttonSubmit.setVisibility(View.GONE);
                }
                getProject.clear();
                textViewProject.setText("");
                ProjectId = "";
                CustomToast.showToast(this, response.body().getMessage());

            }
        }
    }

    public void onSubmitButtonClick(View view) {
        if (GroupWIseAssetListAdapter.getInstance() != null) {
            if (groupWIseAssetListAdapter.checkedList.size() > 0) {
                for (int i = 0; i < groupWIseAssetListAdapter.checkedList.size(); i++) {
                    dataBaseHelper.insertAssets(groupWIseAssetListAdapter.checkedList.get(i), AddAssetDetailActivity.getInstance().SCHEDULE_ID);
                }
                AddAssetDetailActivity.getInstance().setAdapter(dataBaseHelper.getParticularDisposalAssets(SCHEDULE_ID));
                finish();
            } else {
                CustomToast.showToast(BindLocationDisposalActivity.this, "Please Select Assets");
            }
        } else {
            CustomToast.showToast(BindLocationDisposalActivity.this, getResources().getString(R.string.something_bad_happened));
        }
    }
}
