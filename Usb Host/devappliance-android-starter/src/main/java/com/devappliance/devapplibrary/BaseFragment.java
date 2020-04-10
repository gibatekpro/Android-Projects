package com.devappliance.devapplibrary;

import android.content.pm.PackageManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.devappliance.devapplibrary.helper.PermissionRequest;
import com.devappliance.devapplibrary.util.MessageUtil;

public abstract class BaseFragment extends Fragment {
    public void requestPermission(@NonNull final PermissionRequest permissionRequest) {
        if (ContextCompat.checkSelfPermission(getActivity(), permissionRequest.getPermission()) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionRequest.getPermission())) {
                if (!TextUtils.isEmpty(permissionRequest.getRationale())) {
                    MessageUtil.showMessage(permissionRequest.getRationale(),
                            "Grant Permission", (dialog, which) -> requestPermission(permissionRequest), getActivity());
                }

            } else {
                requestPermissions(new String[]{permissionRequest.getPermission()}, permissionRequest.getRequestCode());
            }
        } else {
            permissionRequest.doSomething(this.getContext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            permissionRequestResult(requestCode, permissions[0], grantResults[0] == PackageManager.PERMISSION_GRANTED);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void permissionRequestResult(int requestCode, String permission, boolean permissionGranted) {

    }
}
