package com.gibatekpro.photomanager;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.annotation.StringRes;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.kobakei.ratethisapp.RateThisApp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import static com.gibatekpro.photomanager.AesCbcWithIntegrity.generateKeyFromPassword;


public abstract class BaseApp extends AppCompatActivity {

    private static final String TAG = "MY APP";
    //snackbar length
    private static int SNACK_SHORT = Snackbar.LENGTH_SHORT;
    private static int SNACK_LONG = Snackbar.LENGTH_LONG;
    private static int SNACK_INDEFINITE = Snackbar.LENGTH_INDEFINITE;
    private InterstitialAd m_InterstitialAd;
    private int s_key = 0;
    private static int delay = 0;
    private static int delay_length = 5;

    public void showSnackMessage(@NonNull String message) {
        //noinspection ConstantConditions
        if (message == null) {
            throw new IllegalArgumentException("message cannot be null.");
        }
        View v = getWindow().getDecorView().getRootView();
        showSnackMessage(v, message, SNACK_SHORT, "", null);
    }

    public void showSnackMessage(@NonNull String message, @NonNull int length) {
        //noinspection ConstantConditions
        if (message == null) {
            throw new IllegalArgumentException("message cannot be null.");
        }
        View v = getWindow().getDecorView().getRootView();
        showSnackMessage(v, message, length, "", null);
    }

    public void showSnackMessage(@NonNull View view, @NonNull String message) {
        //noinspection ConstantConditions
        if (view == null || message == null) {
            throw new IllegalArgumentException();
        }
        showSnackMessage(view, message, SNACK_SHORT, "", null);
    }

    /**
     * Shows a snack message on the screen
     *
     * @param view    The view to attach the snack message to (Usually coordinator layout)
     * @param message The message to be displayed.
     * @param length  The duration to be shown for
     */
    public void showSnackMessage(@NonNull View view, @NonNull String message, @NonNull int length) {
        //noinspection ConstantConditions
        if (view == null || message == null) {
            throw new IllegalArgumentException();
        }
        showSnackMessage(view, message, length, "", null);
    }

    public void showSnackMessage(@NonNull View view, @NonNull String message, @NonNull int length, @Nullable String actionText, @Nullable View.OnClickListener onClick) {
        Exception r = new RuntimeException("showSnackMessage");
        //noinspection ConstantConditions
        if (view == null || message == null) {
            throw new IllegalArgumentException("Invalid parameters.", r);
        }
        Snackbar snack = Snackbar.make(view, message, length);
        if (onClick != null) {
            if (actionText == null) {
                throw new IllegalArgumentException("Action text cannot be null.", r);
            }
            snack.setAction(actionText, onClick);
        }
        snack.show();
    }

    private void throwIllegalArgument(@NonNull String message, @NonNull String cause) {
        Exception r = new NullPointerException(cause);
        throw new IllegalArgumentException(message, r);
    }

    @RequiresPermission("android.permission.INTERNET")
    public void initBannerAd(@IdRes int view) {
        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(view);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        //noinspection ConstantConditions
        adView.loadAd(adRequest);
    }

    @RequiresPermission("android.permission.INTERNET")
    public void initBannerAd(@IdRes int view, String testDevice) {
        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(testDevice)
                .build();
        adView.loadAd(adRequest);
    }

