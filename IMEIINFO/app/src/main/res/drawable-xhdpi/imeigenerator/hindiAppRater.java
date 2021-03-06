package com.gibatekpro.imeigenerator;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by Anthony Gibah on 6/30/2017.
 */
public class hindiAppRater {
    private final static String APP_TITLE = "IMEI GENERATOR";
    private final static String APP_PACKAGE_NAME = "com.gibatekpro.imeigenerator";

    //Initialized to 0 and 3 only for test purposes. In Real app, change this
    private final static int DAYS_UNTIL_PROMPT = 0;
    private final static int LAUNCH_UNTIL_PROMPT = 3;

    public static void app_launched(Context context){
        SharedPreferences prefs = context.getSharedPreferences("rate_app", 0);
        if (prefs.getBoolean("dontshowagain",false)){
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();

        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        Long date_firstLaunch = prefs.getLong("date_first_launch", 0);
        if(date_firstLaunch == 0){
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_first_launch", date_firstLaunch);
        }

        if (launch_count >= LAUNCH_UNTIL_PROMPT) {
            if (System.currentTimeMillis()>=date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)){
                showRateDialog(context, editor);
            }
        }
        editor.commit();
    }

    private static void showRateDialog(final Context context, final SharedPreferences.Editor editor) {

        Dialog dialog = new Dialog(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        String message = "यदि आप का उपयोग करने का आनंद लेते हैं "
                + APP_TITLE
                + ", एप्लिकेशन को रेट करने के लिए कृपया एक क्षण ले जाएं. "
                + "आपके सहयोग के लिए धन्यवाद!";

        builder.setMessage(message)
                .setTitle("का मूल्यांकन करें " + APP_TITLE)
                .setIcon(context.getApplicationInfo().icon)
                .setCancelable(false)
                .setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putBoolean("dontshowagain",true);
                        editor.commit();

                        //if app hasn't been uploaded to market get an exception
                        //for test, we catch it here and show a text.
                        try {
                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=" + APP_PACKAGE_NAME)));
                        }catch (ActivityNotFoundException e) {
                            Toast.makeText(context, "आपने हाँ बटन दबाया है", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("अभी नहीं", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "आपने ‘अभी नहीं’ बटन दबाया है", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("नहीं", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editor != null){
                            editor.putBoolean("dontshowagain", true);
                            editor.commit();
                        }

                        Toast.makeText(context, "आपने ‘नहीं’ बटन दबाया है", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });
        dialog=builder.create();
        dialog.show();
    }
}
