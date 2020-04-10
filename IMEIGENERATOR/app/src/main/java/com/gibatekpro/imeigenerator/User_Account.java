package com.gibatekpro.imeigenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gibatekpro.imeigenerator.Auth.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

public class User_Account extends AppCompatActivity {

    //Droid Appliance//
    View mView;
    LinearLayout mRegLay, mSignEmLay, mRecLay;
    TextView mRecInfo;
    static FirebaseAuth auth;
    //FirebaseDatabase db;
    //DatabaseReference users;
    SpotsDialog spotsDialog;
    //Droid Appliance//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_layout);

        mRegLay = findViewById(R.id.reg_lay);
        mSignEmLay = findViewById(R.id.sign_em_lay);
        //mSignPassLay = findViewById(R.id.sign_pass_lay);
        mRecLay = findViewById(R.id.rec_lay);
        mRecInfo = findViewById(R.id.rec_txt_info);

        //Droid Appliance//
        //firebase
        auth = FirebaseAuth.getInstance();
        //db = FirebaseDatabase.getInstance();
        //users = db.getReference("Users");

        //Spots Dialog (Loading)
        spotsDialog = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Loading...")
                .setTheme(R.style.CustomSpotsDialog)
                .build();
        // Droid Appliance//


        Button cButton = findViewById(R.id.cancel_button);
        cButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_Account.this, MainActivity.class);
                startActivity(intent);
            }
        });

        if (checkNew()) {
            showRegister();
        }else {

            // Get the Intent that started this activity and extract the string

            String newString;
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if(extras == null) {
                    showNothing();

                    spotsDialog.show();

                    //Check if user is signed in (non-null) and update UI accordingly.
                    try {
                        FirebaseUser currentUser = auth.getCurrentUser();
                        updateUI(currentUser);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    newString= extras.getString("STRING_I_NEED");

                    if(newString == null) {
                        newString = "empty string";
                        Intent intent = new Intent(User_Account.this, MainActivity.class);
                        startActivity(intent);
                    }

                    if (newString.equals("regcreate")) {
                        showRegister();
                    } else if (newString.equals("accLogin")) {
                        showLogIn();
                    } else if (newString.equals("resetpass")) {
                        showRecoverAccInfo();

                    }
                }
            } else {

                newString= (String) savedInstanceState.getSerializable("STRING_I_NEED");

                if(newString == null) {
                    newString = "empty string";
                    Intent intent = new Intent(User_Account.this, MainActivity.class);
                    startActivity(intent);
                }

                if (newString.equals("regcreate")) {
                    showRegister();
                } else if (newString.equals("accLogin")) {
                    showLogIn();
                } else if (newString.equals("resetpass")) {
                    showRecoverAccInfo();

                }
            }
            //showAccountDialog();

        }

    }

    private void updateUI(FirebaseUser currentUser) {

        spotsDialog.dismiss();

        if (currentUser != null) {

            //If there is a signed in user, just start the main activity
            Intent intent = new Intent(User_Account.this, MainActivity.class);
            startActivity(intent);

        } else {

            //If there is no signed in user, then show option to sign in
            showLogIn();

        }

    }
    // [END Check For Current User]


    //Droid Appliance
    //[START user interaction function]
    public void showAccountDialog() {

        mRegLay = findViewById(R.id.reg_lay);
        mSignEmLay = findViewById(R.id.sign_em_lay);
        //mSignPassLay = findViewById(R.id.sign_pass_lay);
        mRecLay = findViewById(R.id.rec_lay);
        mRecInfo = findViewById(R.id.rec_txt_info);

        showRegister();

    }
    //[END user interaction function]

    public void showNothing() {
        mSignEmLay.setVisibility(View.GONE);
        mRecLay.setVisibility(View.GONE);
        //mSignPassLay.setVisibility(View.GONE);
        mRegLay.setVisibility(View.GONE);
    }

    public void showLogIn() {

        spotsDialog.dismiss();

        mSignEmLay.setVisibility(View.VISIBLE);
        mRecLay.setVisibility(View.GONE);
        //mSignPassLay.setVisibility(View.GONE);
        mRegLay.setVisibility(View.GONE);

        final EditText mEdEmail = findViewById(R.id.sign_em_ed_email);
        TextView mTxtReg = findViewById(R.id.sign_em_txt_reg);
        Button mNext = findViewById(R.id.sign_em_btn_nxt);
        TextView mTxtForgPass = findViewById(R.id.sign_pass_txt_forgpass);
        final EditText mEdPass = findViewById(R.id.sign_pass_edt_password);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager) User_Account.this.getSystemService(
                        Context.INPUT_METHOD_SERVICE
                );
                inputMethodManager.hideSoftInputFromWindow(mEdEmail.getWindowToken(), 0);

                if (!logInInputValid(mEdEmail, mEdPass)) {
                    return;
                }

                userLogin(mEdEmail.getText().toString(), mEdPass.getText().toString());
            }
        });

        mTxtForgPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverAccInfo();
            }
        });

        mTxtReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegister();
            }
        });

    }

    public void userLogin(final String lName, String lPass) {

        spotsDialog.show();
        auth.signInWithEmailAndPassword(lName, lPass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        spotsDialog.dismiss();
                        Intent intent = new Intent(User_Account.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        spotsDialog.dismiss();
                        Snackbar.make(findViewById(R.id.auth_layout), e.getMessage(), Snackbar.LENGTH_LONG)
                                .show();
                    }
                });

    }

    //<editor-fold desc="Signing Password layout">
    /*public void showSignPass() {
        mSignEmLay.setVisibility(View.GONE);
        mRecLay.setVisibility(View.GONE);
        //mSignPassLay.setVisibility(View.VISIBLE);
        mRegLay.setVisibility(View.GONE);

        //TextView mTxtEmail = findViewById(R.id.sign_pass_txt_email);
        //Button mNxt = findViewById(R.id.sign_pass_btn_nxt);

    }*/
    //</editor-fold>

    public void showRegister() {

        spotsDialog.dismiss();

        mSignEmLay.setVisibility(View.GONE);
        mRecLay.setVisibility(View.GONE);
        //mSignPassLay.setVisibility(View.GONE);
        mRegLay.setVisibility(View.VISIBLE);

        final EditText mRegedtEmail = findViewById(R.id.regedt_email);
        final EditText mRegedtPass = findViewById(R.id.regedt_pass);
        final EditText mRegedtConpass = findViewById(R.id.regedt_conpass);
        TextView mSigninInstead = findViewById(R.id.regtxt_sign_in);
        TextView mTxtLearnMore = findViewById(R.id.sign_em_txt_learnmore);
        Button mNxt = findViewById(R.id.regbtn_nxt);


        mTxtLearnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(R.id.auth_layout), "Coming Soon", Snackbar.LENGTH_SHORT)
                        .show();
            }
        });


        //<editor-fold desc="Register">
        mNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                InputMethodManager inputMethodManager = (InputMethodManager) User_Account.this.getSystemService(
                        Context.INPUT_METHOD_SERVICE
                );
                inputMethodManager.hideSoftInputFromWindow(mRegedtEmail.getWindowToken(), 0);


                if (!regInputValid(mRegedtEmail, mRegedtPass, mRegedtConpass)) {
                    return;
                }

                if (!anEmail(mRegedtEmail.getText().toString())) {
                    mRegedtEmail.setError("Not an email");
                    return;
                }

                if (!passwordOK(mRegedtPass, mRegedtConpass)) {
                    return;
                }

                userRegister(mRegedtEmail.getText().toString(), mRegedtPass.getText().toString());
            }
        });
        //</editor-fold>

        mSigninInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogIn();
            }
        });

    }

    public void userRegister(final String uEmail, final String uPass1) {
        //<editor-fold desc="Register">

        spotsDialog.show();
        auth.createUserWithEmailAndPassword(uEmail, uPass1)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        User user = new User();
                        //user.setuName(uName);
                        user.setuEmail(uEmail);
                        user.setuPassword(uPass1);

                        userLogin(uEmail, uPass1);

                        //Use email to key
                        /*users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        userLogin(uEmail, uPass1);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });*/

                        spotsDialog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        spotsDialog.dismiss();

                        Snackbar.make(findViewById(R.id.auth_layout), e.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Sign In", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showLogIn();
                                    }
                                })
                                .show();
                    }
                });
        //</editor-fold>
    }

    public void showRecoverAccInfo() {

        spotsDialog.dismiss();

        mSignEmLay.setVisibility(View.GONE);
        mRecLay.setVisibility(View.VISIBLE);
        //mSignPassLay.setVisibility(View.GONE);
        mRegLay.setVisibility(View.GONE);
        mRecInfo.setVisibility(View.GONE);

        final EditText mEdtRecEmail = findViewById(R.id.rec_ed_email);
        LinearLayout mEdtRecEmailLay = findViewById(R.id.rec_ed_email1);
        TextView mTxtRecSignin = findViewById(R.id.rec_signin);
        TextView mTxtRecEmail = findViewById(R.id.rec_txt_email);
        Button mNxt = findViewById(R.id.rec_btn_nxt);
        mNxt.setVisibility(View.VISIBLE);
        mEdtRecEmailLay.setVisibility(View.VISIBLE);
        mTxtRecEmail.setVisibility(View.GONE);


        mNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager) User_Account.this.getSystemService(
                        Context.INPUT_METHOD_SERVICE
                );
                inputMethodManager.hideSoftInputFromWindow(mEdtRecEmail.getWindowToken(), 0);

                sendResetEmail(mEdtRecEmail.getText().toString());
            }
        });

        mTxtRecSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogIn();
            }
        });

    }

    private void sendResetEmail(final String cEmail) {

        spotsDialog.show();

        auth.sendPasswordResetEmail(cEmail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        spotsDialog.dismiss();
                        showRecoverAccInfo2(cEmail);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        spotsDialog.dismiss();
                        Snackbar.make(findViewById(R.id.auth_layout), e.getMessage(), Snackbar.LENGTH_LONG)
                                .show();

                    }
                });

    }


    public void showRecoverAccInfo2(String vEmail) {
        mSignEmLay.setVisibility(View.GONE);
        mRecLay.setVisibility(View.VISIBLE);
        //mSignPassLay.setVisibility(View.GONE);
        mRegLay.setVisibility(View.GONE);
        mRecInfo.setVisibility(View.VISIBLE);

        LinearLayout mEdtRecEmail1 = findViewById(R.id.rec_ed_email1);
        TextView mTxtRecSignin = findViewById(R.id.rec_signin);
        TextView mTxtRecEmail = findViewById(R.id.rec_txt_email);
        Button mNxt = findViewById(R.id.rec_btn_nxt);
        mNxt.setVisibility(View.GONE);
        mEdtRecEmail1.setVisibility(View.GONE);
        mTxtRecEmail.setVisibility(View.VISIBLE);

        mTxtRecEmail.setText(vEmail);

        mTxtRecSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogIn();
            }
        });

    }

    public boolean anEmail(String anEmail) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (anEmail == null) {
            return false;
        }
        return pat.matcher(anEmail).matches();
    }

    public boolean regInputValid(EditText email, EditText password, EditText password2) {


        if (email.getText().toString().isEmpty()) {
            email.setError("Required");
            return false;
        }

        if (password.getText().toString().isEmpty()) {
            password.setError("Required");
            return false;
        }

        if (password2.getText().toString().isEmpty()) {
            password2.setError("Required");
            return false;
        }

        return true;
    }

    public boolean passwordOK(EditText password, EditText password2) {
        //boolean valid = true;

        String pass1 = password.getText().toString();
        String pass2 = password2.getText().toString();
        int i = -1;

        String[] passSplit = pass1.split("");

        for (String split : passSplit) {
            i++;
        }

        if (i < 6) {

            password.setError("Password is short");
            return false;
        }

        if (!pass1.equals(pass2)) {
            password2.setError("Passwords do not match");
            return false;
        }

        return true;
    }

    public boolean logInInputValid(EditText email, EditText password) {

        if (email.getText().toString().isEmpty()) {
            email.setError("Required");
            return false;
        }

        if (password.getText().toString().isEmpty()) {
            password.setError("Required");
            return false;
        }

        if (!anEmail(email.getText().toString())) {
            email.setError("Not an Email");
            return false;
        }

        return true;
    }

    public static void signOut() {
        auth.signOut();
    }

    public static String getEmail() {
        FirebaseUser currentUser = auth.getCurrentUser();
        return currentUser.getEmail();
    }

    public static boolean isLoggedIn() {

        FirebaseUser currentUser = auth.getCurrentUser();

        return currentUser != null;
    }

    //Droid Appliance//


    @Override
    public void onBackPressed() {
        Snackbar.make(findViewById(R.id.auth_layout), "Exit App?", Snackbar.LENGTH_LONG)
                .setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishAffinity();
                        System.exit(0);
                    }
                })
                .show();
    }


    //Show Registration The first time on Start
    private boolean checkNew() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean showTutorioals = sharedPreferences.getBoolean("REGBOARDINGIMEIGENERATOR", true);

        if (showTutorioals) {

            //showRegister();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("REGBOARDINGIMEIGENERATOR", false);
            editor.apply();

            return true;

        }

        return false;

    }


}
