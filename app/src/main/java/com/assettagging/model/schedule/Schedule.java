package com.assettagging.model.schedule;


import android.support.annotation.NonNull;

import com.assettagging.view.assetdisposer.YetToSubmitDisposerAdapter;
import com.assettagging.view.assetdisposer.YetToSubmitDisposerFragment;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Schedule implements Serializable, Comparable<Schedule> {

    @SerializedName("ENDTIME")
    @Expose
    private String eNDTIME;
    @SerializedName("SCHEDULEDESCRIPTION")
    @Expose
    private String sCHEDULEDESCRIPTION;
    @SerializedName("SCHEDULEID")
    @Expose
    private String sCHEDULEID;
    @SerializedName("STARTTIME")
    @Expose
    private String sTARTTIME;
    @SerializedName("Status")
    @Expose
    private String Status;
    @SerializedName("Type")
    @Expose
    private String Type;
    @SerializedName("Sort")
    @Expose
    private boolean Sort;

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String empId) {
        EmpId = empId;
    }

    private String EmpId;
    public boolean getSort() {
        return Sort;
    }

    public void setSort(boolean Sort) {
        this.Sort = Sort;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getENDTIME() {
        return eNDTIME;
    }

    public void setENDTIME(String eNDTIME) {
        this.eNDTIME = eNDTIME;
    }

    public String getSCHEDULEDESCRIPTION() {
        return sCHEDULEDESCRIPTION;
    }

    public void setSCHEDULEDESCRIPTION(String sCHEDULEDESCRIPTION) {
        this.sCHEDULEDESCRIPTION = sCHEDULEDESCRIPTION;
    }

    public String getSCHEDULEID() {
        return sCHEDULEID;
    }

    public void setSCHEDULEID(String sCHEDULEID) {
        this.sCHEDULEID = sCHEDULEID;
    }

    public String getSTARTTIME() {
        return sTARTTIME;
    }

    public void setSTARTTIME(String sTARTTIME) {
        this.sTARTTIME = sTARTTIME;
    }

    @Override
    public int compareTo(@NonNull Schedule o) {
        if (YetToSubmitDisposerFragment.getInstance() != null) {
            if (YetToSubmitDisposerAdapter.sort == false) {
                return getSTARTTIME().compareTo(o.getSTARTTIME());
            } else {
                return o.getSTARTTIME().compareTo(getSTARTTIME());
            }
        } else {
            return getSTARTTIME().compareTo(o.getSTARTTIME());

        }


    }
}
