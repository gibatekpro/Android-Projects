package com.gibatekpro.stringsplitter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

public class Page extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 23;
    private static final String TAG = "TAG";
    EditText input, input_value;
    Button split, clear_input, clear_input2, clear_extracts, copy_extracts, paste, paste2, page, to_strings, to_strings_xml, fetch, submit;
    TextView total_extracts;
    TextView display;

    private static final int READ_REQUEST_CODE = 42;

    Uri uri2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        //Widgets
        input = findViewById(R.id.input);
        input_value = findViewById(R.id.input2);
        fetch = findViewById(R.id.fetchButton);
        submit = findViewById(R.id.submitButton);
        split = findViewById(R.id.split);
        display = findViewById(R.id.display);
        clear_extracts = findViewById(R.id.clear_extracts);
        copy_extracts = findViewById(R.id.copy_extracts);
        clear_input = findViewById(R.id.clear_input);
        clear_input2 = findViewById(R.id.clear_input2);
        paste = findViewById(R.id.paste);
        paste2 = findViewById(R.id.paste2);
        total_extracts = findViewById(R.id.total_etract);
        to_strings = findViewById(R.id.split2);
        to_strings_xml = findViewById(R.id.split3);

        display.setMovementMethod(new ScrollingMovementMethod());
        display.setTextIsSelectable(true);
        total_extracts.setVisibility(View.INVISIBLE);
        clear_extracts.setVisibility(View.INVISIBLE);
        copy_extracts.setVisibility(View.INVISIBLE);
        clear_input.setVisibility(View.INVISIBLE);
        clear_input2.setVisibility(View.INVISIBLE);
        paste.setVisibility(View.VISIBLE);
        paste2.setVisibility(View.VISIBLE);

        Object clipboardService = getSystemService(CLIPBOARD_SERVICE);
        final ClipboardManager clipboardManager = (ClipboardManager) clipboardService;

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                clear_input.setVisibility(View.INVISIBLE);
                paste.setVisibility(View.VISIBLE);

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

                } else {

                    split.setEnabled(true);

                }

            }
        });

        input_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                clear_input2.setVisibility(View.INVISIBLE);
                paste2.setVisibility(View.VISIBLE);

                if (input_value.getText().toString().isEmpty()) {

                    split.setEnabled(false);

                } else {

                    split.setEnabled(true);

                }


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                clear_input2.setVisibility(View.VISIBLE);
                paste2.setVisibility(View.INVISIBLE);

                if (input_value.getText().toString().isEmpty()) {

                    split.setEnabled(false);

                } else {

                    split.setEnabled(true);

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

                if (input_value.getText().toString().isEmpty()) {

                    clear_input2.setVisibility(View.INVISIBLE);
                    paste2.setVisibility(View.VISIBLE);
                    split.setEnabled(false);

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

        paste2.setOnClickListener(new View.OnClickListener() {
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

                input_value.setText(pasteData);

            }
        });

        clear_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                input.setText("");

            }
        });

        clear_input2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                input_value.setText("");

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

                Split();

                clear_extracts.setVisibility(View.VISIBLE);
                copy_extracts.setVisibility(View.VISIBLE);

            }

        });

        to_strings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Split_to_Strings();

                clear_extracts.setVisibility(View.VISIBLE);
                copy_extracts.setVisibility(View.VISIBLE);

            }

        });

        to_strings_xml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Split_to_StringsXml();

                clear_extracts.setVisibility(View.VISIBLE);
                copy_extracts.setVisibility(View.VISIBLE);

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

    public void Split() {
        String split;
        StringBuilder splits = new StringBuilder();
        StringBuilder splitsbuild = new StringBuilder();
        StringBuilder splitsbuild2 = new StringBuilder();
        StringBuilder splitsbuild3 = new StringBuilder();
        StringBuilder splitsbuild4 = new StringBuilder();
        StringBuilder splitsbuild5 = new StringBuilder();

        String addy = input.getText().toString();
        String parts[] = addy.split("<string name=\"");

        for (String part : parts) {

            splits.append(part).append("\n");

        }

        String splits2 = splits.toString();
        String parts2[] = splits2.split("</string>");

        for (String aParts2 : parts2) {

            splitsbuild.append(aParts2).append("\n");

        }

        String splits3 = splitsbuild.toString();
        String parts3[] = splits3.split(">");

        for (String aParts3 : parts3) {

            splitsbuild2.append(aParts3).append("\n");

        }

        String splits4 = splitsbuild2.toString();
        String parts4[] = splits4.split("\n");

        for (String aParts4 : parts4) {

            if (!aParts4.contains("\"")) {

                splitsbuild3.append("<item>").append(aParts4).append("</item>").append("\n");

            }
        }

        String splits5 = splitsbuild3.toString();
        String parts5[] = splits5.split("\n");

        for (String aParts5 : parts5) {

            if (!aParts5.contains("<item></item>") && !aParts5.contains("<item>    </item>")) {

                splitsbuild4.append(aParts5).append("\n");

            }

        }

        splitsbuild5.append("<string-array name=\"\">").append("\n").append(String.valueOf(splitsbuild4)).append("</string-array>");

        display.setText(splitsbuild5);
    }

    public void Split_to_Strings() {
        StringBuilder splitsbuild = new StringBuilder();

        String addy = input.getText().toString();
        String parts[] = addy.split("\n");

        for (String part : parts) {

            String parts2[] = part.split("\">");
            for (String part2 : parts2) {

                String parts3[] = part2.split("</string>");
                for (String part3 : parts3) {

                    if (!part3.contains("<string")) {
                        splitsbuild.append(part3).append("\n");
                    }
                }

            }

        }


        display.setText(splitsbuild);
    }

    public void Split_to_StringsXml() {
        StringBuilder splitsbuild = new StringBuilder();
        StringBuilder splitsbuild2 = new StringBuilder();

        String addy = input.getText().toString();
        String parts[] = addy.split("\n");

        for (String part : parts) {

            String split2[] = part.split("\">");
            for (String splits2 : split2) {


                if (!splits2.contains("</string>")) {


                    splitsbuild.append(splits2).append("\">").append("\n");
                }


            }

        }

        String head = String.valueOf(splitsbuild);
        String value = input_value.getText().toString();
        String tail = "</string>";

        String[] header = head.split("\n");
        String[] values = value.split("\n");

        for (int i = 0; i < header.length; i++) {

            splitsbuild2.append(header[i]).append(values[i]).append(tail).append("\n");

        }


        display.setText(splitsbuild2);
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
            ParcelFileDescriptor pfd = Page.this.getContentResolver().
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

}

