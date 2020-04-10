package com.devapp.usbhostmanager;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.devapp.usbhostmanager.models.DeviceModel;
import com.devapp.usbhostmanager.models.InterfaceModel;
import com.devapp.usbhostmanager.service.AdsService;
import com.devapp.usbhostmanager.util.LocaleHelper;
import com.devappliance.devapplibrary.BaseActivity;
import com.devappliance.devapplibrary.util.MessageUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.hardware.usb.UsbConstants.USB_CLASS_APP_SPEC;
import static android.hardware.usb.UsbConstants.USB_CLASS_AUDIO;
import static android.hardware.usb.UsbConstants.USB_CLASS_CDC_DATA;
import static android.hardware.usb.UsbConstants.USB_CLASS_COMM;
import static android.hardware.usb.UsbConstants.USB_CLASS_CONTENT_SEC;
import static android.hardware.usb.UsbConstants.USB_CLASS_CSCID;
import static android.hardware.usb.UsbConstants.USB_CLASS_HID;
import static android.hardware.usb.UsbConstants.USB_CLASS_HUB;
import static android.hardware.usb.UsbConstants.USB_CLASS_MASS_STORAGE;
import static android.hardware.usb.UsbConstants.USB_CLASS_MISC;
import static android.hardware.usb.UsbConstants.USB_CLASS_PER_INTERFACE;
import static android.hardware.usb.UsbConstants.USB_CLASS_PHYSICA;
import static android.hardware.usb.UsbConstants.USB_CLASS_PRINTER;
import static android.hardware.usb.UsbConstants.USB_CLASS_STILL_IMAGE;
import static android.hardware.usb.UsbConstants.USB_CLASS_VENDOR_SPEC;
import static android.hardware.usb.UsbConstants.USB_CLASS_VIDEO;
import static android.hardware.usb.UsbConstants.USB_CLASS_WIRELESS_CONTROLLER;
import static android.hardware.usb.UsbConstants.USB_DIR_IN;
import static android.hardware.usb.UsbConstants.USB_DIR_OUT;
import static com.devapp.usbhostmanager.MainActivity.APP_ID;

