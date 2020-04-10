package com.gibatekpro.tipsnodds.Admin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.gibatekpro.tipsnodds.Admin.Adapter.ListItemAdapter;
import com.gibatekpro.tipsnodds.Admin.Model.ToDo;
import com.gibatekpro.tipsnodds.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class frag_pending extends Fragment {

    //Calender
    Calendar mCurrentDate;
    int day, month, year;

    //Text Fields
    public EditText progress_edit, country_edit, date_edit, time_edit, home_club_edit, away_club_edit, tip_edit, odds_edit;

    //Buttons
    private Button mcal_button, mcountry_button, msubmit_button;

    //LOG TAG Declaration
    private static final String TAG = "FireStore";

    //Countries String
    String mcountry;

    //initialize flag
    private int flag = 0;
    private int check_icon = 0;

    //Current Date from locale
    String dater = new SimpleDateFormat("yyy-MM-dd", Locale.getDefault()).format(new Date());

    //Alert Dialog Builder
    AlertDialog.Builder builder;

    //Calender Format
    int year_x, month_x, day_x;

    //Date Picker Dialog
    static final int DIALOG_ID = 0;

    //Cloud Firestore initialization
    FirebaseFirestore db;

    //Model Declaration
    List<ToDo> toDoList = new ArrayList<>();

    //RecyclerView Declaration
    RecyclerView listItem;
    RecyclerView.LayoutManager layoutManager;


    public static boolean isUpdate = false; //false to check is update or is add new
    public static String idUpdate = ""; //Id of item need to update

    ListItemAdapter adapter;

    //SpotsDialog spotsDialog;

    String vs = "Vs";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        return inflater.inflate(R.layout.frag_pending, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        //Init Firebase
        db = FirebaseFirestore.getInstance();

        //View
        //spotsDialog = new SpotsDialog(getActivity());
        date_edit = getView().findViewById(R.id.date_edit);
        progress_edit = getView().findViewById(R.id.progress_edit);
        time_edit = getView().findViewById(R.id.time_edit);
        country_edit = getView().findViewById(R.id.country_edit);
        home_club_edit = getView().findViewById(R.id.home_club_edit);
        away_club_edit = getView().findViewById(R.id.away_club_edit);
        tip_edit = getView().findViewById(R.id.tip_edit);
        odds_edit = getView().findViewById(R.id.odds_edit);

        //Buttons
        mcal_button = getView().findViewById(R.id.cal_button);
        mcountry_button = getView().findViewById(R.id.country_button);
        msubmit_button = getView().findViewById(R.id.submit_button);

        //{START Get Date From Calender Dialog] 2
        mCurrentDate = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        year = mCurrentDate.get(Calendar.YEAR);
        month = mCurrentDate.get(Calendar.MONTH);
        month = month + 1;
        date_edit.setText(year + "-" + month + "-" + day);
        //{END Get Date From Calender Dialog]


        mcal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        date_edit.setText(year + "-" + month + "-" + dayOfMonth);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        //[START Button Functions]
        mcountry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectCountry();

            }
        });


        msubmit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Make sure no field is empty in data to be sent
                if (!validateForm()) {
                    return;
                }

                //Add new
                if (!isUpdate) {
                    setData(progress_edit.getText().toString(), date_edit.getText().toString(), time_edit.getText().toString(), country_edit.getText().toString(), home_club_edit.getText().toString(), away_club_edit.getText().toString(), tip_edit.getText().toString(), odds_edit.getText().toString());
                } else {
                    updateData(progress_edit.getText().toString(), date_edit.getText().toString(), time_edit.getText().toString(), country_edit.getText().toString(), home_club_edit.getText().toString(), away_club_edit.getText().toString(), tip_edit.getText().toString(), odds_edit.getText().toString());
                    isUpdate = !isUpdate; // reset flag
                }
            }
        });

        listItem = getView().findViewById(R.id.listTodo_today);
        listItem.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        listItem.setLayoutManager(layoutManager);

        //[START Load Data From Main Activity]
        ((AdminActivity)getActivity()).load_pending();
        //[END Load Data From Main Activity]


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals("DELETE PENDING"))
            deleteItem(item.getOrder());

        if (item.getTitle().equals("MOVE PENDING TO PLAYED"))
            moveToPlayed(progress_edit.getText().toString(), date_edit.getText().toString(), time_edit.getText().toString(), country_edit.getText().toString(), home_club_edit.getText().toString(), away_club_edit.getText().toString(), tip_edit.getText().toString(), odds_edit.getText().toString());

        return super.onContextItemSelected(item);
    }

    private void moveToPlayed(String progress_edit, String date_edit, String time_edit, String country_edit, String home_club_edit, String away_club_edit, String tip_edit, String odds_edit) {

        //Random id
        String id = UUID.randomUUID().toString();
        Map<String, Object> todo = new HashMap<>();
        todo.put("Progress", progress_edit);
        todo.put("id", id);
        todo.put("Country", country_edit);
        todo.put("Date", date_edit);
        todo.put("Time", time_edit);
        todo.put("Home", home_club_edit);
        todo.put("Away", away_club_edit);
        todo.put("Tips", tip_edit);
        todo.put("Odds", odds_edit);

        db.collection("Played").document(id)
                .set(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                //Resfresh data
                //[START Load Data From Main Activity]
                ((AdminActivity)getActivity()).load_pending();
                //[END Load Data From Main Activity]

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    private void deleteItem(int index) {

        db.collection("Today")
                .document(toDoList.get(index).getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        //[START Load Data From Main Activity]
                        ((AdminActivity)getActivity()).load_pending();
                        //[END Load Data From Main Activity]

                        Log.d(TAG, "Deleted!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void updateData(String progress_edit, String date_edit, String time_edit, String country_edit, String home_club_edit, String away_club_edit, String tip_edit, String odds_edit) {

        db.collection("Today").document(idUpdate)
                .update("Progress", progress_edit,"Date", date_edit, "Time", time_edit, "Country", country_edit, "Home", home_club_edit, "Away", away_club_edit, "Tips", tip_edit, "Odds", odds_edit)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        try {
            //Realtime Update refresh data
            //Can be used in Tips n Odds
            db.collection("Today").document(idUpdate)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {

                            //[START Load Data From Main Activity]
                            ((AdminActivity)getActivity()).load_pending();
                            //[END Load Data From Main Activity]


                        }
                    });
        } catch (Exception e) {

        }

    }

    public void loadData(Task<QuerySnapshot> task) {

        if (toDoList.size() > 0)
            toDoList.clear(); //Remove Old Value

        for (DocumentSnapshot doc : task.getResult()) {


            getCountry(doc.getString("Country"));

            //[START integer for Check_icon]
            getNumber(doc.getString("Progress"));
            //[START integer for Check_icon]

            ToDo toDo = new ToDo(check_icon,
                    flag,
                    doc.getString("Progress"),
                    doc.getString("id"),
                    doc.getString("Country"),
                    doc.getString("Date"),
                    doc.getString("Home"),
                    vs,
                    doc.getString("Away"),
                    doc.getString("Tips"),
                    doc.getString("Time"),
                    doc.getString("Odds"));

            toDoList.add(toDo);

        }


        adapter = new ListItemAdapter(frag_pending.this, toDoList);
        listItem.setAdapter(adapter);

    }

    private void setData(String progress_edit, String date_edit, String time_edit, String country_edit, String home_club_edit, String away_club_edit, String tip_edit, String odds_edit) {
        //Random id
        String id = UUID.randomUUID().toString();
        Map<String, Object> todo = new HashMap<>();
        todo.put("Progress", progress_edit);
        todo.put("id", id);
        todo.put("Country", country_edit);
        todo.put("Date", date_edit);
        todo.put("Time", time_edit);
        todo.put("Home", home_club_edit);
        todo.put("Away", away_club_edit);
        todo.put("Tips", tip_edit);
        todo.put("Odds", odds_edit);

        db.collection("Today").document(id)
                .set(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                //Resfresh data
                //[START Load Data From Main Activity]
                ((AdminActivity)getActivity()).load_pending();
                //[END Load Data From Main Activity]


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    //[START getNumber for Check]
    private void getNumber(String mNumber) {
        switch (mNumber) {
            case "0":
                check_icon = R.drawable.ic_grey_icon;
                break;
            case "1":
                check_icon = R.drawable.ic_green_icon;
                break;
            case "2":
                check_icon = R.drawable.ic_red_icon;
                break;
            default:
                check_icon = 0;
        }
    }
    //[END getNumber for Check]

    //[START getCountry Method for Flags]
    private void getCountry(@NonNull String country) {

        switch (country) {
            case "Afghanistan":
                flag = R.drawable.afghanistan_flag;
                break;

            case "Albania":
                flag = R.drawable.albania_flag;
                break;

            case "Algeria":
                flag = R.drawable.algeria_flag;
                break;

            case "A.Samoa":
                flag = R.drawable.american_samoa_flag;
                break;

            case "Andorra":
                flag = R.drawable.andorra_flag;
                break;

            case "Angola":
                flag = R.drawable.angola_flag;
                break;

            case "Antigua":
                flag = R.drawable.antigua_and_barbuda_flag;
                break;

            case "Argentina":
                flag = R.drawable.argentina_flag;
                break;

            case "Armenia":
                flag = R.drawable.armenia;
                break;

            case "Aruba":
                flag = R.drawable.aruba;
                break;

            case "Australia":
                flag = R.drawable.australia_flag;
                break;

            case "Austria":
                flag = R.drawable.austria_flag;
                break;

            case "Azerbaijan":
                flag = R.drawable.azerbaijan_flag;
                break;

            case "Bahamas":
                flag = R.drawable.bahamas_flag;
                break;

            case "Bahrain":
                flag = R.drawable.bahrain_flag;
                break;

            case "Bangladesh":
                flag = R.drawable.bangladesh_flag;
                break;

            case "Barbados":
                flag = R.drawable.barbados_flag;
                break;

            case "Belarus":
                flag = R.drawable.belarus_flag;
                break;

            case "Belgium":
                flag = R.drawable.belgium_flag;
                break;

            case "Belize":
                flag = R.drawable.belize_flag;
                break;

            case "Benin":
                flag = R.drawable.benin_flag;
                break;

            case "Bermuda":
                flag = R.drawable.bermuda_flag;
                break;

            case "Bhutan":
                flag = R.drawable.bhutan_flag;
                break;

            case "Bolivia":
                flag = R.drawable.bolivia_flag;
                break;

            case "Bosnia":
                flag = R.drawable.bosnia_and_herzegovina_flag;
                break;

            case "Botswana":
                flag = R.drawable.botswana_flag;
                break;

            case "Brazil":
                flag = R.drawable.brazil_flag;
                break;

            case "B.V. Island":
                flag = R.drawable.british_virgin_islands_flag;
                break;

            case "Brunei":
                flag = R.drawable.brunei_flag;
                break;

            case "Bulgaria":
                flag = R.drawable.bulgaria_flag;
                break;

            case "B. Faso":
                flag = R.drawable.burkina_faso_flag;
                break;

            case "Burundi":
                flag = R.drawable.burundi;
                break;

            case "Cambodia":
                flag = R.drawable.cambodia_flag;
                break;

            case "Cameroon":
                flag = R.drawable.cameroon_flag;
                break;

            case "Canada":
                flag = R.drawable.canada_flag;
                break;

            case "C. Verde":
                flag = R.drawable.cape_verde_flag;
                break;

            case "C. Island":
                flag = R.drawable.cayman_islands_flag;
                break;

            case "CAR":
                flag = R.drawable.central_african_republic_flag;
                break;

            case "Chad":
                flag = R.drawable.chad_flag;
                break;

            case "Chile":
                flag = R.drawable.chile_flag;
                break;

            case "China":
                flag = R.drawable.china_flag;
                break;

            case "Ch. Taipei":
                flag = R.drawable.chinese_taipei_flag;
                break;

            case "Colombia":
                flag = R.drawable.colombia_flag;
                break;

            case "Comoros":
                flag = R.drawable.comoros_flag;
                break;

            case "Congo":
                flag = R.drawable.congo_flag;
                break;

            case "Costa Rica":
                flag = R.drawable.costa_rica_flag;
                break;

            case "Croatia":
                flag = R.drawable.croatia_flag;
                break;

            case "Cuba":
                flag = R.drawable.cuba_flag;
                break;

            case "Cyprus":
                flag = R.drawable.cyprus_flag;
                break;

            case "Czech":
                flag = R.drawable.czechoslovakia_flag;
                break;

            case "DR Congo":
                flag = R.drawable.dr_congo_flag;
                break;

            case "Denmark":
                flag = R.drawable.denmark_flag;
                break;

            case "Djibouti":
                flag = R.drawable.djibouti_flag;
                break;

            case "Dominica":
                flag = R.drawable.dominica_flag;
                break;

            case "Dominican R.":
                flag = R.drawable.dominican_republic_flag;
                break;

            case "Ecuador":
                flag = R.drawable.ecuador_flag;
                break;

            case "Egypt":
                flag = R.drawable.egypt_flag;
                break;

            case "El Salvador":
                flag = R.drawable.el_salvador_flag;
                break;

            case "England":
                flag = R.drawable.england_flag;
                break;

            case "E. Guinea":
                flag = R.drawable.equatorial_guinea_flag;
                break;

            case "Eritrea":
                flag = R.drawable.eritrea_flag;
                break;

            case "Estonia":
                flag = R.drawable.estonia_flag;
                break;

            case "Ethiopia":
                flag = R.drawable.ethiopia_flag;
                break;

            case "Fiji":
                flag = R.drawable.fiji_flag;
                break;

            case "Finland":
                flag = R.drawable.finland_flag;
                break;

            case "France":
                flag = R.drawable.france_flag;
                break;

            case "Gabon":
                flag = R.drawable.gabon_flag;
                break;

            case "Georgia":
                flag = R.drawable.georgia_flag;
                break;

            case "Germany":
                flag = R.drawable.germany_flag;
                break;

            case "Ghana":
                flag = R.drawable.ghana_flag;
                break;

            case "G. Britain":
                flag = R.drawable.great_britain_flag;
                break;

            case "Greece":
                flag = R.drawable.greece_flag;
                break;

            case "Grenada":
                flag = R.drawable.grenada_flag;
                break;

            case "Guam":
                flag = R.drawable.guam_flag;
                break;

            case "Guatemala":
                flag = R.drawable.guatemala_flag;
                break;

            case "G. Bissau":
                flag = R.drawable.guinea_bissau_flag;
                break;

            case "Guinea":
                flag = R.drawable.guinea_flag;
                break;

            case "Guyana":
                flag = R.drawable.guyana_flag;
                break;

            case "Haiti":
                flag = R.drawable.haiti_flag;
                break;

            case "Honduras":
                flag = R.drawable.honduras_flag;
                break;

            case "Hong Kong":
                flag = R.drawable.hong_kong_flag;
                break;

            case "Hungary":
                flag = R.drawable.hungary_flag;
                break;

            case "Iceland":
                flag = R.drawable.iceland_flag;
                break;

            case "India":
                flag = R.drawable.india_flag;
                break;

            case "Indonesia":
                flag = R.drawable.indonesia_flag;
                break;

            case "Iran":
                flag = R.drawable.iran_flag;
                break;

            case "Iraq":
                flag = R.drawable.iraq_flag;
                break;

            case "Ireland":
                flag = R.drawable.ireland_flag;
                break;

            case "Israel":
                flag = R.drawable.israel_flag;
                break;

            case "Italy":
                flag = R.drawable.italy_flag;
                break;

            case "Ivory Coast":
                flag = R.drawable.ivory_coast_flag;
                break;

            case "Jamaica":
                flag = R.drawable.jamaica_flag;
                break;

            case "Japan":
                flag = R.drawable.japan_flag;
                break;

            case "Jordan":
                flag = R.drawable.jordan_flag;
                break;

            case "Kazakhstan":
                flag = R.drawable.kazakhstan_flag;
                break;

            case "Kenya":
                flag = R.drawable.kenya_flag;
                break;

            case "Kiribati":
                flag = R.drawable.kiribati_flag;
                break;

            case "Kosovo":
                flag = R.drawable.kosovo_flag;
                break;

            case "Kuwait":
                flag = R.drawable.kuwait_flag;
                break;

            case "Kyrgyzstan":
                flag = R.drawable.kyrgyzstan_flag;
                break;

            case "Laos":
                flag = R.drawable.laos_flag;
                break;

            case "Latvia":
                flag = R.drawable.latvia_flag;
                break;

            case "Lebanon":
                flag = R.drawable.lebanon_flag;
                break;

            case "Lesotho":
                flag = R.drawable.lesotho_flag;
                break;

            case "Liberia":
                flag = R.drawable.liberia_flag;
                break;

            case "Libya":
                flag = R.drawable.libya_flag;
                break;

            case "Liechtenstein":
                flag = R.drawable.liechtenstein_flag;
                break;

            case "Lithuania":
                flag = R.drawable.lithuania_flag;
                break;

            case "Luxembourg":
                flag = R.drawable.luxembourg_flag;
                break;

            case "Macedonia":
                flag = R.drawable.macedonia_flag;
                break;

            case "Madagascar":
                flag = R.drawable.madagascar_flag;
                break;

            case "Malawi":
                flag = R.drawable.malawi_flag;
                break;

            case "Malaysia":
                flag = R.drawable.malaysia_flag;
                break;

            case "Maldives":
                flag = R.drawable.maldives_flag;
                break;

            case "Mali":
                flag = R.drawable.mali_flag;
                break;

            case "Malta":
                flag = R.drawable.malta_flag;
                break;

            case "M. Islands":
                flag = R.drawable.marshall_islands_flag;
                break;

            case "Mauritania":
                flag = R.drawable.mauritania_flag;
                break;

            case "Mauritius":
                flag = R.drawable.mauritius_flag;
                break;

            case "Mexico":
                flag = R.drawable.mexico_flag;
                break;

            case "Moldova":
                flag = R.drawable.moldova_flag;
                break;

            case "Monaco":
                flag = R.drawable.monaco_flag;
                break;

            case "Mongolia":
                flag = R.drawable.mongolia_flag;
                break;

            case "Montenegro":
                flag = R.drawable.montenegro_flag;
                break;

            case "Morocco":
                flag = R.drawable.morocco_flag;
                break;

            case "Mozambique":
                flag = R.drawable.mozambique_flag;
                break;

            case "Myanmar":
                flag = R.drawable.myanmar_flag;
                break;

            case "Namibia":
                flag = R.drawable.namibia_flag;
                break;

            case "Nauru":
                flag = R.drawable.nauru_flag;
                break;

            case "Nepal":
                flag = R.drawable.nepal_flag;
                break;

            case "Netherlands":
                flag = R.drawable.netherlands_flag;
                break;

            case "New Zealand":
                flag = R.drawable.new_zealand_flag;
                break;

            case "Nicaragua":
                flag = R.drawable.nicaragua_flag;
                break;

            case "Niger":
                flag = R.drawable.niger_flag;
                break;

            case "Nigeria":
                flag = R.drawable.nigeria_flag;
                break;

            case "N. Korea":
                flag = R.drawable.north_korea_flag;
                break;

            case "Norway":
                flag = R.drawable.norway_flag;
                break;

            case "Oman":
                flag = R.drawable.oman_flag;
                break;

            case "Pakistan":
                flag = R.drawable.pakistan_flag;
                break;

            case "Palau":
                flag = R.drawable.palau_flag;
                break;

            case "Palestine":
                flag = R.drawable.palestine_flag;
                break;

            case "Panama":
                flag = R.drawable.panama_flag;
                break;

            case "P.N Guinea":
                flag = R.drawable.papua_new_guinea_flag;
                break;

            case "Paraguay":
                flag = R.drawable.paraguay_flag;
                break;

            case "Peru":
                flag = R.drawable.peru_flag;
                break;

            case "Philippines":
                flag = R.drawable.philippines_flag;
                break;

            case "Poland":
                flag = R.drawable.poland_flag;
                break;

            case "Portugal":
                flag = R.drawable.portugal_flag;
                break;

            case "Puerto Rico":
                flag = R.drawable.puerto_rico_flag;
                break;

            case "Qatar":
                flag = R.drawable.qatar_flag;
                break;

            case "Romania":
                flag = R.drawable.romania_flag;
                break;

            case "Russia":
                flag = R.drawable.russia_flag;
                break;

            case "Rwanda":
                flag = R.drawable.rwanda_flag;
                break;

            case "St. Kitts":
                flag = R.drawable.saint_kitts_and_nevis_flag;
                break;

            case "St. Lucia":
                flag = R.drawable.saint_lucia_flag;
                break;

            case "St. Vincent":
                flag = R.drawable.saint_vincent_and_the_grenadines_flag;
                break;

            case "Samoa":
                flag = R.drawable.samoa_flag;
                break;

            case "San Marino":
                flag = R.drawable.san_marino_flag;
                break;

            case "Sao Tome":
                flag = R.drawable.sao_tome_and_principe_flag;
                break;

            case "Saudi Arabia":
                flag = R.drawable.saudi_arabia_flag;
                break;

            case "Scotland":
                flag = R.drawable.scotland_flag;
                break;

            case "Senegal":
                flag = R.drawable.senegal_flag;
                break;

            case "Serbia":
                flag = R.drawable.serbia_flag;
                break;

            case "Seychelles":
                flag = R.drawable.seychelles_flag;
                break;

            case "S. Leone":
                flag = R.drawable.sierra_leone_flag;
                break;

            case "Singapore":
                flag = R.drawable.singapore_flag;
                break;

            case "Slovakia":
                flag = R.drawable.slovakia_flag;
                break;

            case "Slovenia":
                flag = R.drawable.slovenia_flag;
                break;

            case "Sol. Islands":
                flag = R.drawable.solomon_islands_flag;
                break;

            case "Somalia":
                flag = R.drawable.somalia_flag;
                break;

            case "S. Africa":
                flag = R.drawable.south_africa_flag;
                break;

            case "S. Korea":
                flag = R.drawable.south_korea_flag;
                break;

            case "S. Sudan":
                flag = R.drawable.south_sudan_flag;
                break;

            case "Spain":
                flag = R.drawable.spain;
                break;

            case "Sri Lanka":
                flag = R.drawable.sri_lanka_flag;
                break;

            case "Sudan":
                flag = R.drawable.sudan_flag;
                break;

            case "Suriname":
                flag = R.drawable.suriname_flag;
                break;

            case "Swaziland":
                flag = R.drawable.swaziland_flag;
                break;

            case "Sweden":
                flag = R.drawable.sweden_flag;
                break;

            case "Switzerland":
                flag = R.drawable.switzerland_flag;
                break;

            case "Syria":
                flag = R.drawable.syria_flag;
                break;

            case "Tajikistan":
                flag = R.drawable.tajikistan_flag;
                break;

            case "Tanzania":
                flag = R.drawable.tanzania_flag;
                break;

            case "Thailand":
                flag = R.drawable.thailand_flag;
                break;

            case "Gambia":
                flag = R.drawable.the_gambia_flag;
                break;

            case "T. Leste":
                flag = R.drawable.timor_leste_flag;
                break;

            case "Togo":
                flag = R.drawable.togo_flag;
                break;

            case "Tonga":
                flag = R.drawable.tonga_flag;
                break;

            case "Tri And Tob":
                flag = R.drawable.trinidad_and_tobago_flag;
                break;

            case "Tunisia":
                flag = R.drawable.tunisia_flag;
                break;

            case "Turkey":
                flag = R.drawable.turkey_flag;
                break;

            case "Turkmenistan":
                flag = R.drawable.turkmenistan_flag;
                break;

            case "Tuvalu":
                flag = R.drawable.tuvalu_flag;
                break;

            case "USA":
                flag = R.drawable.usa_flag;
                break;

            case "Uganda":
                flag = R.drawable.uganda_flag;
                break;

            case "Ukraine":
                flag = R.drawable.ukraine_flag;
                break;

            case "UAE":
                flag = R.drawable.united_arab_emirates_flag;
                break;

            case "Uruguay":
                flag = R.drawable.uruguay_flag;
                break;

            case "Uzbekistan":
                flag = R.drawable.uzbekistan_flag;
                break;

            case "Vanuatu":
                flag = R.drawable.vanuatu_flag;
                break;

            case "Venezuela":
                flag = R.drawable.venezuela_flag;
                break;

            case "Vietnam":
                flag = R.drawable.vietnam_flag;
                break;

            case "V. Island":
                flag = R.drawable.virgin_island_flag;
                break;

            case "Yemen":
                flag = R.drawable.yemen_flag;
                break;

            case "Zambia":
                flag = R.drawable.zambia_flag;
                break;

            case "Zimbabwe":
                flag = R.drawable.zimbabwe_flag;
                break;

            default:
                flag = R.drawable.international_flag;
        }
    }
    //[END getCountry Method for Flags]

    //[START Validate Form]
    private boolean validateForm() {
        boolean valid = true;

        String date = date_edit.getText().toString();
        if (TextUtils.isEmpty(date)) {
            date_edit.setError("Required.");
            valid = false;
        } else {
            date_edit.setError(null);
        }

        String time = time_edit.getText().toString();
        if (TextUtils.isEmpty(time)) {
            time_edit.setError("Required.");
            valid = false;
        } else {
            time_edit.setError(null);
        }

        String country = country_edit.getText().toString();
        if (TextUtils.isEmpty(country)) {
            country_edit.setError("Required.");
            valid = false;
        } else {
            country_edit.setError(null);
        }

        String home_club = home_club_edit.getText().toString();
        if (TextUtils.isEmpty(home_club)) {
            home_club_edit.setError("Required.");
            valid = false;
        } else {
            home_club_edit.setError(null);
        }

        String away_club = away_club_edit.getText().toString();
        if (TextUtils.isEmpty(away_club)) {
            away_club_edit.setError("Required.");
            valid = false;
        } else {
            away_club_edit.setError(null);
        }

        String tip = tip_edit.getText().toString();
        if (TextUtils.isEmpty(tip)) {
            tip_edit.setError("Required.");
            valid = false;
        } else {
            tip_edit.setError(null);
        }

        String odds = odds_edit.getText().toString();
        if (TextUtils.isEmpty(odds)) {
            odds_edit.setError("Required.");
            valid = false;
        } else {
            odds_edit.setError(null);
        }

        String progress = progress_edit.getText().toString();
        if (TextUtils.isEmpty(progress)) {
            progress_edit.setError("Required.");
            valid = false;
        } else {
            progress_edit.setError(null);
        }

        return valid;
    }
    //[END Validate Form]

    //[START Select Country]
    private void SelectCountry() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
        mBuilder.setTitle("Country");

        final Spinner mSpinner;
        mSpinner = mView.findViewById(R.id.spinner);

        //set an adapter that holds the value of the list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.country_arrays));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSpinner.setAdapter(adapter);

        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*brain-box (gets value of spinner)*/

                if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Country")) {
                    getCountry(String.valueOf(mSpinner.getSelectedItem()));
                    //Toast.makeText(getActivity(), mSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                    country_edit.setText(mSpinner.getSelectedItem().toString());

                }
            }
        });

        mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setView(mView);
        AlertDialog alertdialog = mBuilder.create();


        alertdialog.show();


    }
    //[END Select Country]


}
