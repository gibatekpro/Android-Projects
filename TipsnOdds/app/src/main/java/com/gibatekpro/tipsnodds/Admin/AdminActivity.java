package com.gibatekpro.tipsnodds.Admin;

import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gibatekpro.tipsnodds.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;
import dmax.dialog.SpotsDialog;

public class AdminActivity extends AppCompatActivity {

    //LOG TAG Declaration
    private static final String TAG = "FireStore";

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private Fragment[] fragments = new Fragment[]{new frag_pending(), new frag_played()};

    //Spots dialog declaration
    SpotsDialog spotsDialog;

    //Cloud Firestore initialization
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //Spots Dialog Instance
        spotsDialog = new SpotsDialog(AdminActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                load_pending();

                load_played();

            }
        });


    }

    //[START Load Data For Played Fragment]
    public void load_played() {

        //[START Show Spots Dialog]
        spotsDialog.show();
        //[END Show Spots Dialog]

        //[START Load Data From Fire_store And Send Response To loadData(task)]
        db.collection("Played")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
                        for (Fragment fragment : fragmentList) {

                            try {
                                if (fragment.isVisible()) {
                                    spotsDialog.dismiss();
                                    ((frag_played) fragment).loadData(task);

                                    break;
                                }
                            } catch (Exception e) {
                            }
                        }

                        //[START Snack Bar Data Loaded Successfully]
                        //Snackbar.make(findViewById(R.id.main_content), "Data Loaded Successfully", Snackbar.LENGTH_LONG)
                        //        .setAction("Action", null).show();
                        //[END Snack Bar Data Loaded Successfully]

                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //[START Snack Bar Failed To Load Data]
                //Snackbar.make(findViewById(R.id.main_content), "Failed To Load Data", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                //[END Snack Bar Failed To Load Data]

            }
        });
        //[END Load Data From Fire_store And Send Response To loadData(task)]

    }
    //[END Load Data For Played Fragment]

    //[START Load Data For Today Fragment]
    public void load_pending() {

        //[START Show Spots Dialog]
        spotsDialog.show();
        //[END Show Spots Dialog]

        //[START Load Data From Fire_store And Send Response To loadData(task)]
        db.collection("Today")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
                        for (Fragment fragment : fragmentList) {
                            try {
                                if (fragment.isVisible()) {

                                    spotsDialog.dismiss();
                                    ((frag_pending) fragment).loadData(task);

                                    break;
                                }
                            } catch (Exception e) {
                            }
                        }

                        //[START Snack Bar Data Loaded Successfully]
                        Snackbar.make(findViewById(R.id.main_content), "Data Loaded Successfully", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        //[END Snack Bar Data Loaded Successfully]

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //[START Snack Bar Failed To Load Data]
                Snackbar.make(findViewById(R.id.main_content), "Failed To Load Data", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //[END Snack Bar Failed To Load Data]

            }
        });
        //[END Load Data From Fire_store And Send Response To loadData(task)]

    }
    //[END Load Data For Today Fragment]

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if ( id == android.R.id.home ) {

            NavUtils.navigateUpFromSameTask(this);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new frag_pending();
                case 1:
                    return new frag_played();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }

}
