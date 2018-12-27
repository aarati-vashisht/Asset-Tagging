package com.assettagging.model.WebServer;

import com.assettagging.model.Upadte_tag;
import com.assettagging.model.all_data.AllData;
import com.assettagging.model.assetList.assetslistModel;
import com.assettagging.model.asset_detai.AssetData;
import com.assettagging.model.asset_detai.DisposalAssetData;
import com.assettagging.model.asset_detai.SaveAssets;
import com.assettagging.model.asset_detai.SaveDisposalTrack;
import com.assettagging.model.asset_detai.UserAssets;
import com.assettagging.model.asset_disposal.AssetDisposal;
import com.assettagging.model.asset_disposal.UserAssetDisposal;
import com.assettagging.model.asset_disposal.UserDisposalSchedule;
import com.assettagging.model.locationwise.LocationWise;
import com.assettagging.model.locationwise.UserLocationWise;
import com.assettagging.model.login.ChangePassword;
import com.assettagging.model.login.ForgotUser;
import com.assettagging.model.login.Login;
import com.assettagging.model.login.LoginUser;
import com.assettagging.model.login.ResetUser;
import com.assettagging.model.login.UserChangePass;
import com.assettagging.model.schedule.ScheduleData;
import com.assettagging.model.schedule.UserSchedule;
import com.assettagging.model.schedule_detail.FinacialDimension;
import com.assettagging.model.schedule_detail.SaveTracking;
import com.assettagging.model.schedule_detail.ScheduleDetail;
import com.assettagging.model.schedule_detail.UserScannedList;
import com.assettagging.model.schedule_detail.UserScheduleDetail;
import com.assettagging.model.tasklocationwise.TaskLocationWise;
import com.assettagging.model.tasklocationwise.UserTaskLocationWise;
import com.assettagging.model.user_tracking.TrackingStatus;
import com.assettagging.model.user_tracking.TrackingStatus_;
import com.assettagging.model.user_tracking.UserAssetGroup;
import com.assettagging.model.user_tracking.UserAssetGroupDetail;
import com.assettagging.model.user_tracking.UserAssetGroupProjectWise;
import com.assettagging.model.user_tracking.UserLocation;
import com.assettagging.model.user_tracking.UserProjectLocationWise;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {
    @POST("LoginUser")
    Call<Login> doLogin(@Body LoginUser loginUser);

    @POST("GetScheduleData")
    Call<ScheduleData> getUserSchedule(@Body UserSchedule userSchedule);

    @POST("SendEmail")
    Call<ResetUser> doResetUser(@Body ForgotUser forgotUser);

    @POST("GetScheduleDetailData")
    Call<ScheduleDetail> getUserScheduleDetail(@Body UserScheduleDetail userScheduleDetail);


    @POST("BindLocation")
    Call<TrackingStatus_> getLocations(@Body UserLocation userLocation);

    @POST("ChangePassword")
    Call<ChangePassword> getChangePassword(@Body UserChangePass userChangePass);

    @POST("GetScheduleLocation")
    Call<LocationWise> getLocationWise(@Body UserLocationWise userLocationWise);

    @POST("GetScheduleTask")
    Call<TaskLocationWise> getTaskLocationWise(@Body UserTaskLocationWise userTaskLocationWise);

    @POST("SaveTracking")
    Call<SaveTracking> getSaveTracking(@Body UserScannedList userScheduleDetail);

    /////sent image
    @POST("GetBarcodeWiseData")
    Call<AssetData> GetBarcodeWiseData(@Body UserAssets userAssets);

    @POST("GetAllData")
    Call<AllData> getAllData();

    @POST("SaveDisposal")
    Call<SaveDisposalTrack> getSaveDisposalTracking(@Body SaveAssets saveAssets);

    @POST("GetDisposalSchedule")
    Call<AssetDisposal> getGetDisposalSchedule(@Body UserAssetDisposal userAssetDisposal);

    /////sent image
    @POST("GetBarcodeWiseProjectData")
    Call<AssetData> GetBarcodeWiseProjectData(@Body UserAssets userAssets);

    @POST("UpDateTagNo")
    Call<TrackingStatus> UpDateTagNo(@Body Upadte_tag upadte_tag);

    /////sent image
    @POST("GetDisposalScheduleDetail")
    Call<DisposalAssetData> GetDisposalScheduleDetail(@Body UserDisposalSchedule userAssetDisposal);

    @POST("GetProjectLocationWise")
    Call<TrackingStatus_> GetProjectLocationWise(@Body UserAssetGroupProjectWise userProjectLocationWise);

    @POST("GetAssetGroup")
    Call<TrackingStatus_> GetAssetGroup(@Body UserAssetGroup userAssetGroupProjectWise);

    @POST("GetAssetAssetGruopwise")
    Call<assetslistModel> GetAssetAssetGruopwise(@Body UserAssetGroupDetail userAssetGroupProjectWise);

    @POST("GetDimesionData")
    Call<FinacialDimension> GetDimesionData();
}