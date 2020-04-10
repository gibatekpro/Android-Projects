package com.devapp.usbhostmanager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.crashlytics.android.Crashlytics;
import com.devapp.usbhostmanager.adapter.DeviceListAdapter;
import com.devapp.usbhostmanager.models.DeviceModel;
import com.devapp.usbhostmanager.service.AdsService;
import com.devapp.usbhostmanager.util.LocaleHelper;
import com.devappliance.devapplibrary.BaseActivity;
import com.devappliance.devapplibrary.dto.SupportedLanguage;
import com.devappliance.devapplibrary.util.AppUtil;
import com.devappliance.devapplibrary.util.ItemClickSupport;
import com.devappliance.devapplibrary.util.PreferenceUtil;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.snackbar.Snackbar;
import com.jrejaud.onboarder.OnboardingActivity;
import com.jrejaud.onboarder.OnboardingPage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends BaseActivity implements PurchasesUpdatedListener {
    public static final String APP_ID = "ca-app-pub-5688626796086116~3169999803";
    private static final String ON_BOARDED_PREF_KEY = "ON_BOARDED";
    RecyclerView recyclerView;
    private DeviceListAdapter deviceListAdapter;
    private List<DeviceModel> deviceModels;
    private SwipeRefreshLayout refreshLayout;

    ProgressDialog progressDialog;
    List<SkuDetails> skuDetailsList;
    private BillingClient billingClient;
    private List<String> skuList = Arrays.asList("una1001");
    private BillingFlowParams noAdsParam;
    private Boolean hideMenuItem = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupBillingClient();
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        AdsService.with(this, APP_ID)
                .setTestDeviceIds("196D4B95A414F6D22E25EDE25EA87252")
                .setBanner(R.id.adView)
                .setInterstitialAdCallDelay(3)
                .setInterstitialAd("ca-app-pub-5688626796086116/9150575480");

        if (!PreferenceUtil.get(this, ON_BOARDED_PREF_KEY, false)) {
            onBoard();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.scanning_for_devices));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        refreshLayout = ((SwipeRefreshLayout) findViewById(R.id.refreshLayout));
        refreshLayout.setOnRefreshListener(this::loadData);
        findViewById(R.id.refreshButton).setOnClickListener(v -> loadData());

        deviceModels = new ArrayList<>();

        deviceListAdapter = new DeviceListAdapter(deviceModels, this);
        deviceListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (deviceModels.isEmpty()) {
                    findViewById(R.id.errorLayout).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.errorLayout).setVisibility(View.GONE);
                }
            }
        });
        recyclerView = ((RecyclerView) findViewById(R.id.devicesRecyclerView));
        recyclerView.setAdapter(deviceListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((RecyclerView recyclerView, int position, View v) -> {
            AdsService.with(this).showInterstitialAd(context -> {
                Intent intent = new Intent(MainActivity.this, DeviceDetailsActivity.class);
                intent.putExtra(DeviceDetailsActivity.DATA, deviceModels.get(position));
                startActivity(intent);
            });
        });

        RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .threshold(3F)
                .session(4)
                .onRatingBarFormSumbit(feedback -> {
                    Snackbar.make(recyclerView, R.string.thank_you_for_feedback, Snackbar.LENGTH_LONG).show();
                }).build();
        ratingDialog.show();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onResume() {
        restorePurchase();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
        AdsService.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_removeads);
        item.setVisible(!hideMenuItem);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setMessage(getString(R.string.are_you_sure_quit_app))
                .setPositiveButton(getString(R.string.exit_app), (dialog, which) -> MainActivity.super.onBackPressed())
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {

                }).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_scan:
                loadData();
                return true;
            case R.id.action_removeads:
                if (noAdsParam == null) {
                    setupBillingClient();
                    Toast.makeText(this, "Could not contact billing server", Toast.LENGTH_LONG).show();
                    return true;
                }
                AlertDialog alert = new AlertDialog.Builder(this)
                        .setTitle("Remove ads")
                        .setMessage("Are the ads bugging you? You can disable them for a small fee of " + skuDetailsList.get(0).getPrice() + ". This will really help in supporting continuous development of this app.")
                        .setPositiveButton("Proceed", (dialog, which) -> billingClient.launchBillingFlow(MainActivity.this, noAdsParam))
                        .setNegativeButton("Cancel", (dialog, which) -> {

                        })
                        .show();
                return true;
            case R.id.action_rate:
                RatingDialog ratingDialog = new RatingDialog.Builder(this)
                        .threshold(3F)
                        .onRatingBarFormSumbit(feedback -> {
                            Snackbar.make(recyclerView, getString(R.string.thank_you_for_feedback), Snackbar.LENGTH_LONG).show();
                        }).build();
                ratingDialog.show();
                return true;
            case R.id.action_more:
                AppUtil.viewMoreApps(getString(R.string.dev_name), MainActivity.this);
                return true;
            case R.id.action_language:
                languageSettings("Language");
                return true;
            case R.id.action_help:
                onBoard();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        AdsService.with(this).showInterstitialAd(true, context -> {
            if (progressDialog != null && !progressDialog.isShowing())
                progressDialog.show();
            new Handler().postDelayed(() -> {
                boolean hasHostFeature = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_USB_HOST);
                if (!hasHostFeature) {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    Toast.makeText(MainActivity.this.getApplicationContext(), R.string.device_does_not_support_usb_host_mode, Toast.LENGTH_LONG).show();
                } else {
                    getDevices();
                }
            }, 2000);
        });
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
                            // The BillingClient is setup successfully
                            loadAllSKUs();
                            restorePurchase();
                        }
                    }

                    @Override
                    public void onBillingServiceDisconnected() {

                    }
                }
        );

    }

    private void loadAllSKUs() {
        if (billingClient.isReady()) {
            SkuDetailsParams params = SkuDetailsParams
                    .newBuilder()
                    .setSkusList(skuList)
                    .setType(BillingClient.SkuType.INAPP)
                    .build();
            billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> list) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && !list.isEmpty()) {
                        skuDetailsList = list;
                        noAdsParam = BillingFlowParams
                                .newBuilder()
                                .setSkuDetails(list.get(0))
                                .build();
                        //this will return both the SKUs from Google Play Console

                    }
                }
            });

        } else {

        }

    }

    private void getDevices() {
        refreshLayout.setRefreshing(true);
        deviceModels.clear();
        deviceListAdapter.notifyDataSetChanged();
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);

        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                DeviceModel deviceModel = new DeviceModel();
                deviceModel.setProductName(device.getProductName());
                deviceModel.setDeviceName(device.getDeviceName());
                deviceModel.setManufacturerName(device.getManufacturerName());
                deviceModel.setDeviceClass(device.getDeviceClass());
                deviceModel.setConfigCount(device.getConfigurationCount());
                deviceModel.setDeviceId(device.getDeviceId());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    deviceModel.setVersion(device.getVersion());
                }
                deviceModels.add(deviceModel);
                deviceModel.setVendorId(device.getVendorId());
            }
        }
        deviceListAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void onBoard() {
        OnboardingPage page1 = new OnboardingPage(null, getString(R.string.onboard_instruction_1));
        OnboardingPage page2 = new OnboardingPage(null, getString(R.string.onboard_instruction_2), getString(R.string.done));
//        OnboardingPage page3 = new OnboardingPage("The Robot again!","Hey look, it's the robot again! \nMaybe he wants to be your friend.",R.mipmap.ic_launcher,"We're done here");

        page1.setTitleTextColor(R.color.white);
        page1.setBodyTextColor(R.color.white);

        page2.setBodyTextColor(R.color.white);

//        page3.setTitleTextColor(R.color.white);
//        page3.setBodyTextColor(R.color.white);

//        page3.setTitleTextColor(R.color.white);
//        page3.setBodyTextColor(R.color.white);

        //Finally, add all the Onboarding Pages to a list
        List<OnboardingPage> onboardingPages = new LinkedList<>();
        onboardingPages.add(page1);
        onboardingPages.add(page2);
//        onboardingPages.add(page3);

        Bundle onboardingActivityBundle = OnboardingActivity.newBundleColorBackground(R.color.colorPrimary, onboardingPages);
//        Bundle onboardingActivityBundle = OnboardingActivity..newBundleImageBackground(R.drawable.backgroundPicture, onboardingPages);

        //Start the Onboarding Activity
        Intent intent = new Intent(this, OnboardingActivity.class);
        intent.putExtras(onboardingActivityBundle);
        startActivity(intent);
        runOnboardingSequence();
        PreferenceUtil.save(this, ON_BOARDED_PREF_KEY, true);
    }

    public void runOnboardingSequence() {
        TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.refreshButton), getString(R.string.refresh_device_list), getString(R.string.refresh_device_list_instruction)));
    }

    protected List<SupportedLanguage> getSupportedLanguages() {
        SupportedLanguage english = new SupportedLanguage("English", "en");
        SupportedLanguage albania = new SupportedLanguage("Albania", "sq");
        SupportedLanguage arabic = new SupportedLanguage("Arabic", "ar");
        SupportedLanguage azerbaijani = new SupportedLanguage("Azerbaijani", "az");
        SupportedLanguage bosnian = new SupportedLanguage("Bosnian", "bs");
        SupportedLanguage chinese = new SupportedLanguage("中文", "zh");
        SupportedLanguage czech = new SupportedLanguage("Czech", "cs");
        SupportedLanguage dutch = new SupportedLanguage("Dutch", "nl");
        SupportedLanguage estonian = new SupportedLanguage("Estonian", "et");
        SupportedLanguage french = new SupportedLanguage("Français", "fr");
        SupportedLanguage german = new SupportedLanguage("German", "de");
        SupportedLanguage greek = new SupportedLanguage("Greek", "el");
        SupportedLanguage hausa = new SupportedLanguage("Hausa", "ha");
        SupportedLanguage hebrew = new SupportedLanguage("Hebrew", "iw");
        SupportedLanguage igbo = new SupportedLanguage("Igbo", "ig");
        SupportedLanguage indonesian = new SupportedLanguage("Indonesian", "in");
        SupportedLanguage italian = new SupportedLanguage("Italiano", "it");
        SupportedLanguage japanese = new SupportedLanguage("Japanese", "ja");
        SupportedLanguage korean = new SupportedLanguage("한국어", "ko");
        SupportedLanguage spanish = new SupportedLanguage("Español", "es");
        SupportedLanguage hindi = new SupportedLanguage("हिंदी", "hi");
        return new ArrayList<>(Arrays.asList(english, albania, arabic, azerbaijani, bosnian, czech, dutch,
                estonian, french, german, greek, hebrew, indonesian, japanese, chinese, korean, italian,
                spanish, hindi));
    }

    protected void languageSettings(String title) {
        androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(this);
        alert.setTitle(title);

        SupportedLanguage currentLocale = new SupportedLanguage("", PreferenceUtil.get(this.getApplicationContext(), "locale", getResources().getConfiguration().locale.getLanguage()));

        List<SupportedLanguage> supportedLanguages = getSupportedLanguages();
        if (supportedLanguages == null || supportedLanguages.isEmpty()) {
            throw new IllegalArgumentException("Supported languages not set");
        }
        int selected = supportedLanguages.indexOf(currentLocale);

        List<String> list = new ArrayList<>();
        for (SupportedLanguage it : supportedLanguages) {
            String name = it.getName();
            list.add(name);
        }
        final String[] locales = list.toArray(new String[]{});

        alert.setSingleChoiceItems(locales, selected, (dialogInterface, i) -> {
            SharedPreferences.Editor settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            settings.putString("locale", supportedLanguages.get(i).getCode());
            settings.apply();
            dialogInterface.dismiss();
            recreate();
//            internationalise(true, activity);
        });
        alert.show();
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
                ConsumeParams consumeParams = ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();
//                billingClient.consumeAsync(consumeParams, new ConsumeResponseListener() {
//                    @Override
//                    public void onConsumeResponse(BillingResult billingResult, String s) {
//
//                    }
//                });
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
            if (!hideMenuItem) {
                hideMenuItem = true;
                invalidateOptionsMenu();
            }
            findViewById(R.id.adView).setVisibility(View.GONE);
            AdsService.destroy();
        });
    }
}