    @RequiresPermission("android.permission.INTERNET")
    public void initNativeExpressAd(@IdRes int viewID) {
        // Load an ad into the AdMob banner view.
        NativeExpressAdView adView = (NativeExpressAdView) findViewById(viewID);

        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);
    }

    @RequiresPermission("android.permission.INTERNET")
    public void initNativeExpressAd(@IdRes int viewID, String testDevice) {
        // Load an ad into the AdMob banner view.
        NativeExpressAdView adView = (NativeExpressAdView) findViewById(viewID);

        AdRequest request = new AdRequest.Builder().addTestDevice(testDevice).build();
        adView.loadAd(request);
    }

    @RequiresPermission("android.permission.INTERNET")
    public void initInterstitialAds(String adUnitID) {
        if (adUnitID == null || adUnitID.isEmpty()) {
            throwIllegalArgument("adUnitId cannot be null or empty", "initInterstitialAds");
        }
        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        m_InterstitialAd = new InterstitialAd(this);
        m_InterstitialAd.setAdUnitId(adUnitID);
        m_InterstitialAd.setAdListener(new AdListener() {
            @Override
            @RequiresPermission("android.permission.INTERNET")
            public void onAdClosed() {
                loadInterstitial();
                performAction();
            }
        });
        loadInterstitial();
    }

    @RequiresPermission("android.permission.INTERNET")
    public void initInterstitialAds(String adUnitID, final String testDeviceID) {
        if (adUnitID == null || adUnitID.isEmpty()) {
            throwIllegalArgument("adUnitId cannot be null or empty", "initInterstitialAds");
        }
        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        if (m_InterstitialAd == null) {
            m_InterstitialAd = new InterstitialAd(this);
            m_InterstitialAd.setAdUnitId(adUnitID);
            m_InterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    performAction();
                    loadInterstitial(testDeviceID);
                }
            });
            loadInterstitial(testDeviceID);
        }
    }

    public abstract void performAction();

    @RequiresPermission("android.permission.INTERNET")
    public void initInterstitialAds(String adUnitID, AdListener adListener) {
        if (adUnitID == null || adUnitID.isEmpty()) {
            throwIllegalArgument("adUnitId cannot be null or empty", "initInterstitialAds");
        }
        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        m_InterstitialAd = new InterstitialAd(this);
        m_InterstitialAd.setAdUnitId(adUnitID);
        m_InterstitialAd.setAdListener(adListener);
        loadInterstitial();
    }

    /*
    *Method call for showing Interstitial ads
     */
    @RequiresPermission("android.permission.INTERNET")
    public void showInterstitialWithDelay() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (m_InterstitialAd != null && m_InterstitialAd.isLoaded()) {
            if (delay % delay_length == 0)
                m_InterstitialAd.show();
            else
                performAction();
        } else {
            loadInterstitial();
            performAction();
        }
        delay++;
    }

    @RequiresPermission("android.permission.INTERNET")
    public void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (m_InterstitialAd != null && m_InterstitialAd.isLoaded()) {
            //performAction();
            m_InterstitialAd.show();
        } else {
            performAction();
            loadInterstitial();
        }
    }

    @RequiresPermission("android.permission.INTERNET")
    public void loadInterstitial() {
        if (m_InterstitialAd == null) {
            throwIllegalArgument("Please call initInterstitialAd before calling this method.", "loadInterstitial");
        }
        AdRequest adRequest = new AdRequest.Builder().build();
        m_InterstitialAd.loadAd(adRequest);
    }

    @RequiresPermission("android.permission.INTERNET")
    public void loadInterstitial(String testDeviceID) {
        if (m_InterstitialAd == null) {
            throwIllegalArgument("Please call initInterstitialAd before calling this method.", "loadInterstitial");
        }
        if (testDeviceID == null || testDeviceID.isEmpty()) {
            throwIllegalArgument("testDeviceID cannot be null or empty", "loadInterstitial");
        }
        AdRequest adRequest = new AdRequest.Builder().build();
                //.addTestDevice(testDeviceID)
        m_InterstitialAd.loadAd(adRequest);
    }

    /**
     * Secures yur app by verifying that it was signed by you.
     *
     * @param sign Your encrypted signature stored in resources
     * @param salt The salt for the encryption
     */
    public void secure(@StringRes int sign, @StringRes int salt) {
        s_key = salt;
        if (isDebuggable(getApplicationContext())) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        itsMyApp(sign);
    }

    /**
     * Secures yur app by verifying that it was signed by you.
     *
     * @param sign     Your encrypted signature stored in resources
     * @param salt     The salt for the encryption
     * @param advanced Use advanced security
     */
    public void secure(@StringRes int sign, @StringRes int salt, boolean advanced) {
        s_key = salt;
        if (isDebuggable(getApplicationContext())) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        if (advanced)
            itsMyApp(sign);
    }


    /**
     * Decrypts an encrypted string value
     *
     * @param text The string to be decrypted
     * @return The decrypted string
     */
    private String decrypt(String text) {
        String decryptedText = "";
        try {
            AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac = new AesCbcWithIntegrity.CipherTextIvMac(text);

            decryptedText = AesCbcWithIntegrity.decryptString(cipherTextIvMac, generateKeyFromPassword(getAppSignature(getApplicationContext()), getString(s_key)));
            Log.i(TAG, "Decrypted: " + decryptedText);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "IllegalArgumentException", e);
        } catch (GeneralSecurityException e) {
            Log.e(TAG, "GeneralSecurityException", e);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UnsupportedEncodingException", e);
        }
        return decryptedText;
    }

    private String getAppSignature(Context context) {
        return getCertificateSHA1Fingerprint();
    }

    private void itsMyApp(@StringRes int sign) {

        if (!getAppSignature(getApplicationContext()).equals(getMyString(sign))) {
            kill();
        }
       /* if (checkAppInstall()) {
            kill();
        }*/
    }

    public void initSupportToolbar(@IdRes int toolbarID) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Helper method to decrypt a string from the string resource file
     *
     * @param string The ID of the string to be decrypted
     * @return The decrypted string value (returns an empty string if decrytion fails)
     */
    private String getMyString(int string) {
        String value = "";
        value = decrypt(getString(string));
        return value;
    }


    private static boolean isDebuggable(Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    private void kill() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    public void viewMoreApps(@NonNull String devName) {
        //noinspection ConstantConditions
        if (devName == null || devName.isEmpty()) {
            throwIllegalArgument("Developer name cannot be null or empty.", "Viewmoreapps");
        }
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + devName)));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=pub:" + devName)));
        }
    }


    public void initHelp() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean show_help = settings.getBoolean("show_help", true);
        if (show_help) {
            showHelp();
        }
    }

    public void rate() {
        String pName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pName)));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pName)));
        }
    }

    public String getCertificateSHA1Fingerprint() {
        Context mContext = getApplicationContext();
        PackageManager pm = mContext.getPackageManager();
        String packageName = mContext.getPackageName();
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        InputStream input = new ByteArrayInputStream(cert);
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        String hexString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(c.getEncoded());
            hexString = byte2HexFormatted(publicKey);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }

    public static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) h = "0" + h;
            if (l > 2) h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1)) str.append(':');
        }
        return str.toString();
    }

    private void visitWebsite(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jumia.com.ng")));
        } catch (NullPointerException e) {
            throwIllegalArgument(e.getMessage(), e.getCause().getMessage());
        }
    }

    public void initAutoRate() {
        // Monitor launch times and interval from installation
        RateThisApp.onCreate(this);
        // If the condition is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(this);
    }

    public void textAlertDialog(String title, String message, String positiveText, @Nullable DialogInterface.OnClickListener positive, Context context) {
        textAlertDialog(title, message, positiveText, positive, "", null, "", null, true, context);
    }

    public void textAlertDialog(String message, String positiveText, @Nullable DialogInterface.OnClickListener positive, Context context) {
        textAlertDialog(message, positiveText, positive, "", null, "", null, true, context);
    }

    public void textAlertDialog(String title, String message, String positiveText, @Nullable DialogInterface.OnClickListener positive, String negativeText, @Nullable DialogInterface.OnClickListener negative, Context context) {
        textAlertDialog(title, message, positiveText, positive, negativeText, negative, "", null, true, context);
    }

    public void textAlertDialog(String message, String positiveText, @Nullable DialogInterface.OnClickListener positive, String negativeText, @Nullable DialogInterface.OnClickListener negative, Context context) {
        textAlertDialog(message, positiveText, positive, negativeText, negative, "", null, true, context);
    }
