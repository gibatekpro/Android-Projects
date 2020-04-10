package com.devappliance.devapplibrary.helper;

import android.content.Context;

public abstract class PermissionRequest {
    public abstract String getPermission();

    public String getRationale() {
        return null;
    }

    public abstract int getRequestCode();

    public abstract void doSomething(Context context);
}