public class DeviceDetailsActivity extends BaseActivity implements PurchasesUpdatedListener {
    private static final String TAG = "XXX"; //NON-NLS
    @SuppressWarnings("HardCodedStringLiteral")
    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";
    @SuppressWarnings("HardCodedStringLiteral")
    public static String DATA = "DATA";
    private static int TIMEOUT = 0;
    PendingIntent mPermissionIntent;
    UsbInterface intf;
    UsbDeviceConnection connection = null;
    UsbManager manager;
    Thread usbThread;
    List<InterfaceModel> interfaceModelList = new ArrayList<>();
    ProgressDialog progressDialog;
    private DeviceModel deviceModel;
    private TextView mTextMessage;
    private View fab;
    private BillingClient billingClient;
    private List<String> skuList = Arrays.asList("una1001");
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            if (manager != null)
                                connect(manager, device);
                        } else {
                            Toast.makeText(context, getString(R.string.device_not_found), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        MessageUtil.showMessage(getString(R.string.permission_request_reason), DeviceDetailsActivity.this);
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                Toast.makeText(context, "Usb device has been detached", Toast.LENGTH_LONG).show();
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null && intf != null && connection != null) {
                    close();
                }
            }
        }
    };


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);
        setupBillingClient();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deviceModel = (DeviceModel) getIntent().getSerializableExtra(DATA);

        if (deviceModel == null) {
            Toast.makeText(this.getApplicationContext(), "Device not selected", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        AdsService.with(this, APP_ID)
                .setTestDeviceIds("196D4B95A414F6D22E25EDE25EA87252")
                .setInterstitialAdCallDelay(3)
                .setInterstitialAd("ca-app-pub-5688626796086116/9150575480");
        AdsService.setBanner(R.id.adView, this);
        getSupportActionBar().setTitle(deviceModel.getProductName());
        getSupportActionBar().setSubtitle(deviceModel.getDeviceName());
        getDevice(deviceModel.getDeviceName());
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            warnBeforeRunDiagnostics();
        });

        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);
    }

    private void warnBeforeRunDiagnostics() {
        AdsService.with(this).showInterstitialAd(true, context -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(DeviceDetailsActivity.this);
            alert.setMessage(R.string.usb_disconnect_warning)
                    .setTitle(R.string.run_diagnostics)
                    .setPositiveButton(getString(R.string.continueText), (dialog, which) -> {
                        runDiagnostics();
                    })
                    .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {

                    }).show();
        });
    }

    private void runDiagnostics() {
        progressDialog = new ProgressDialog(DeviceDetailsActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.running_diagnostics_message));
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Handler().postDelayed(() -> {
            manager = (UsbManager) getSystemService(Context.USB_SERVICE);

            if (manager == null) {
                Toast.makeText(DeviceDetailsActivity.this, R.string.usb_service_not_found, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }

            HashMap<String, UsbDevice> deviceList = manager.getDeviceList();

            UsbDevice device = deviceList.get(deviceModel.getDeviceName());
            if (device == null) {
                Toast.makeText(DeviceDetailsActivity.this, R.string.device_not_found_not_connected, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }
            if (manager.hasPermission(device)) {
                connect(manager, device);
            } else {
                try {
                    manager.requestPermission(device, mPermissionIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(DeviceDetailsActivity.this, "Could not obtain permission to proceed", Toast.LENGTH_LONG).show();
                }
            }
        }, 3000);
    }

    private void close() {
        usbThread.interrupt();
        if (connection.releaseInterface(intf)) {
            connection.close();
        }
    }

    private void connect(UsbManager manager, UsbDevice device) {
        interfaceModelList.clear();
        if (manager == null) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Toast.makeText(this, getString(R.string.could_not_complete_task), Toast.LENGTH_LONG).show();
            return;
        }
        connection = manager.openDevice(device);
        if (connection == null) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Toast.makeText(this, "Could not establish connection with device.", Toast.LENGTH_LONG).show();
            return;
        }
        for (int i = 0; i < device.getInterfaceCount(); i++) {
            UsbInterface intf = device.getInterface(i);
            InterfaceModel interfaceModel = new InterfaceModel();
            interfaceModel.setClassName(getDeviceClass(intf.getInterfaceClass()));
            interfaceModel.setNumberOfEndpoints(intf.getEndpointCount());

            for (int j = 0; j < intf.getEndpointCount(); j++) {
                final UsbEndpoint endpoint = intf.getEndpoint(j);
                if (endpoint.getDirection() == USB_DIR_IN) {
                    interfaceModel.setTotalNumInEndpoints(interfaceModel.getTotalNumInEndpoints() + 1);
                } else if (endpoint.getDirection() == USB_DIR_OUT) {
                    interfaceModel.setTotalOutEndpoints(interfaceModel.getTotalOutEndpoints() + 1);
                }
                interfaceModel.setMaxPacketSize(endpoint.getMaxPacketSize());
            }

            boolean forceClaim = true;
            boolean claimed = connection.claimInterface(intf, forceClaim);
            interfaceModel.setSuccessfulConnection(claimed);
            if (claimed) {
                boolean release = connection.releaseInterface(intf);
                interfaceModel.setSuccessfulDisconnection(release);
            }

            interfaceModelList.add(interfaceModel);
        }
        connection.close();
        createReport();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void createReport() {
        boolean seen = false;
        Integer best = null;
        int totalSuccess = 0;
        int totalDisconect = 0;
        int totalInEndpoint = 0;
        int totalOutEndpoint = 0;
        for (InterfaceModel interfaceModel : interfaceModelList) {
            Integer maxPacketSize = interfaceModel.getMaxPacketSize();
            if (!seen || maxPacketSize.compareTo(best) > 0) {
                seen = true;
                best = maxPacketSize;
            }
            if (interfaceModel.isSuccessfulConnection()) {
                totalSuccess++;
            } else {
                totalDisconect++;
            }
            totalInEndpoint += interfaceModel.getTotalNumInEndpoints();
            totalOutEndpoint += interfaceModel.getTotalOutEndpoints();
        }
        int mPS = seen ? best : 0;

        setTextOrNa(R.id.maxPacketSizeTextView, mPS + "");
        setTextOrNa(R.id.successfulConnectionsTextView, totalSuccess + "/" + interfaceModelList.size());
        setTextOrNa(R.id.successfulDisconnectionsTextView, totalDisconect + "/" + interfaceModelList.size());
        setTextOrNa(R.id.totalInEndpointsTextView, totalInEndpoint + "");
        setTextOrNa(R.id.totalOutEndpointsTextView, totalOutEndpoint + "");
        fab.requestFocus();
        Toast.makeText(DeviceDetailsActivity.this, R.string.scroll_down_to_view_diag_report, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void getDevice(String deviceName) {
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);

        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();

        UsbDevice device = deviceList.get(deviceName);
        if (device == null) {
            Toast.makeText(DeviceDetailsActivity.this, R.string.device_not_found_not_connected, Toast.LENGTH_LONG).show();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTextOrNa(R.id.manufacturerNameTextView, device.getManufacturerName());
            setTextOrNa(R.id.deviceNameTextView, device.getProductName());

//            device.getConfigurationCount();
//            device.getConfiguration();
        }


        if (device.getDeviceClass() == USB_CLASS_PER_INTERFACE) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < device.getInterfaceCount(); i++) {
                UsbInterface usbInterface = device.getInterface(i);
                if (i != 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(getDeviceClass(usbInterface.getInterfaceClass()));
//                usbInterface.getName();
//                usbInterface.getEndpointCount();
// usbInterface.getInterfaceProtocol();
//                usbInterface.getAlternateSetting();
            }
            setTextOrNa(R.id.deviceTypeTextView, stringBuilder.toString());
        } else {
            setTextOrNa(R.id.deviceTypeTextView, getDeviceClass(device.getDeviceClass()));
        }
        setTextOrNa(R.id.compositeTextView, device.getInterfaceCount() > 1 ? getString(R.string.yes) : getString(R.string.no));
        setTextOrNa(R.id.deviceIdTextView, String.valueOf(device.getDeviceId()));
        setTextOrNa(R.id.productIdTextView, String.valueOf(device.getProductId()));
        setTextOrNa(R.id.vendorIdTextView, String.valueOf(device.getVendorId()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setTextOrNa(R.id.versionTextView, String.valueOf(device.getVersion()));
        } else {
            setTextOrNa(R.id.versionTextView, null);
        }


//        device.getDeviceProtocol();
//        device.getDeviceSubclass();
//        device.getSerialNumber();

    }

    private String getDeviceClass(int deviceClass) {
        switch (deviceClass) {
            case USB_CLASS_AUDIO:
                return getString(R.string.audio_device);
            case USB_CLASS_COMM:
                return getString(R.string.communications_device);
            case USB_CLASS_HID:
                return getString(R.string.human_interface_device);
            case USB_CLASS_PHYSICA:
                return getString(R.string.physical_device);
            case USB_CLASS_STILL_IMAGE:
                return getString(R.string.still_imaging_device);
            case USB_CLASS_PRINTER:
                return getString(R.string.printer);
            case USB_CLASS_MASS_STORAGE:
                return getString(R.string.mass_storage_device);
            case USB_CLASS_HUB:
                return getString(R.string.usb_hub);
            case USB_CLASS_CDC_DATA:
                return getString(R.string.cdc_device);
            case USB_CLASS_CSCID:
                return getString(R.string.smart_card_device);
            case USB_CLASS_CONTENT_SEC:
                return getString(R.string.content_security_device);
            case USB_CLASS_VIDEO:
                return getString(R.string.video_device);
            case USB_CLASS_WIRELESS_CONTROLLER:
                return getString(R.string.wireless_controller);
            case USB_CLASS_MISC:
                return getString(R.string.other_wireless_devices);
            case USB_CLASS_APP_SPEC:
                return getString(R.string.application_specific);
            case USB_CLASS_VENDOR_SPEC:
                return getString(R.string.vendor_specific);
        }
        return getString(R.string.could_not_be_determined);
    }

    public void setTextOrNa(@IdRes int id, String text) {
        if (TextUtils.isEmpty(text) || text.equalsIgnoreCase("null")) {
            text = getString(R.string.not_applicable);
        }
        ((TextView) findViewById(id)).setText(text);
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mUsbReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.device_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_diagnostics) {
            warnBeforeRunDiagnostics();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupBillingClient() {
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(this)
                .build();
        billingClient.startConnection(
                new BillingClientStateListener() {
                    @Override
                    public void onBillingSetupFinished(BillingResult billingResult) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            restorePurchase();
                        }
                    }

                    @Override
                    public void onBillingServiceDisconnected() {

                    }
                }
        );

    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.

        } else {
            // Handle any other error codes.
        }
    }

    private void restorePurchase() {
        Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
        if (purchasesResult.getPurchasesList() != null) {
            for (Purchase purchase : purchasesResult.getPurchasesList()) {
                handlePurchase(purchase);
            }
        }
    }

    private void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            acknowledgePurchase(purchase.getPurchaseToken());
            // Acknowledge purchase and grant the item to the user
        } else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
            // Here you can confirm to the user that they've started the pending
            // purchase, and to complete it, they should follow instructions that
            // are given to them. You can also choose to remind the user in the
            // future to complete the purchase if you detect that it is still
            // pending.
        }
    }

    private void acknowledgePurchase(String purchaseToken) {
        AcknowledgePurchaseParams params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchaseToken)
                .build();
        billingClient.acknowledgePurchase(params, billingResult -> {
            findViewById(R.id.adView).setVisibility(View.GONE);
            AdsService.destroy();
        });
    }
}
