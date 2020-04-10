package com.gibatekpro.stringsplitter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

public class MainActivity extends AppCompatActivity {

    EditText input;
    Button split, clear_input, clear_extracts, copy_extracts, paste, page, fetch, submit;
    TextView display, total_extracts;
    CheckBox gmail_checkbox, yahoo_checkbox, hotmail_checkbox, msn_checkbox, aol_checkbox, extract_all_checkbox;
    LinearLayout linearLayout;

    private static final int READ_REQUEST_CODE = 42;

    private static final String TAG = "TAG";
    Uri uri2;


    public String gmail_emails = "", aol_emails = "", msn_emails = "",
            yahoo_emails = "", hotmail_emails = "", all_emails = "";

    public int gmail_emails_num = 0, aol_emails_num = 0, msn_emails_num = 0,
            yahoo_emails_num = 0, hotmail_emails_num = 0, all_emails_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Widgets
        input = findViewById(R.id.input);
        fetch = findViewById(R.id.fetch);
        submit = findViewById(R.id.submit);
        total_extracts = findViewById(R.id.total_etract);
        split = findViewById(R.id.split);
        display = findViewById(R.id.display);
        hotmail_checkbox = findViewById(R.id.hotmail);
        msn_checkbox = findViewById(R.id.msn);
        yahoo_checkbox = findViewById(R.id.yahoo);
        gmail_checkbox = findViewById(R.id.gmail);
        aol_checkbox = findViewById(R.id.aol);
        extract_all_checkbox = findViewById(R.id.all_email);
        clear_extracts = findViewById(R.id.clear_extracts);
        copy_extracts = findViewById(R.id.copy_extracts);
        clear_input = findViewById(R.id.clear_input);
        paste = findViewById(R.id.paste);
        page = findViewById(R.id.page);

        display.setMovementMethod(new ScrollingMovementMethod());
        display.setTextIsSelectable(true);
        total_extracts.setVisibility(View.INVISIBLE);
        clear_extracts.setVisibility(View.INVISIBLE);
        copy_extracts.setVisibility(View.INVISIBLE);
        clear_input.setVisibility(View.INVISIBLE);
        paste.setVisibility(View.VISIBLE);

        Object clipboardService = getSystemService(CLIPBOARD_SERVICE);
        final ClipboardManager clipboardManager = (ClipboardManager) clipboardService;

