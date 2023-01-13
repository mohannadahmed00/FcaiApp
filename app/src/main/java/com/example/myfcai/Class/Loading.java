package com.example.myfcai.Class;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.myfcai.R;

public class Loading {
    private Activity activity;
    private AlertDialog dialog;

    public Loading(Activity myAactivity) {
        activity = myAactivity;
    }

    public void startLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void dismissLoading() {
        dialog.dismiss();
    }
}
