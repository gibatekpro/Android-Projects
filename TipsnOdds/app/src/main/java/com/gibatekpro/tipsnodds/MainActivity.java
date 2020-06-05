package com.gibatekpro.tipsnodds;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.gibatekpro.tipsnodds.Admin.AdminActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Layouts
    LinearLayout mSigninLayout, mUpdateNameLayout, mUpdateImageLayout, mSignupConvoLayout,
            mCreateDialogLayout, mSignupErrorLayout, mMainLayout, mPassResetLayout,
            mPassChangeLayout, mCreateSuccessLayout, mResetDialogLayout, mResetErrorLayout, mResetSuccessLayout;

    ConstraintLayout mSignUpTitle_layout, mSignInTitle_layout;
    TextInputLayout mPasswordFieldEye;

    //Edit Text Fields
    EditText mEmailField, mPasswordField, mPasswordField2, mDisplayNamer, mPassResetEmail,
            mPassChangePass1, mPassChangePass2;

    //Buttons
    Button mSignupButton, mSigninButton, mResetPassButton, mChangePassButton, mSignupButtonOpt,
            mChooseButton, mUpdateName, mUpdateImage, mTrySignupagain, mResetPasswordOpt, mDismiss,
            mCancelResetPass, mCancelChangePass, mTryResetPassagain, mDismissResetPass, mCamera, mGallery;

    ImageButton mAdminButton;

    //TextViews
    TextView mDisplayName, mLoginTitle, mSignupTitle, mSignupConvoTitle, mUpdateTitle;

    //Image Views
    CircleImageView imageView, mUpdateImageView, mUpdateImageViewTab;

    //Navigation Drawer View
    NavigationView navigationView;
    Menu nav_Menu;

    //Alert Dialog Builder
    AlertDialog.Builder mBuilder;
    AlertDialog alertdialog;

    //Intents
    Intent CamIntent, GalIntent, CropIntent;

    //Fragments declaration array
    private Fragment[] fragments = new Fragment[]{new PendingFragment(),
            new PlayedFragment()};

    //Ads Initialization
    AdView mAdView;
    private InterstitialAd mInterstitialAd;

    //LOG TAG Declaration
    private static final String TAG = "FireStore";

    //Package Name Initialization
    private final static String APP_PACKAGE_NAME = "com.gibatekpro.tipsnodds";

    private final int PICK_IMAGE = 1;

    final int RequestPermissionCode = 1;

    private BroadcastReceiver broadcastReceiver;

    private Uri filePath, uri;

    long dataSize = 0;

    File f, file;

    double progress = 0;


    //Spots dialog declaration
    SpotsDialog spotsDialog;

    //Cloud Firestore initialization
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // [START declare_firebase_storage]
    FirebaseStorage storage;
    StorageReference storageReference;
    // [END declare_firebase_storage]

    // [START declare_auth]
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    // [END declare_auth]

    private DrawerLayout mDrawerLayout;

    //[START OnCreate Method]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //<editor-fold desc="Broadcast and fetch token from Shared Pref Manager">
        //[START Broadcast and fetch token from Shared Pref Manager]
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d(TAG, "token retrieve  " + SharedPrefManager.getmInstance(MainActivity.this).getToken());

            }
        };

        if (SharedPrefManager.getmInstance(this).getToken() != null)
            Log.d(TAG, "token retrieve  " + SharedPrefManager.getmInstance(this).getToken());

        registerReceiver(broadcastReceiver, new IntentFilter(MyFirebaseMessagingService.TOKEN_BROADCAST));
        //[END Broadcast and fetch token from Shared Pref Manager]
        //</editor-fold>

        //[START Firebase Insatance]
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //[END Firebase Insatance]

        //[START Spots Dialog Instance]
        spotsDialog = new SpotsDialog(MainActivity.this, "Loading...");
        //[END Spots Dialog Instance]

        //<editor-fold desc="Mobile Ads Initialization">
        //[START Mobile Ads Initialization]
        MobileAds.initialize(this,
                "ca-app-pub-1488497497647050~3104706924");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1488497497647050/8963005439");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //[END Mobile Ads Initialization]
        //</editor-fold>

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //[START Navigation Drawer Button On Tool Bar]
       /* ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);*/
        //[END Navigation Drawer Button On Tool Bar]

        //<editor-fold desc="Navigation Drawer Declaration">
        //[START Navigation Drawer Declaration]
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        nav_Menu = navigationView.getMenu();

        View hView = navigationView.inflateHeaderView(R.layout.nav_header_nav_d);
        imageView = hView.findViewById(R.id.imageView);

        mDisplayName = hView.findViewById(R.id.displayname_textView);

        //[END Navigation Drawer Declaration]
        //</editor-fold>

        SectionsPagerAdapter mSectionsPagerAdapter
                = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        //[START Refresh Button]
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //[START Ads call]
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                //[END Ads Call]

                //[START Refresh Data]
                spotsDialog.show();
                load_pending();
                load_played();
                //[END Refresh Data]

                //[START Refresh Ads]
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                //[END Refresh Ads]
            }
        });
        //[END Refresh Button]

        mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_log_in, null);


        //<editor-fold desc="Layouts views declaration">
        mSigninLayout = mView.findViewById(R.id.signinLayout);
        mUpdateNameLayout = mView.findViewById(R.id.updateNameLayout);
        mUpdateImageLayout = mView.findViewById(R.id.updateImageLayout);
        mSignupConvoLayout = mView.findViewById(R.id.signup_option_layout);
        mCreateDialogLayout = mView.findViewById(R.id.signupdialog_layout);
        mResetDialogLayout = mView.findViewById(R.id.resetpassdialog_layout);
        mSignupErrorLayout = mView.findViewById(R.id.signuperror_layout);
        mResetErrorLayout = mView.findViewById(R.id.resetpasserror_layout);
        mCreateSuccessLayout = mView.findViewById(R.id.create_success_layout);
        mResetSuccessLayout = mView.findViewById(R.id.reset_success_layout);
        mSignInTitle_layout = mView.findViewById(R.id.signin_title_layout);
        mSignUpTitle_layout = mView.findViewById(R.id.signup_title_layout);
        mPasswordFieldEye = mView.findViewById(R.id.password_edit_eye);
        mMainLayout = mView.findViewById(R.id.main_lay);
        mPassResetLayout = mView.findViewById(R.id.reset_pass_lay);
        mPassChangeLayout = mView.findViewById(R.id.change_pass_lay);
        //</editor-fold>


        //Image Views
        mUpdateImageView = mView.findViewById(R.id.update_imageView);
        mUpdateImageViewTab = toolbar.findViewById(R.id.update_imageViewTab);

        //<editor-fold desc="TextViews Declaration">
        //TextViews
        mLoginTitle = mView.findViewById(R.id.login_title);
        mSignupTitle = mView.findViewById(R.id.signup_title);
        mSignupConvoTitle = mView.findViewById(R.id.signup_convo_title);
        mUpdateTitle = mView.findViewById(R.id.update_title);
        //</editor-fold>

        //<editor-fold desc="EditTexts Declaration">
        //EditTexts
        mEmailField = mView.findViewById(R.id.email_edit);
        mPassResetEmail = mView.findViewById(R.id.passreset_email_edit);
        mPasswordField = mView.findViewById(R.id.password_edit);
        mPasswordField2 = mView.findViewById(R.id.password_edit2);
        mDisplayNamer = mView.findViewById(R.id.displayname_edit);
        mPassChangePass1 = mView.findViewById(R.id.passchange_pass1_edit);
        mPassChangePass2 = mView.findViewById(R.id.passchange_pass2_edit);
        //</editor-fold>

        //<editor-fold desc="Buttons Declaration">
        //Buttons
        mSigninButton = mView.findViewById(R.id.signin_button);
        mSignupButton = mView.findViewById(R.id.signup_button);
        mSignupButtonOpt = mView.findViewById(R.id.signupOption_button);
        mResetPasswordOpt = mView.findViewById(R.id.reset_password_button_opt);
        mGallery = mView.findViewById(R.id.btn_gallery);
        mUpdateName = mView.findViewById(R.id.updateName_button);
        //mUpdateImage = mView.findViewById(R.id.updateImage_button);
        mTrySignupagain = mView.findViewById(R.id.try_again_button);
        mTryResetPassagain = mView.findViewById(R.id.try_reset_again_button);
        mDismiss = mView.findViewById(R.id.dismiss_button);
        mDismissResetPass = mView.findViewById(R.id.dismiss_reset_button);
        mResetPasswordOpt = mView.findViewById(R.id.reset_password_button_opt);
        mResetPassButton = mView.findViewById(R.id.reset_pass_button);
        mChangePassButton = mView.findViewById(R.id.change_pass_button);
        mCancelResetPass = mView.findViewById(R.id.cancel_reset_pass);
        mCancelChangePass = mView.findViewById(R.id.cancel_change_pass);
        mAdminButton = toolbar.findViewById(R.id.admin_btn);
        //</editor-fold>

        mBuilder.setView(mView);
        alertdialog = mBuilder.create();

        ShowSignInOnStart();

        //<editor-fold desc="Button Functions">
        mAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                MainActivity.this.startActivity(intent);

            }
        });

        //<editor-fold desc="Button Functions">
        mGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GalleryOpen();

            }
        });

        mUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertdialog.dismiss();

                update_display_name();

            }
        });

        /*mUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertdialog.dismiss();

                Upload_Image();

            }
        });*/

        mSignupButtonOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSigninLayout.setVisibility(View.VISIBLE);
                mMainLayout.setVisibility(View.VISIBLE);
                mPassResetLayout.setVisibility(View.GONE);
                mPasswordFieldEye.setVisibility(View.VISIBLE);
                mSignupConvoLayout.setVisibility(View.GONE);
                mSignupButton.setVisibility(View.VISIBLE);
                mUpdateImageLayout.setVisibility(View.GONE);
                mUpdateNameLayout.setVisibility(View.GONE);
                mCreateDialogLayout.setVisibility(View.GONE);
                mResetDialogLayout.setVisibility(View.GONE);
                mSignInTitle_layout.setVisibility(View.GONE);
                mSignUpTitle_layout.setVisibility(View.VISIBLE);
                mSigninButton.setVisibility(View.GONE);
                mResetPasswordOpt.setVisibility(View.GONE);

            }
        });

        mSigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());

            }
        });

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());

            }
        });

        mTrySignupagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSigninLayout.setVisibility(View.VISIBLE);
                mMainLayout.setVisibility(View.VISIBLE);
                mPassResetLayout.setVisibility(View.GONE);
                mPassChangeLayout.setVisibility(View.GONE);
                mSignupConvoLayout.setVisibility(View.GONE);
                mSignupButton.setVisibility(View.VISIBLE);
                mUpdateImageLayout.setVisibility(View.GONE);
                mUpdateNameLayout.setVisibility(View.GONE);
                mCreateDialogLayout.setVisibility(View.GONE);
                mResetDialogLayout.setVisibility(View.GONE);
                mSignInTitle_layout.setVisibility(View.GONE);
                mSignUpTitle_layout.setVisibility(View.VISIBLE);
                mSigninButton.setVisibility(View.GONE);
                mResetPasswordOpt.setVisibility(View.GONE);

            }
        });

        mTryResetPassagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMainLayout.setVisibility(View.GONE);
                mPassResetLayout.setVisibility(View.VISIBLE);
                mPassChangeLayout.setVisibility(View.GONE);

            }
        });

        mDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertdialog.dismiss();

            }
        });

        mDismissResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertdialog.dismiss();

            }
        });

        mCancelResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertdialog.dismiss();

            }
        });

        mCancelChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertdialog.dismiss();

            }
        });

        mResetPasswordOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertdialog.show();


                mMainLayout.setVisibility(View.GONE);
                mPassResetLayout.setVisibility(View.VISIBLE);
                mPassChangeLayout.setVisibility(View.GONE);

            }
        });

        mResetPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendPasswordResetEmail();

            }
        });

        mChangePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatePassword();

            }
        });
        //</editor-fold>

    }

    private void ShowSignInOnStart() {

        String Sign = SharedPrefManager.getmInstance(MainActivity.this).getSign();

        SendSign(Sign);

        int SignInt = Integer.valueOf(Sign);

        SignInt ++;

        Sign = String.valueOf(SignInt);

        SendSign(Sign);

        if (Sign.equals("1")) {


            //<editor-fold desc="Alert Dialog On App Open">
            mSigninLayout.setVisibility(View.VISIBLE);
            mMainLayout.setVisibility(View.VISIBLE);
            mPassResetLayout.setVisibility(View.GONE);
            mPassChangeLayout.setVisibility(View.GONE);
            mPasswordFieldEye.setVisibility(View.GONE);
            mSignupConvoLayout.setVisibility(View.VISIBLE);
            mSignupButton.setVisibility(View.GONE);
            mUpdateNameLayout.setVisibility(View.GONE);
            mUpdateImageLayout.setVisibility(View.GONE);
            mCreateDialogLayout.setVisibility(View.GONE);
            mResetDialogLayout.setVisibility(View.GONE);
            mSignInTitle_layout.setVisibility(View.VISIBLE);
            mSignUpTitle_layout.setVisibility(View.GONE);
            mSigninButton.setVisibility(View.VISIBLE);
            mResetPasswordOpt.setVisibility(View.VISIBLE);
            //</editor-fold>

            alertdialog.show();


        }else {
            alertdialog.dismiss();
        }


    }

    private void SendSign(String sign) {

        SharedPrefManager.getmInstance(getApplicationContext()).storeSign(sign);

    }
    //[END OnCreate Method]

    //[START Load Data For Played Fragment]
    public void load_played() {

        //[START Show Spots Dialog]
        //[END Show Spots Dialog]


        if (isNetworkAvaliable(MainActivity.this)) {
            //[START Load Data From Fire_store And Send Response To loadData(task)]
            db.collection("Played")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            spotsDialog.dismiss();

                            if (task.isSuccessful()) {

                                List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
                                for (Fragment fragment : fragmentList) {

                                    try {
                                        if (fragment.isVisible()) {
                                            //spotsDialog.dismiss();
                                            ((PlayedFragment) fragment).loadData(task);

                                            break;
                                        }
                                    } catch (Exception e) {
                                    }
                                }

                                //[START Snack Bar Data Loaded Successfully]
                                /*Snackbar.make(findViewById(R.id.main_content), "Data Loaded Successfully", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();*/
                                //[END Snack Bar Data Loaded Successfully]
                            } else {

                                //[START Snack Bar Failed To Load Data]
                                /*Snackbar.make(findViewById(R.id.main_content), "Failed To Load Data", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();*/
                                //[END Snack Bar Failed To Load Data]

                            }
                        }

                    });
            //[END Load Data From Fire_store And Send Response To loadData(task)]
        } else {

            spotsDialog.dismiss();

        }

    }
    //[END Load Data For Played Fragment]

    //[START Load Data For Today Fragment]
    public void load_pending() {

        //[START Show Spots Dialog]
        final SpotsDialog spotsDialog = new SpotsDialog(MainActivity.this, "Loading...");
        spotsDialog.show();
        //[END Show Spots Dialog]

        if (isNetworkAvaliable(this)) {
            //[START Load Data From Fire_store And Send Response To loadData(task)]
            db.collection("Today")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            spotsDialog.dismiss();

                            if (task.isSuccessful()) {

                                List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
                                for (Fragment fragment : fragmentList) {
                                    try {
                                        if (fragment.isVisible()) {

                                            //spotsDialog.dismiss();
                                            ((PendingFragment) fragment).loadData(task);

                                            break;
                                        }
                                    } catch (Exception e) {
                                    }
                                }

                                //[START Snack Bar Data Loaded Successfully]
                                Snackbar.make(findViewById(R.id.main_content), "Data Loaded Successfully", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                                //[END Snack Bar Data Loaded Successfully]
                            } else {

                                //[START Snack Bar Failed To Load Data]
                                Snackbar.make(findViewById(R.id.main_content), "Failed To Load Data", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                //[END Snack Bar Failed To Load Data]

                            }
                        }
                    });
            //[END Load Data From Fire_store And Send Response To loadData(task)]
        } else {

            spotsDialog.dismiss();

            //[START Snack Bar Data Loaded Successfully]
            Snackbar.make(findViewById(R.id.main_content), "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            //[END Snack Bar Data Loaded Successfully]

        }

    }
    //[END Load Data For Today Fragment]

    //[START Back Press On Navigation Drawer]
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //[END Back Press On Navigation Drawer]

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_displayname) {

            alertdialog.show();

            mSigninLayout.setVisibility(View.GONE);
            mMainLayout.setVisibility(View.VISIBLE);
            mPassResetLayout.setVisibility(View.GONE);
            mPassChangeLayout.setVisibility(View.GONE);
            mSignupConvoLayout.setVisibility(View.GONE);
            mSignupButton.setVisibility(View.GONE);
            mUpdateNameLayout.setVisibility(View.VISIBLE);
            mUpdateImageLayout.setVisibility(View.GONE);
            mCreateDialogLayout.setVisibility(View.GONE);
            mResetDialogLayout.setVisibility(View.GONE);

        } else if (id == R.id.nav_displaypic) {

            alertdialog.show();

            mSigninLayout.setVisibility(View.GONE);
            mMainLayout.setVisibility(View.VISIBLE);
            mPassResetLayout.setVisibility(View.GONE);
            mPassChangeLayout.setVisibility(View.GONE);
            mSignupConvoLayout.setVisibility(View.GONE);
            mSignupButton.setVisibility(View.GONE);
            mUpdateImageLayout.setVisibility(View.VISIBLE);
            mUpdateNameLayout.setVisibility(View.GONE);
            mCreateDialogLayout.setVisibility(View.GONE);
            mResetDialogLayout.setVisibility(View.GONE);


        } else if (id == R.id.nav_create_account) {

            alertdialog.show();

            mSigninLayout.setVisibility(View.VISIBLE);
            mMainLayout.setVisibility(View.VISIBLE);
            mPassResetLayout.setVisibility(View.GONE);
            mPassChangeLayout.setVisibility(View.GONE);
            mPasswordFieldEye.setVisibility(View.VISIBLE);
            mSignupConvoLayout.setVisibility(View.GONE);
            mSignupButton.setVisibility(View.VISIBLE);
            mUpdateImageLayout.setVisibility(View.GONE);
            mUpdateNameLayout.setVisibility(View.GONE);
            mCreateDialogLayout.setVisibility(View.GONE);
            mResetDialogLayout.setVisibility(View.GONE);
            mSignInTitle_layout.setVisibility(View.GONE);
            mSignUpTitle_layout.setVisibility(View.VISIBLE);
            mSigninButton.setVisibility(View.GONE);
            mResetPasswordOpt.setVisibility(View.GONE);


        } else if (id == R.id.nav_change_pass) {

            alertdialog.show();

            /*mMainLayout.setVisibility(View.GONE);
            mPassResetLayout.setVisibility(View.GONE);
            mPassChangeLayout.setVisibility(View.VISIBLE);*/

            mMainLayout.setVisibility(View.GONE);
            mPassResetLayout.setVisibility(View.VISIBLE);
            mPassChangeLayout.setVisibility(View.GONE);


        } else if (id == R.id.nav_signin) {

            alertdialog.show();

            mSigninLayout.setVisibility(View.VISIBLE);
            mMainLayout.setVisibility(View.VISIBLE);
            mPassResetLayout.setVisibility(View.GONE);
            mPassChangeLayout.setVisibility(View.GONE);
            mPasswordFieldEye.setVisibility(View.GONE);
            mSignupConvoLayout.setVisibility(View.VISIBLE);
            mSignupButton.setVisibility(View.GONE);
            mUpdateNameLayout.setVisibility(View.GONE);
            mUpdateImageLayout.setVisibility(View.GONE);
            mCreateDialogLayout.setVisibility(View.GONE);
            mResetDialogLayout.setVisibility(View.GONE);
            mSignInTitle_layout.setVisibility(View.VISIBLE);
            mSignUpTitle_layout.setVisibility(View.GONE);
            mSigninButton.setVisibility(View.VISIBLE);
            mResetPasswordOpt.setVisibility(View.VISIBLE);


        } else if (id == R.id.nav_share) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Great Betting Tips" + "\n" + "https://play.google.com/store/apps/details?id=com.gibatekpro.tipsnodds");
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));


        } else if (id == R.id.nav_rate_app) {

            rate();

        } else if (id == R.id.nav_logout) {

            signOut();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //[START Select Navigation Drawer Options Menu]

    //[START Fragments Array]
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return fragments[position];
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
    //[END Fragments Array]

    //[START App Rate Function]
    public void rate() {

        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .icon(getResources().getDrawable(R.mipmap.ic_launcher))
                .threshold(4)
                .title(getString(R.string.rate_app))
                .titleTextColor(R.color.black)
                .positiveButtonText(getString(R.string.not_now))
                .negativeButtonText(getString(R.string.no))
                .positiveButtonTextColor(R.color.black)
                .negativeButtonTextColor(R.color.black)
                .formTitle(getString(R.string.Submit_Feedback))
                .formHint(getString(R.string.Tell_us_where_to_improve))
                .formSubmitText(getString(R.string.Submit))
                .formCancelText(getString(R.string.Cancel))
                .ratingBarColor(R.color.black)
                .playstoreUrl("https://play.google.com/store/apps/details?id=com.gibatekpro.tipsnodds")
                .onThresholdCleared(new RatingDialog.Builder.RatingThresholdClearedListener() {
                    @Override
                    public void onThresholdCleared(RatingDialog ratingDialog, float rating, boolean thresholdCleared) {

                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=" + APP_PACKAGE_NAME)));
                        } catch (ActivityNotFoundException e) {
                            // Toast.makeText(getActivity(), R.string.You_have_pressed_Rate_Button, Toast.LENGTH_SHORT).show();
                        }
                        ratingDialog.dismiss();
                    }
                })
                .onRatingChanged(new RatingDialog.Builder.RatingDialogListener() {
                    @Override
                    public void onRatingSelected(float rating, boolean thresholdCleared) {

                    }
                })
                .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                    @Override
                    public void onFormSubmitted(String feedback) {
                        Log.i("TAG", "Feedback:" + feedback);
                    }
                }).build();
        ratingDialog.show();

    }
    //[END App Rate Function]

    // [START Check For Current User]
    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }
    // [END Check For Current User]

    // [START Sign IN]
    private void signIn(String email, String password) {
        // Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        final SpotsDialog spotsDialog = new SpotsDialog(MainActivity.this, "Signing In...");
        spotsDialog.show();


        if (isNetworkAvaliable(MainActivity.this)) {
            // [START sign_in_with_email]
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            spotsDialog.dismiss();

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);

                                alertdialog.dismiss();

                                Snackbar.make(findViewById(R.id.main_content), "Sign In Successful", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                            } else {
                                // If sign in fails, display a message to the user.

                                alertdialog.dismiss();

                                Snackbar.make(findViewById(R.id.main_content), "Sign In Failed", Snackbar.LENGTH_LONG)
                                        .setAction("Try Again", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                alertdialog.show();

                                                mSigninLayout.setVisibility(View.VISIBLE);
                                                mMainLayout.setVisibility(View.VISIBLE);
                                                mPassResetLayout.setVisibility(View.GONE);
                                                mPassChangeLayout.setVisibility(View.GONE);
                                                mPasswordFieldEye.setVisibility(View.GONE);
                                                mSignupConvoLayout.setVisibility(View.VISIBLE);
                                                mSignupButton.setVisibility(View.GONE);
                                                mUpdateNameLayout.setVisibility(View.GONE);
                                                mUpdateImageLayout.setVisibility(View.GONE);
                                                mCreateDialogLayout.setVisibility(View.GONE);
                                                mResetDialogLayout.setVisibility(View.GONE);
                                                mSignInTitle_layout.setVisibility(View.VISIBLE);
                                                mSignUpTitle_layout.setVisibility(View.GONE);
                                                mSigninButton.setVisibility(View.VISIBLE);
                                                mResetPasswordOpt.setVisibility(View.VISIBLE);

                                            }
                                        }).setActionTextColor(getResources().getColor(R.color.red)).show();

                            }

                        }
                    });
            // [END sign_in_with_email]
        } else {

            spotsDialog.dismiss();

            alertdialog.dismiss();

            //[START Snack Bar Data Loaded Successfully]
            Snackbar.make(findViewById(R.id.main_content), "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            //[END Snack Bar Data Loaded Successfully]


        }

    }
    // [END Sign IN]

    // [START Sign IN]
    private void signIn2(String email, String password) {


        if (isNetworkAvaliable(MainActivity.this)) {
            // [START sign_in_with_email]
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {

                                    nav_Menu.findItem(R.id.nav_logout).setVisible(true);
                                    nav_Menu.findItem(R.id.nav_displayname).setVisible(true);
                                    nav_Menu.findItem(R.id.nav_displaypic).setVisible(true);
                                    nav_Menu.findItem(R.id.nav_signin).setVisible(false);
                                    nav_Menu.findItem(R.id.nav_create_account).setVisible(false);

                                    //[START Image Fetch And Show]
                                    Show_Image();
                                    //[END Image Fetch And Show]

                                    //[START Display Name Fetch And Show]
                                    String name = user.getDisplayName();
                                    mDisplayName.setText(name);
                                    //[END Display Name Fetch And Show]

                                } else {


                                    mDisplayName.setText(R.string.not_signed_in);

                                    imageView.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_account));
                                    mUpdateImageViewTab.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_account));


                                    nav_Menu.findItem(R.id.nav_logout).setVisible(false);
                                    nav_Menu.findItem(R.id.nav_displayname).setVisible(false);
                                    nav_Menu.findItem(R.id.nav_displaypic).setVisible(false);
                                    nav_Menu.findItem(R.id.nav_signin).setVisible(true);
                                    nav_Menu.findItem(R.id.nav_create_account).setVisible(true);

                                }

                                sendEmailVerification();


                            } else {
                                // If sign in fails, display a message to the user.

                            }

                        }
                    });
            // [END sign_in_with_email]
        } else {

            spotsDialog.dismiss();

            alertdialog.dismiss();

            //[START Snack Bar Data Loaded Successfully]
            Snackbar.make(findViewById(R.id.main_content), "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            //[END Snack Bar Data Loaded Successfully]


        }


    }
    // [END Sign IN]

    // [START Sign OUT]
    private void signOut() {

        mAuth.signOut();

        Snackbar.make(findViewById(R.id.main_content), "Sign out Successful", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();


        updateUI(null);
    }
    // [END Sign IN]

    // [START Change View of Alert Dialog]
    private void updateUI(FirebaseUser user) {

        spotsDialog.dismiss();

        FirebaseUser currentUser = mAuth.getCurrentUser();



        if (user != null) {

            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
            nav_Menu.findItem(R.id.nav_displayname).setVisible(true);
            nav_Menu.findItem(R.id.nav_displaypic).setVisible(true);
            nav_Menu.findItem(R.id.nav_signin).setVisible(false);
            nav_Menu.findItem(R.id.nav_create_account).setVisible(false);
            nav_Menu.findItem(R.id.nav_change_pass).setVisible(true);

            //[START Image Fetch And Show]
            Show_Image();
            //[END Image Fetch And Show]

            //[START Display Name Fetch And Show]
            String name = user.getDisplayName();
            mDisplayName.setText(name);
            //[END Display Name Fetch And Show]

            String uid = user.getUid();

            //Log.d(TAG, "My ID  " + uid);

            if (uid.equals( "PNXIzkvwkxXuZQBGjqAR7yhXRdp1" )) {

                mAdminButton.setVisibility(View.VISIBLE);

            } else {

                mAdminButton.setVisibility(View.GONE);

            }

            alertdialog.dismiss();

        } else {

            mAdminButton.setVisibility(View.GONE);

            mDisplayName.setText(R.string.not_signed_in);

            imageView.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_account));
            mUpdateImageViewTab.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_account));


            nav_Menu.findItem(R.id.nav_logout).setVisible(false);
            nav_Menu.findItem(R.id.nav_displayname).setVisible(false);
            nav_Menu.findItem(R.id.nav_displaypic).setVisible(false);
            nav_Menu.findItem(R.id.nav_signin).setVisible(true);
            nav_Menu.findItem(R.id.nav_create_account).setVisible(true);
            nav_Menu.findItem(R.id.nav_change_pass).setVisible(false);

        }
    }
    // [END Change View of Alert Dialog]

    // [START Load User Image And Display]
    private void Show_Image() {

        final FirebaseUser user = mAuth.getCurrentUser();

        storageReference.child("images/" + user.getEmail() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'

                Picasso.with(MainActivity.this).load(uri).placeholder(R.drawable.ic_account)
                        .error(R.drawable.ic_account)
                        .into(imageView, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                            }
                        });

                Picasso.with(MainActivity.this).load(uri).placeholder(R.drawable.ic_account)
                        .error(R.drawable.ic_account)
                        .into(mUpdateImageViewTab, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                            }
                        });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    // [END Load User Image And Display]

    // [START Create Account]
    private void createAccount(String email, String password) {
        // Log.d(TAG, "createAccount:" + email);
        if (!validatePassword()) {
            return;
        }

        final SpotsDialog spotsDialog = new SpotsDialog(MainActivity.this, "Creating Account...");
        spotsDialog.show();


        if (isNetworkAvaliable(MainActivity.this)) {
            // [START create_user_with_email]
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            spotsDialog.dismiss();

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                signIn2(mEmailField.getText().toString(), mPasswordField.getText().toString());
                                //mSigninLayout.setVisibility(View.GONE);
                                //mUpdateLayout.setVisibility(View.VISIBLE);

                            } else {
                                // If sign in fails, display a message to the user.
                                mSigninLayout.setVisibility(View.GONE);
                                mMainLayout.setVisibility(View.VISIBLE);
                                mPassResetLayout.setVisibility(View.GONE);
                                mPassChangeLayout.setVisibility(View.GONE);
                                mUpdateImageLayout.setVisibility(View.GONE);
                                mUpdateNameLayout.setVisibility(View.GONE);
                                mSignupErrorLayout.setVisibility(View.VISIBLE);
                                mDismiss.setVisibility(View.VISIBLE);
                                mTrySignupagain.setVisibility(View.VISIBLE);
                                mCreateSuccessLayout.setVisibility(View.GONE);
                                mCreateDialogLayout.setVisibility(View.VISIBLE);
                                mResetDialogLayout.setVisibility(View.GONE);

                                //updateUI(null);
                            }

                            //spotsDialog.dismiss();

                        }
                    });
            // [END create_user_with_email]
        } else {

            spotsDialog.dismiss();

            alertdialog.dismiss();

            //[START Snack Bar Data Loaded Successfully]
            Snackbar.make(findViewById(R.id.main_content), "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            //[END Snack Bar Data Loaded Successfully]

        }


    }
    // [END Create Account]

    // [START Send Email Verification]
    private void sendEmailVerification() {


        if (isNetworkAvaliable(MainActivity.this)) {
            // [START send_email_verification]
            final FirebaseUser user = mAuth.getCurrentUser();
            user.sendEmailVerification()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // [START_EXCLUDE]
                            if (task.isSuccessful()) {

                                mSigninLayout.setVisibility(View.GONE);
                                mMainLayout.setVisibility(View.VISIBLE);
                                mPassResetLayout.setVisibility(View.GONE);
                                mPassChangeLayout.setVisibility(View.GONE);
                                mUpdateImageLayout.setVisibility(View.GONE);
                                mUpdateNameLayout.setVisibility(View.GONE);
                                mSignupErrorLayout.setVisibility(View.GONE);
                                mCreateSuccessLayout.setVisibility(View.VISIBLE);
                                mTrySignupagain.setVisibility(View.GONE);
                                mDismiss.setVisibility(View.VISIBLE);
                                mCreateDialogLayout.setVisibility(View.VISIBLE);
                                mResetDialogLayout.setVisibility(View.GONE);

                            } else {

                                Log.e(TAG, "sendEmailVerification", task.getException());

                            }
                            // [END_EXCLUDE]
                        }
                    });
            // [END send_email_verification]
        } else {

            spotsDialog.dismiss();

            alertdialog.dismiss();

            //[START Snack Bar Data Loaded Successfully]
            Snackbar.make(findViewById(R.id.main_content), "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            //[END Snack Bar Data Loaded Successfully]

        }

    }
    // [END Send Email Verification]

    // [START Display Name Update]
    private void update_display_name() {

        final SpotsDialog spotsDialog = new SpotsDialog(MainActivity.this, "Updating...");
        spotsDialog.show();


        if (isNetworkAvaliable(MainActivity.this)) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(mDisplayNamer.getText().toString())
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            spotsDialog.dismiss();

                            if (task.isSuccessful()) {
                                // Log.d(TAG, "User profile updated.");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Snackbar.make(findViewById(R.id.main_content), "Display Name Update Successful", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                                updateUI(user);
                            } else {

                                Snackbar.make(findViewById(R.id.main_content), "Display Name Update Failed", Snackbar.LENGTH_LONG)
                                        .setAction("Try Again", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                alertdialog.show();

                                                mSigninLayout.setVisibility(View.GONE);
                                                mMainLayout.setVisibility(View.VISIBLE);
                                                mPassResetLayout.setVisibility(View.GONE);
                                                mPassChangeLayout.setVisibility(View.GONE);
                                                mSignupConvoLayout.setVisibility(View.GONE);
                                                mSignupButton.setVisibility(View.GONE);
                                                mUpdateNameLayout.setVisibility(View.VISIBLE);
                                                mUpdateImageLayout.setVisibility(View.GONE);
                                                mCreateDialogLayout.setVisibility(View.GONE);
                                                mResetDialogLayout.setVisibility(View.GONE);


                                            }
                                        }).setActionTextColor(getResources().getColor(R.color.red)).show();


                            }

                        }
                    });
        } else {

            spotsDialog.dismiss();

            alertdialog.dismiss();

            //[START Snack Bar Data Loaded Successfully]
            Snackbar.make(findViewById(R.id.main_content), "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            //[END Snack Bar Data Loaded Successfully]

        }

    }
    // [END Display Name Update]

    // [START Form Validation]
    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }
    // [END Form Validation]

    // [START Form Validation]
    private boolean validatePassword() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        String password2 = mPasswordField2.getText().toString();
        if (TextUtils.isEmpty(password2)) {
            mPasswordField2.setError("Required.");
            valid = false;
        } else {
            mPasswordField2.setError(null);
        }

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(password2)) {

            if (!password.equals(password2)) {

                mPasswordField.setError("");
                mPasswordField2.setError("Mismatch");
                valid = false;
            } else {
                mPasswordField.setError(null);
                mPasswordField2.setError(null);
            }

        } else {

            valid = false;

        }

        return valid;
    }
    // [END Form Validation]

    // [START Form Validation]
    private boolean validateEmailReset() {
        boolean valid = true;

        FirebaseUser user = mAuth.getCurrentUser();

        String emailAdress = null;

        try {
            emailAdress = user.getEmail();
        } catch (Exception e) {
            Log.d(TAG, "Email0  " + e.getMessage());
        }

        //Log.d(TAG, "Email1  " + emailAdress);

        String email = mPassResetEmail.getText().toString();
        Log.d(TAG, "Email2  " + email);

        if (TextUtils.isEmpty(email)) {
            mPassResetEmail.setError("Required.");
            valid = false;
        } else {
            mPassResetEmail.setError(null);
        }

        if (emailAdress != null) {

            if (!emailAdress.equals(email)) {
                mPassResetEmail.setError("Wrong Email");
                valid = false;
            } else {
                mPassResetEmail.setError(null);
            }

        } else {
            mPassResetEmail.setError(null);
        }

        return valid;
    }
    // [END Form Validation]

    // [START Form Validation]
    private boolean validatePassChange() {
        boolean valid = true;

        String pass1 = mPassChangePass1.getText().toString();
        if (TextUtils.isEmpty(pass1)) {
            mPassChangePass1.setError("Required.");
            valid = false;
        } else {
            mPassChangePass1.setError(null);
        }

        String pass2 = mPassChangePass2.getText().toString();
        if (TextUtils.isEmpty(pass2)) {
            mPassChangePass2.setError("Required.");
            valid = false;
        } else {
            mPassChangePass2.setError(null);
        }

        if (!TextUtils.isEmpty(pass1) && !TextUtils.isEmpty(pass2)) {

            if (!pass1.equals(pass2)) {

                mPassChangePass1.setError("");
                mPassChangePass2.setError("Mismatch");
                valid = false;

            } else {
                mPassChangePass1.setError(null);
                mPassChangePass2.setError(null);
            }

        } else {

            valid = false;

        }

        return valid;
    }
    // [END Form Validation]

    private void sendPasswordResetEmail() {

        if (isNetworkAvaliable(MainActivity.this)) {

            if (!validateEmailReset()) {
                return;
            }

            final SpotsDialog spotsDialog = new SpotsDialog(MainActivity.this, "Resetting...");
            spotsDialog.show();

            FirebaseAuth auth = FirebaseAuth.getInstance();

            String emailAddress = mPassResetEmail.getText().toString();

            //String emailAddress = mPassResetEmail.getText().toString();

            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            spotsDialog.dismiss();

                            alertdialog.show();

                            if (task.isSuccessful()) {

                                //spotsDialog.dismiss();

                                mSigninLayout.setVisibility(View.GONE);
                                mMainLayout.setVisibility(View.VISIBLE);
                                mPassResetLayout.setVisibility(View.GONE);
                                mPassChangeLayout.setVisibility(View.GONE);
                                mUpdateImageLayout.setVisibility(View.GONE);
                                mUpdateNameLayout.setVisibility(View.GONE);
                                mResetErrorLayout.setVisibility(View.GONE);
                                mResetSuccessLayout.setVisibility(View.VISIBLE);
                                mTryResetPassagain.setVisibility(View.GONE);
                                mDismissResetPass.setVisibility(View.VISIBLE);
                                mCreateDialogLayout.setVisibility(View.GONE);
                                mResetDialogLayout.setVisibility(View.VISIBLE);

                            } else {


                                mSigninLayout.setVisibility(View.GONE);
                                mMainLayout.setVisibility(View.VISIBLE);
                                mPassResetLayout.setVisibility(View.GONE);
                                mPassChangeLayout.setVisibility(View.GONE);
                                mUpdateImageLayout.setVisibility(View.GONE);
                                mUpdateNameLayout.setVisibility(View.GONE);
                                mResetErrorLayout.setVisibility(View.VISIBLE);
                                mTryResetPassagain.setVisibility(View.VISIBLE);
                                mDismissResetPass.setVisibility(View.VISIBLE);
                                mResetSuccessLayout.setVisibility(View.GONE);
                                mCreateDialogLayout.setVisibility(View.GONE);
                                mResetDialogLayout.setVisibility(View.VISIBLE);

                            }
                        }
                    });
        } else {

            spotsDialog.dismiss();

            alertdialog.dismiss();

            //[START Snack Bar Data Loaded Successfully]
            Snackbar.make(findViewById(R.id.main_content), "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            //[END Snack Bar Data Loaded Successfully]

        }


    }

    private void updatePassword() {

        if (!validatePassChange()) {
            return;
        }

        //spotsDialog.show();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String newPassword = mPassChangePass1.getText().toString();

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            //spotsDialog.dismiss();

                            alertdialog.dismiss();

                            Snackbar.make(findViewById(R.id.main_content), "Password Change Successful", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();

                        } else {

                            //spotsDialog.dismiss();

                            alertdialog.dismiss();

                            Snackbar.make(findViewById(R.id.main_content), "Password Change Failed", Snackbar.LENGTH_LONG)
                                    .setAction("Try Again", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            alertdialog.show();

                                            mMainLayout.setVisibility(View.GONE);
                                            mPassResetLayout.setVisibility(View.GONE);
                                            mPassChangeLayout.setVisibility(View.VISIBLE);

                                        }
                                    }).setActionTextColor(getResources().getColor(R.color.red)).show();

                        }
                    }
                });

    }

    public static boolean isNetworkAvaliable(Context ctx) {
        boolean valid;

        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network

            valid = true;

        } else {

            valid = false;

        }

        return valid;

    }

    //<editor-fold desc="Crop Image And Upload">
    private void GalleryOpen() {

        GalIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(GalIntent, "Select Picture"), 2);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {

            CropImage();

        } else if (requestCode == 2) {

            if (data != null) {

                uri = data.getData();
                CropImage();

            }

        } else if (requestCode == 1) {

            if (data != null) {

                Bundle bundle = data.getExtras();
                Bitmap bitmap = bundle.getParcelable("data");
                mUpdateImageView.setImageBitmap(bitmap);

                Upload_Image();

            }

        }

    }

    private void CropImage() {

        try {

            CropIntent  = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri, "image/*");

            CropIntent.putExtra("crop", true);
            CropIntent.putExtra("outputX", 180);
            CropIntent.putExtra("outputY", 180);
            CropIntent.putExtra("aspectX", 3);
            CropIntent.putExtra("aspectX", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {
        }

    }

    private void Upload_Image() {

        alertdialog.dismiss();


        final FirebaseUser user = mAuth.getCurrentUser();

        final SpotsDialog proDialog = new SpotsDialog(MainActivity.this, "Updating  " + (int) progress + " %");

        //Log.d(TAG, "progressShow" + (int) progress);

        if (isNetworkAvaliable(MainActivity.this)) {
            StorageReference ref;
            byte[] data;

            ref = storageReference.child("images/" + user.getEmail() + ".jpg");
            mUpdateImageView.setDrawingCacheEnabled(true);
            mUpdateImageView.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) mUpdateImageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            data = baos.toByteArray();

            UploadTask uploadTask = ref.putBytes(data);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    progress = (100.0 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()));
                    //Log.d(TAG, "bytesComp  " + taskSnapshot.getTotalByteCount());
                    proDialog.show();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    updateUI(user);

                    progress = 0;
                    proDialog.dismiss();

                    //Log.d(TAG, "bytesComp2  " + taskSnapshot.getTotalByteCount() );

                    Snackbar.make(findViewById(R.id.main_content), "Image Update Successful", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progress = 0;
                    proDialog.dismiss();

                    Snackbar.make(findViewById(R.id.main_content), "Image Update Failed", Snackbar.LENGTH_LONG)
                            .setAction("Try Again", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    alertdialog.show();

                                    mSigninLayout.setVisibility(View.GONE);
                                    mMainLayout.setVisibility(View.VISIBLE);
                                    mPassResetLayout.setVisibility(View.GONE);
                                    mPassChangeLayout.setVisibility(View.GONE);
                                    mSignupConvoLayout.setVisibility(View.GONE);
                                    mSignupButton.setVisibility(View.GONE);
                                    mUpdateImageLayout.setVisibility(View.VISIBLE);
                                    mUpdateNameLayout.setVisibility(View.GONE);
                                    mCreateDialogLayout.setVisibility(View.GONE);
                                    mResetDialogLayout.setVisibility(View.GONE);


                                }
                            }).setActionTextColor(getResources().getColor(R.color.red)).show();

                }
            });
        } else {

            spotsDialog.dismiss();

            alertdialog.dismiss();

            //[START Snack Bar Data Loaded Successfully]
            Snackbar.make(findViewById(R.id.main_content), "No Internet Connection", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            //[END Snack Bar Data Loaded Successfully]

        }


    }
    //</editor-fold>


}