        //<editor-fold desc="Checkboxes Interactions and clicks">
        extract_all_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (extract_all_checkbox.isChecked()) {

                    all_emails = extract_all_check();

                    hotmail_checkbox.setEnabled(false);
                    yahoo_checkbox.setEnabled(false);
                    gmail_checkbox.setEnabled(false);
                    msn_checkbox.setEnabled(false);
                    aol_checkbox.setEnabled(false);

                    hotmail_checkbox.setChecked(false);
                    yahoo_checkbox.setChecked(false);
                    gmail_checkbox.setChecked(false);
                    msn_checkbox.setChecked(false);
                    aol_checkbox.setChecked(false);

                } else {

                    all_emails = "";
                    all_emails_num = 0;

                    hotmail_checkbox.setEnabled(true);
                    yahoo_checkbox.setEnabled(true);
                    gmail_checkbox.setEnabled(true);
                    msn_checkbox.setEnabled(true);
                    aol_checkbox.setEnabled(true);

                    hotmail_checkbox.setChecked(false);
                    yahoo_checkbox.setChecked(false);
                    gmail_checkbox.setChecked(false);
                    msn_checkbox.setChecked(false);
                    aol_checkbox.setChecked(false);


                }

            }
        });

        gmail_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (gmail_checkbox.isChecked() || hotmail_checkbox.isChecked()
                        || yahoo_checkbox.isChecked() || msn_checkbox.isChecked()
                        || aol_checkbox.isChecked()) {


                    extract_all_checkbox.setEnabled(false);

                    extract_all_checkbox.setChecked(false);

                } else {

                    extract_all_checkbox.setEnabled(true);

                    extract_all_checkbox.setChecked(false);

                }

                if (gmail_checkbox.isChecked()) {

                    gmail_emails = gmail_check();

                } else {

                    gmail_emails = "";
                    gmail_emails_num = 0;

                }

            }
        });

        hotmail_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (gmail_checkbox.isChecked() || hotmail_checkbox.isChecked()
                        || yahoo_checkbox.isChecked() || msn_checkbox.isChecked()
                        || aol_checkbox.isChecked()) {

                    extract_all_checkbox.setEnabled(false);

                    extract_all_checkbox.setChecked(false);

                } else {

                    extract_all_checkbox.setEnabled(true);

                    extract_all_checkbox.setChecked(false);

                }

                if (hotmail_checkbox.isChecked()) {

                    hotmail_emails = hotmail_check();

                } else {

                    hotmail_emails = "";
                    hotmail_emails_num = 0;

                }

            }
        });

        yahoo_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (gmail_checkbox.isChecked() || hotmail_checkbox.isChecked()
                        || yahoo_checkbox.isChecked() || msn_checkbox.isChecked()
                        || aol_checkbox.isChecked()) {

                    extract_all_checkbox.setEnabled(false);

                    extract_all_checkbox.setChecked(false);

                } else {

                    extract_all_checkbox.setEnabled(true);

                    extract_all_checkbox.setChecked(false);

                }

                if (yahoo_checkbox.isChecked()) {

                    yahoo_emails = yahoomail_check();

                } else {

                    yahoo_emails = "";
                    yahoo_emails_num = 0;

                }

            }
        });

        msn_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (gmail_checkbox.isChecked() || hotmail_checkbox.isChecked()
                        || yahoo_checkbox.isChecked() || msn_checkbox.isChecked()
                        || aol_checkbox.isChecked()) {

                    extract_all_checkbox.setEnabled(false);

                    extract_all_checkbox.setChecked(false);

                } else {

                    extract_all_checkbox.setEnabled(true);

                    extract_all_checkbox.setChecked(false);

                }

                if (msn_checkbox.isChecked()) {

                    msn_emails = msnmail_check();

                } else {

                    msn_emails = "";
                    msn_emails_num = 0;

                }

            }
        });

        aol_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (gmail_checkbox.isChecked() || hotmail_checkbox.isChecked()
                        || yahoo_checkbox.isChecked() || msn_checkbox.isChecked()
                        || aol_checkbox.isChecked()) {

                    extract_all_checkbox.setEnabled(false);

                    extract_all_checkbox.setChecked(false);

                } else {

                    extract_all_checkbox.setEnabled(true);

                    extract_all_checkbox.setChecked(false);

                }

                if (aol_checkbox.isChecked()) {

                    aol_emails = aolmail_check();

                } else {

                    aol_emails = "";
                    aol_emails_num = 0;

                }

            }
        });
        //</editor-fold>

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                clear_input.setVisibility(View.INVISIBLE);
                paste.setVisibility(View.VISIBLE);

                hotmail_checkbox.setChecked(false);
                yahoo_checkbox.setChecked(false);
                gmail_checkbox.setChecked(false);
                msn_checkbox.setChecked(false);
                aol_checkbox.setChecked(false);
                extract_all_checkbox.setChecked(false);

                if (input.getText().toString().isEmpty()) {

                    split.setEnabled(false);

                } else {

                    split.setEnabled(true);

                }


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                clear_input.setVisibility(View.VISIBLE);
                paste.setVisibility(View.INVISIBLE);

                hotmail_checkbox.setChecked(false);
                yahoo_checkbox.setChecked(false);
                gmail_checkbox.setChecked(false);
                msn_checkbox.setChecked(false);
                aol_checkbox.setChecked(false);
                extract_all_checkbox.setChecked(false);

                if (input.getText().toString().isEmpty()) {

                    split.setEnabled(false);

                } else {

                    split.setEnabled(true);

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

                if (input.getText().toString().isEmpty()) {

                    clear_input.setVisibility(View.INVISIBLE);
                    paste.setVisibility(View.VISIBLE);
                    split.setEnabled(false);

                    hotmail_checkbox.setChecked(false);
                    yahoo_checkbox.setChecked(false);
                    gmail_checkbox.setChecked(false);
                    msn_checkbox.setChecked(false);
                    aol_checkbox.setChecked(false);
                    extract_all_checkbox.setChecked(false);

                } else {

                    split.setEnabled(true);

                }

            }
        });

        copy_extracts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String extracted_emails = display.getText().toString();

                ClipData clipData = ClipData.newPlainText("extracts", extracted_emails);
                clipboardManager.setPrimaryClip(clipData);

                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.main_content), "Copied to clipboard", Snackbar.LENGTH_SHORT);
                snackbar.show();

            }
        });

        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pasteData = "";

                try {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                    // If it does contain data, decide if you can handle the data.
                    if (!(clipboard.hasPrimaryClip())) {

                    } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {

                        // since the clipboard has data but it is not plain text

                    } else {

                        //since the clipboard contains plain text.
                        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

                        // Gets the clipboard as text.
                        pasteData = item.getText().toString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d("TAG", "paster" + pasteData);

                input.setText(pasteData);

            }
        });

        clear_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                input.setText("");

            }
        });

        clear_extracts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                display.setText("");

                total_extracts.setVisibility(View.INVISIBLE);
                clear_extracts.setVisibility(View.INVISIBLE);
                copy_extracts.setVisibility(View.INVISIBLE);

            }
        });

        split.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder extracts = new StringBuilder();

                extracts.append(all_emails).append(aol_emails).append(gmail_emails)
                        .append(hotmail_emails).append(msn_emails).append(yahoo_emails);

                display.setText(extracts);

                total_extracts.setVisibility(View.VISIBLE);
                clear_extracts.setVisibility(View.VISIBLE);
                copy_extracts.setVisibility(View.VISIBLE);

                total_extracts.setText(String.format("Number of Emails Exctracted    %s",
                        String.valueOf(gmail_emails_num
                                + hotmail_emails_num
                                + yahoo_emails_num
                                + aol_emails_num
                                + msn_emails_num
                                + all_emails_num)));

            }

        });

        page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Page.class);

                startActivity(intent);

            }
        });

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*StringBuilder stringBuilder = new StringBuilder();

                mQuoteBank = new QuoteBank(Page.this);
                mLines = mQuoteBank.readLine(mPath);
                for (String string : mLines) {

                    stringBuilder.append(string);

                }*/

                performReadFromFile();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alterDocument(uri2);

            }
        });


    }

    public void performReadFromFile() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        }

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("text/plain");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i(TAG, "Uri: " + uri.toString());
                try {
                    uri2 = uri;
                    input.setText(readTextFromUri(uri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String readTextFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);

        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    private void alterDocument(Uri uri) {

        String value = display.getText().toString();

        Log.d(TAG, "alterDocument: " + value);

        try {
            ParcelFileDescriptor pfd = MainActivity.this.getContentResolver().
                    openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());
            fileOutputStream.write(value.getBytes());
            // Let the document provider know you're done by closing the stream.
            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String gmail_check() {
        String gmail;
        StringBuilder splits = new StringBuilder();
        StringBuilder splitsbuild = new StringBuilder();

        String addy = input.getText().toString();
        String parts[] = addy.split("\n");

        for (String part : parts) {

            splits.append(part).append(" ");

        }

        String splits2 = splits.toString();
        String parts2[] = splits2.split(" ");

        gmail_emails_num = 0;

        for (String aParts2 : parts2) {

            if (aParts2.contains("@gmail.com")) {

                gmail_emails_num++;

                splitsbuild.append(aParts2).append("\n");

            }

        }

        gmail = splitsbuild.toString();

        return gmail;
    }

    public String hotmail_check() {
        String hotmail;
        StringBuilder splits = new StringBuilder();
        StringBuilder splitsbuild = new StringBuilder();

        String addy = input.getText().toString();
        String parts[] = addy.split("\n");

        for (String part : parts) {

            splits.append(part).append(" ");

        }

        String splits2 = splits.toString();
        String parts2[] = splits2.split(" ");

        hotmail_emails_num = 0;

        for (String aParts2 : parts2) {

            if (aParts2.contains("@hotmail.com")) {

                hotmail_emails_num++;

                splitsbuild.append(aParts2).append("\n");

            }

        }

        hotmail = splitsbuild.toString();

        return hotmail;
    }

    public String yahoomail_check() {
        String yahoomail;
        StringBuilder splits = new StringBuilder();
        StringBuilder splitsbuild = new StringBuilder();

        String addy = input.getText().toString();
        String parts[] = addy.split("\n");

        for (String part : parts) {

            splits.append(part).append(" ");

        }

        String splits2 = splits.toString();
        String parts2[] = splits2.split(" ");

        yahoo_emails_num = 0;

        for (String aParts2 : parts2) {

            if (aParts2.contains("@yahoo.com")) {

                yahoo_emails_num++;

                splitsbuild.append(aParts2).append("\n");

            }

        }

        yahoomail = splitsbuild.toString();

        return yahoomail;
    }

    public String msnmail_check() {
        String msnmail;
        StringBuilder splits = new StringBuilder();
        StringBuilder splitsbuild = new StringBuilder();

        String addy = input.getText().toString();
        String parts[] = addy.split("\n");

        for (String part : parts) {

            splits.append(part).append(" ");

        }

        String splits2 = splits.toString();
        String parts2[] = splits2.split(" ");

        msn_emails_num = 0;

        for (String aParts2 : parts2) {

            if (aParts2.contains("@msn.com")) {

                msn_emails_num++;

                splitsbuild.append(aParts2).append("\n");

            }

        }

        msnmail = splitsbuild.toString();

        return msnmail;
    }

    public String aolmail_check() {
        String aolmail;
        StringBuilder splits = new StringBuilder();
        StringBuilder splitsbuild = new StringBuilder();

        String addy = input.getText().toString();
        String parts[] = addy.split("\n");

        for (String part : parts) {

            splits.append(part).append(" ");

        }

        String splits2 = splits.toString();
        String parts2[] = splits2.split(" ");

        aol_emails_num = 0;

        for (String aParts2 : parts2) {

            if (aParts2.contains("@aol.com")) {

                aol_emails_num++;

                splitsbuild.append(aParts2).append("\n");

            }

        }

        aolmail = splitsbuild.toString();

        return aolmail;
    }

    public String extract_all_check() {
        String allmail;
        StringBuilder splits = new StringBuilder();
        StringBuilder splitsbuild = new StringBuilder();

        String addy = input.getText().toString();
        String parts[] = addy.split("\n");

        for (String part : parts) {

            splits.append(part).append(" ");

        }

        String splits2 = splits.toString();
        String parts2[] = splits2.split(" ");

        all_emails_num = 0;

        for (String aParts2 : parts2) {

            if (aParts2.contains("@")) {

                all_emails_num++;

                splitsbuild.append(aParts2).append("\n");

            }

        }

        allmail = splitsbuild.toString();

        return allmail;
    }

}


    /*StringBuilder splits = new StringBuilder();
    StringBuilder splitsbuild = new StringBuilder();

    String addy = input.getText().toString();
    String parts[] = addy.split("\n");

        for (int i = 0; i < parts.length; i++) {

        splits.append(parts[i] + " ");

        }

        String splits2 = splits.toString();
        String parts2[] = splits2.split(" ");

        for (int i = 0; i < parts2.length; i++) {

        if (parts2[i].contains("@gmail.com")) {

        splitsbuild.append(parts2[i] + "\n");

        }

        }*/
