package com.assettagging.view.assetdisposer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.assettagging.MyApplication;
import com.assettagging.R;
import com.assettagging.controller.CheckInternetConnection;
import com.assettagging.controller.Constants;
import com.assettagging.controller.DataBaseHelper;
import com.assettagging.model.asset_detai.AssetData;
import com.assettagging.model.asset_detai.BarcodeWiseDataList;
import com.assettagging.model.asset_detai.DisposalAssetData;
import com.assettagging.model.asset_detai.SaveAssets;
import com.assettagging.model.asset_detai.SaveDisposalTrack;
import com.assettagging.model.asset_detai.UserAssets;
import com.assettagging.model.asset_disposal.DisposalWiseDataList;
import com.assettagging.model.asset_disposal.UserDisposalSchedule;
import com.assettagging.model.login.ChangePassword;
import com.assettagging.model.login.UserChangePass;
import com.assettagging.preference.Preferance;
import com.assettagging.view.BaseActivity;
import com.assettagging.view.AddAssetLocation.BindLocationDisposalActivity;
import com.assettagging.view.custom_control.CustomDialogForMessages;
import com.assettagging.view.custom_control.CustomProgress;
import com.assettagging.view.custom_control.CustomToast;
import com.assettagging.view.login.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAssetDetailActivity extends BaseActivity {
    @BindView(R.id.recyclerViewScheduleDetail)
    RecyclerView recyclerViewScheduleDetail;
    @BindView(R.id.textViewNoSchedule)
    TextView textViewNoSchedule;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    static EditText editTextBarCode;
    private DataBaseHelper dataBaseHelper;
    private Button buttonSubmit;
    String SCHEDULE_NAME;
    String DISPOSAL_DATE;
    public String SCHEDULE_ID;
    private Dialog dialogChangePassword;
    private LinearLayout linearLayoutContainer;
    private MenuItem menuitem;
    private JsonArray ScannedList;
    private String Type;
    private MenuItem addMenu;
    private List<DisposalWiseDataList> disposalAssets = new ArrayList<>();
    private static AddAssetDetailActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_asset_detail);
        ButterKnife.bind(this);
        instance = this;
        dataBaseHelper = new DataBaseHelper(this);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        editTextBarCode = findViewById(R.id.editTextBarCode);
        getIntentData();
        setActionBarData();
        setclick();
        if (DisposerFragmnet.position == 0) {
            setAdapter(dataBaseHelper.getParticularDisposalAssets(SCHEDULE_ID));
        } else {
            getData();
        }
        onRefreshListener();
    }
    private void onRefreshListener() {
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckInternetConnection.isInternetConnected(getApplicationContext())) {
                    if (DisposerFragmnet.position == 0) {
                        setAdapter(dataBaseHelper.getParticularDisposalAssets(SCHEDULE_ID));
                    } else {
                        getData();
                    }
                } else {
                    CustomToast.showToast(AddAssetDetailActivity.this, getString(R.string.no_internet_connection));
                }
                pullToRefresh.setRefreshing(false);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (DisposerFragmnet.position == 0) {
            setAdapter(dataBaseHelper.getParticularDisposalAssets(SCHEDULE_ID));
        } else {
            getData();
        }
    }

    public static AddAssetDetailActivity getInstance() {
        return instance;
    }

    private void getData() {
        if (DisposerFragmnet.position != 0) {

            CustomProgress.startProgress(AddAssetDetailActivity.this);
            String userId = Preferance.getUserId(AddAssetDetailActivity.this);
            UserDisposalSchedule userAssetDisposal = new UserDisposalSchedule(userId, SCHEDULE_ID);
            Call<DisposalAssetData> call = MyApplication.apiInterface.GetDisposalScheduleDetail(userAssetDisposal);
            call.enqueue(new Callback<DisposalAssetData>() {
                @Override
                public void onResponse(Call<DisposalAssetData> call, Response<DisposalAssetData> response) {
                    CustomProgress.endProgress();
                    setResponse(response.body());

                }

                @Override
                public void onFailure(Call<DisposalAssetData> call, Throwable t) {
                    CustomProgress.endProgress();
                    setResponse(null);
                }
            });
        }

    }

    private void setResponse(DisposalAssetData body) {
        if (body == null) {
            CustomDialogForMessages.showMessageAlert(this, getString(R.string.failure), getString(R.string.something_bad_happened));
        } else {
            if (body.getStatus().equals("success")) {
                setAdapter(body.getBarcodeWiseDataList());
                CustomDialogForMessages.closeDialog();
            } else {
                CustomDialogForMessages.showMessageAlert(this, body.getStatus(), body.getMessage());
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_assets, menu);
        addMenu = menu.findItem(R.id.action_add_new);
        if (DisposerFragmnet.position == 0) {
            addMenu.setVisible(true);
        } else {
            addMenu.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                overridePendingTransition(0, 0);
                return true;
            case R.id.action_add_new:
                startActivity(new Intent(this, BindLocationDisposalActivity.class));
                return true;
            case R.id.action_logout:
                Preferance.clearPreference(this);
                dataBaseHelper.dropAssetandDisposalTable(); dataBaseHelper.dropTable();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.action_change_password:
                changePassword();
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
        linearLayoutContainer = dialogChangePassword.findViewById(R.id.linearLayoutContainer);

        edtoldpassword = dialogChangePassword.findViewById(R.id.edt_oldpassword);
        edtnewpassword = dialogChangePassword.findViewById(R.id.edt_oldnewpassword);
        edtconfirmpassword = dialogChangePassword.findViewById(R.id.edt_confirmpassword);

        tvChangepass = dialogChangePassword.findViewById(R.id.tv_changepassword);
        if (Preferance.getTheme(this).equals("ORANGE")) {
            linearLayoutContainer.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            edtoldpassword.setBackground(getResources().getDrawable(R.drawable.edittext_background_not_round,null));
            edtnewpassword.setBackground(getResources().getDrawable(R.drawable.edittext_background_not_round,null));
            edtconfirmpassword.setBackground(getResources().getDrawable(R.drawable.edittext_background_not_round,null));
            tvChangepass.setBackground(getResources().getDrawable(R.drawable.button_background,null));
        } else if (Preferance.getTheme(getApplicationContext()).equals("BLUE")) {
            linearLayoutContainer.setBackgroundColor(getResources().getColor(R.color.colorAccentBlue));
            edtoldpassword.setBackground(getResources().getDrawable(R.drawable.edittext_background_not_round_blue,null));
            edtnewpassword.setBackground(getResources().getDrawable(R.drawable.edittext_background_not_round_blue,null));
            edtconfirmpassword.setBackground(getResources().getDrawable(R.drawable.edittext_background_not_round_blue,null));
            tvChangepass.setBackground(getResources().getDrawable(R.drawable.button_background_blue,null));
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
        Call<ChangePassword> call = MyApplication.apiInterface.getChangePassword(userChangePass);
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

    private void setclick() {
        editTextBarCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0)
                    callService(s.toString());

            }
        });

    }

    boolean isAlreadyExistedINSameId = false;
    boolean isAlreadyExistedInDiffId = false;

    private void callService(String s) {

        final String userId = Preferance.getUserId(this);
        CustomProgress.startProgress(this);

        Call<AssetData> call = MyApplication.apiInterface.GetBarcodeWiseData(new UserAssets(userId, s.trim()));
        call.enqueue(new Callback<AssetData>() {
            @Override
            public void onResponse(Call<AssetData> call, Response<AssetData> response) {
                insertDataInDataBase(response);
                CustomProgress.endProgress();


            }

            @Override
            public void onFailure(Call<AssetData> call, Throwable t) {

                // insertDataInDataBase(null);
                CustomProgress.endProgress();

            }
        });


    }

    private void insertDataInDataBase(Response<AssetData> response) {
        if (response.body().getStatus().equals("success")) {
            if (response.body().getIsExist().equals("1")) {
                editTextBarCode.setText("");
                CustomToast.showToast(AddAssetDetailActivity.this, "QR Code Already Exists in Another Schedule");
            } else {
                List<BarcodeWiseDataList> tempList = dataBaseHelper.getAllDisposalAssets();
                for (int i = 0; i < tempList.size(); i++) {
                    if (response.body().getBarcodeWiseDataList().get(0).getASSETID().trim().equals(tempList.get(i).getASSETID()) && SCHEDULE_ID.equals(tempList.get(i).getSCHEDULEID())) {
                        isAlreadyExistedINSameId = true;
                    } else if (response.body().getBarcodeWiseDataList().get(0).getASSETID().trim().equals(tempList.get(i).getASSETID()) && !SCHEDULE_ID.equals(tempList.get(i).getSCHEDULEID())) {
                        isAlreadyExistedInDiffId = true;
                    }
                }
                if (isAlreadyExistedINSameId == true || isAlreadyExistedInDiffId == true) {
                    editTextBarCode.setText("");
                    if (isAlreadyExistedINSameId == true) {
                        CustomToast.showToast(AddAssetDetailActivity.this, "QR Code Already Exists");
                    } else {
                        CustomToast.showToast(AddAssetDetailActivity.this, "QR Code Already Exists in Another Schedule");
                    }
                } else {
                    dataBaseHelper.insertAssets(response.body().getBarcodeWiseDataList().get(0), SCHEDULE_ID);
                    setAdapter(dataBaseHelper.getParticularDisposalAssets(SCHEDULE_ID));
                    CustomProgress.endProgress();
                    editTextBarCode.setText("");
                }
            }

        } else if (response.body().getStatus().equals("failure")) {
            editTextBarCode.setText("");
            CustomToast.showToast(AddAssetDetailActivity.this, "QR Code Does Not Exist in DataBase");

        }
    }

    public void setAdapter(List<DisposalWiseDataList> disposalAssetsList) {
        if (disposalAssetsList.size() > 0) {
            disposalAssets.clear();
            disposalAssets.addAll(disposalAssetsList);
            pullToRefresh.setVisibility(View.VISIBLE);
            recyclerViewScheduleDetail.setVisibility(View.VISIBLE);
            if (DisposerFragmnet.position == 0) {
                buttonSubmit.setVisibility(View.VISIBLE);
            } else {
                buttonSubmit.setVisibility(View.GONE);
            }


            ArrayList<DisposalWiseDataList> values = new ArrayList<DisposalWiseDataList>();
            HashSet<DisposalWiseDataList> hashSet = new HashSet<DisposalWiseDataList>();
            hashSet.addAll(disposalAssetsList);
            values.clear();
            values.addAll(hashSet);

            textViewNoSchedule.setVisibility(View.GONE);
            DisposalAssetsAdapter disposalAssetsAdapter = new DisposalAssetsAdapter(this, values);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerViewScheduleDetail.setLayoutManager(mLayoutManager);
            recyclerViewScheduleDetail.setItemAnimator(new DefaultItemAnimator());
            recyclerViewScheduleDetail.setAdapter(disposalAssetsAdapter);
        } else {
            pullToRefresh.setVisibility(View.GONE);
            recyclerViewScheduleDetail.setVisibility(View.GONE);
            buttonSubmit.setVisibility(View.GONE);
            textViewNoSchedule.setVisibility(View.VISIBLE);
        }
    }

    private void getIntentData() {
        Type = getIntent().getStringExtra(Constants.TYPE);
        SCHEDULE_ID = getIntent().getStringExtra(Constants.SCHEDULE_ID);
        SCHEDULE_NAME = getIntent().getStringExtra(Constants.SCHEDULE_NAME);
        DISPOSAL_DATE = getIntent().getStringExtra(Constants.DISPOSAL_DATE);
        if (DisposerFragmnet.position == 0) {
            editTextBarCode.setVisibility(View.VISIBLE);
        } else {
            editTextBarCode.setVisibility(View.GONE);
        }

    }

    private void setActionBarData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.assetDisposer));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Preferance.getTheme(this).equals("ORANGE")) {
            editTextBarCode.setBackground(getResources().getDrawable(R.drawable.edittext_background_not_round,null));
            buttonSubmit.setBackground(getResources().getDrawable(R.drawable.button_background,null));
        } else if (Preferance.getTheme(this).equals("BLUE")) {
            editTextBarCode.setBackground(getResources().getDrawable(R.drawable.edittext_background_not_round_blue,null));
            buttonSubmit.setBackground(getResources().getDrawable(R.drawable.button_background_blue,null));
        }

    }

    public void showAlertForTwoButton(String message) {
        AlertDialog alertDialog = null;
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        alertDialog = new AlertDialog.Builder(
                this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();

        alertDialog.setMessage(message);

        // Setting Icon to Dialog

        // Setting OK Button
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Gson gson = new GsonBuilder().create();
                JsonArray myCustomArray = gson.toJsonTree(disposalAssets).getAsJsonArray();
                ScannedList = myCustomArray;
                if (ScannedList != null) {
                    if (ScannedList.size() > 0) {
                        String newString = ScannedList.toString().replace("\"", "\'");
                        String userId = Preferance.getUserId(AddAssetDetailActivity.this);
                        CustomProgress.startProgress(AddAssetDetailActivity.this);
                        SaveAssets saveAssets = new SaveAssets(userId, SCHEDULE_NAME, DISPOSAL_DATE, newString, Type);
                        Call<SaveDisposalTrack> call = MyApplication.apiInterface.getSaveDisposalTracking(saveAssets);
                        call.enqueue(new Callback<SaveDisposalTrack>() {
                            @Override
                            public void onResponse(Call<SaveDisposalTrack> call, Response<SaveDisposalTrack> response) {
                                CustomProgress.endProgress();
                                if (response.body().getMessage().contains("Asset Already Exist")) {
                                    CustomToast.showToast(AddAssetDetailActivity.this, response.body().getMessage());
                                } else {
                                    dataBaseHelper.dropASSETSTable(AddAssetDetailActivity.this, SCHEDULE_ID);
                                    dataBaseHelper.dropDisposerTable(AddAssetDetailActivity.this, SCHEDULE_ID);
                                    YetToSubmitDisposerFragment.getInstance().setAdapter(dataBaseHelper.getAllDisposedSchedule());
                                    if (CreatedAssetsFragment.getInstance() != null) {
                                        CreatedAssetsFragment.getInstance().getAssetDisposalData();
                                    }
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<SaveDisposalTrack> call, Throwable t) {
                                CustomProgress.endProgress();
                            }
                        });


                    } else {
                        CustomToast.showToast(AddAssetDetailActivity.this, "Please Scan QR Code");
                    }
                } else {
                    CustomToast.showToast(AddAssetDetailActivity.this, "QR Code not exist in this list");
                }
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

    public void onSubmitButtonClick(View view) {
        showAlertForTwoButton("Are you sure you want to submit?");
    }
}
