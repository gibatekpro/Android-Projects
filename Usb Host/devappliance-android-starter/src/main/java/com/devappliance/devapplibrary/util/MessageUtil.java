package com.devappliance.devapplibrary.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MessageUtil {
    public static void showMessage(String message, final Activity activity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setMessage(message)
                .show();
    }

    public static void showMessage(String message, String positiveButtonText, DialogInterface.OnClickListener onPositiveButtonClicked, Activity activity) {
        showMessage(null, message, positiveButtonText, onPositiveButtonClicked, activity);
    }

    public static void showMessage(String title, String message, String positiveButtonText, DialogInterface.OnClickListener onPositiveButtonClicked, Activity activity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message)
                .setPositiveButton(positiveButtonText, onPositiveButtonClicked)
                .show();
    }
}
