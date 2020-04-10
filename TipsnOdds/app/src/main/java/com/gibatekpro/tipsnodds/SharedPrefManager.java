package com.gibatekpro.tipsnodds;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "fcmsharedpref";
    private static final String SHARED_PREF_NAME2 = "Sign";
    private static final String KEY_ACCESS_TOKEN = "token";
    private static final String KEY_ACCESS_SIGN = "0";

    private static Context mCtx;
    private static SharedPrefManager mInstance;


    public SharedPrefManager(Context context) {

        mCtx = context;

    }

    public static synchronized SharedPrefManager getmInstance(Context context){
        if(mInstance == null)
            mInstance = new SharedPrefManager(context);
        return mInstance;
    }

    public boolean storeToken (String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences( SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.apply();
        return true;
    }

    public boolean storeSign (String sign){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences( SHARED_PREF_NAME2, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_SIGN, sign);
        editor.apply();
        return true;
    }

    public String getToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences( SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getSign(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences( SHARED_PREF_NAME2, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_SIGN, "0");
    }

}
