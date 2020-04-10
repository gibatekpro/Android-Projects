package com.gibatekpro.stakeapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //[START: Button declaration]
    Button btnRefresh, btnSubmit;
    //[END: Button declaration]

    //[START: EditText declaration]
    EditText txtProfit, txtOdd;
    //[END: EditText declaration]

    //[START: RecyclerView Components declaration]
    RecyclerView recyclerView;
    DataAdapter dataAdapter;
    List <DataList>  dataLists;
    LinearLayoutManager layoutManager;
    //[START: RecyclerView Components declaration]

    double odd = 2;
    double profit = 1000;
    int round = 1;
    double previousstake = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //<editor-fold desc="FindViews">
        //[START: Button inflater]
        btnRefresh = findViewById(R.id.bRefresh);
        btnSubmit = findViewById(R.id.bSubmit);
        //[END: Button inflater]

        //[START: RecyclerView Components Inflater]
        recyclerView = findViewById(R.id.my_recycler_view);
        //recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //[END: RecyclerView Components Inflater]
        //</editor-fold>

        dataLists = new ArrayList<>();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }


    void showDialog(){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.alertdialog, null);
        mBuilder.setTitle("Input Values");

        txtProfit = mView.findViewById(R.id.inProfit);
        txtOdd = mView.findViewById(R.id.inOdd);

        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                profit = Double.valueOf(txtProfit.getText().toString());
                odd = Double.valueOf(txtOdd.getText().toString());

                DataCalc dataCalc = new DataCalc(round, previousstake, odd, profit);

                dataLists.add(new DataList(dataCalc.getRound(),dataCalc.getStake(),dataCalc.getOdd(),dataCalc.getReturns(),dataCalc.getProfit()));
                dataAdapter = new DataAdapter(MainActivity.this, dataLists);
                recyclerView.setAdapter(dataAdapter);

                round++;

                previousstake = previousstake + dataCalc.getStake();

            }
        });
        mBuilder.setNegativeButton("Dejar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        dialog.show();
    }


}
