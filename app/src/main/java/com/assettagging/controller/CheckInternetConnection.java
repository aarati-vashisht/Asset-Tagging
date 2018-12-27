package com.assettagging.controller;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.assettagging.view.custom_control.CustomToast;

public class CheckInternetConnection extends BroadcastReceiver {
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that which service class will handle the intent.
        CustomToast.showToast((Activity) context,"NetworkConnected");
        ComponentName comp = new ComponentName(context.getPackageName(),
                NetworkHandleService.class.getName());
        intent.putExtra("isNetworkConnected", isInternetConnected(context));
        context.startService((intent.setComponent(comp)));
    }
}
