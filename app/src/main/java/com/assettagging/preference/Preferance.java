package com.assettagging.preference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.assettagging.controller.Constants;

public class Preferance {
    private static String SHARED_PREF_NAME = "ASSET_TAGGING_PREF";
    private static String USERNAME = "USERNAME";
    private static String USERID = "USERID";
    private static String THEME = "THEME";
    private static String SHARED_PREF_NAME_THEME = "THEME";
    private static String EMP_ID = "EMP_ID";
    public static String ALL_DATA = "ALL_DATA";
    private static String USER_LIST="USER_LIST";


    public static boolean saveUserName(Activity activity, String userName) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME, userName);
        editor.apply();
        return true;
    }

    public static String getUserName(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USERNAME, "");
    }


    public static boolean saveUserId(Activity activity, String userId) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERID, userId);
        editor.apply();
        return true;
    }

    public static String getUserId(Context activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USERID, "");
    }

    public static boolean saveEmpId(Activity activity, String empId) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMP_ID, empId);
        editor.apply();
        return true;
    }

    public static String getEmpId(Context activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(EMP_ID, "");
    }

    public static void clearPreference(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    public static boolean saveActionCount(Activity activity, String actionCounnt) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.Action_Count, actionCounnt);
        editor.apply();
        return true;
    }


    public static String getActionCount(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.Action_Count, "");
    }

    public static boolean saveTheme(Activity activity, String actionCounnt) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME_THEME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(THEME, actionCounnt);
        editor.apply();
        return true;
    }

    public static String getTheme(Context activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME_THEME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(THEME, "BLUE");
    }


    public static boolean saveAllDAta(Activity activity, String allData) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME_THEME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ALL_DATA, allData);
        editor.apply();
        return true;
    }

    public static String getAllDAta(Context activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME_THEME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ALL_DATA, "");
    }

    public static boolean setUserList(Activity activity, String userList) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME_THEME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_LIST, userList);
        editor.apply();
        return true;
    }

    public static String getUserList(Context activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME_THEME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_LIST, "");
    }

}