//
//    public void textAlertDialog(@LayoutRes int dialogLayout, @IdRes int textviewID, String title, String message, String positiveText, @Nullable DialogInterface.OnClickListener positive, String negativeText, @Nullable DialogInterface.OnClickListener negative, String neutralText, @Nullable DialogInterface.OnClickListener neutral, Boolean cancelable, Context context) {
//
//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
//        View mView = getLayoutInflater().inflate(dialogLayout, null);
//        mBuilder.setTitle(title);
//        TextView details = (TextView) mView.findViewById(textviewID);
//        details.setText(message);
//
//        if (positive != null) {
//            mBuilder.setPositiveButton(positiveText, positive);
//        }
//
//        if (negative != null) {
//            mBuilder.setNegativeButton(negativeText, negative);
//        }
//
//        if (neutral != null) {
//            mBuilder.setNeutralButton(neutralText, neutral);
//        }
//        mBuilder.setView(mView);
//        AlertDialog dialog = mBuilder.setCancelable(cancelable).create();
//        dialog.show();
//    }

    public void textAlertDialog(String title, String message, String positiveText, @Nullable DialogInterface.OnClickListener positive, String negativeText, @Nullable DialogInterface.OnClickListener negative, String neutralText, @Nullable DialogInterface.OnClickListener neutral, Boolean cancelable, Context context) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setMessage(message);
        if (title != null || !title.isEmpty())
            mBuilder.setTitle(title);

        if (positive != null) {
            mBuilder.setPositiveButton(positiveText, positive);
        }

        if (negative != null) {
            mBuilder.setNegativeButton(negativeText, negative);
        }

        if (neutral != null) {
            mBuilder.setNeutralButton(neutralText, neutral);
        }
        AlertDialog dialog = mBuilder.setCancelable(cancelable).create();
        dialog.show();
    }

    public void textAlertDialog(String message, String positiveText, @Nullable DialogInterface.OnClickListener positive, String negativeText, @Nullable DialogInterface.OnClickListener negative, String neutralText, @Nullable DialogInterface.OnClickListener neutral, Boolean cancelable, Context context) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setMessage(message);
        if (positive != null) {
            mBuilder.setPositiveButton(positiveText, positive);
        }

        if (negative != null) {
            mBuilder.setNegativeButton(negativeText, negative);
        }

        if (neutral != null) {
            mBuilder.setNeutralButton(neutralText, neutral);
        }
        AlertDialog dialog = mBuilder.setCancelable(cancelable).create();
        dialog.show();
    }

    public boolean checkPermission(String permission, int requestCode, Context context, String rationale) {
        int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
        return false;
    }

    public String toInitialCaps(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public String millisecondsToDate(long milliseconds, String format) {
        return DateFormat.format(format, milliseconds).toString();
    }

    public static void shareApp(String appName, Context context) {
        final String appPackageName = context.getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out " + appName + " at: \n https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent,"Share with"));
    }

    public void visitSite(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void shareImage(String path) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        Uri uri;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            String authorities = this.getPackageName() + ".fileprovider";
//            uri = FileProvider.getUriForFile(this, authorities, new File(path));
//        } else {
            uri = Uri.fromFile(new File(path));
       // }

        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        try {
            startActivity(Intent.createChooser(shareIntent, "Share Photo"));
        } catch (ActivityNotFoundException e) {
            showSnackMessage("Activity not found");
        }
    }

    public void shareImages(List<String> paths) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setType("image/*");
        String authorities = this.getPackageName() + ".fileprovider";

        ArrayList<Uri> uris = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            for (String path : paths) {
                uris.add(FileProvider.getUriForFile(this, authorities, new File(path)));
            }
        } else {
            for (String path : paths) {
                uris.add(Uri.fromFile(new File(path)));
            }
        }
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        try {
            startActivity(Intent.createChooser(shareIntent, "Share Photos"));
        } catch (ActivityNotFoundException e) {
            showSnackMessage("Activity not found");
        }
    }

    private void shareLink(String appName, String link) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, link);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this link! Shared from " + appName + " mobile app.");
        startActivity(Intent.createChooser(intent, "Share"));
    }

    public abstract void showHelp();
}
