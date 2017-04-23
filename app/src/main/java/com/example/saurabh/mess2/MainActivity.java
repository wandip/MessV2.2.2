package com.example.saurabh.mess2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saurabh.mess2.BackendLogic.Group;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    int year_x,month_x,day_x;
    static final int DIALOG_ID=0;
    private static Button showmess;
    private static TextView showmes;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers,mDatabaseGroups;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String mUserName;
    private int LOGOUT_VAL;
    private HashMap<String,Integer> map=new HashMap<>();
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LOGOUT_VAL=0;
        Intent mainIntent1 = getIntent();
        final int intValue = mainIntent1.getIntExtra("vf", 0);
        Intent mainIntent2 = getIntent();
        final int intValue2 = mainIntent2.getIntExtra("gf", 0);

        mAuth=FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null&&intValue==0&&intValue2==0)
                {
                    Toast.makeText(MainActivity.this,"Jhavla",Toast.LENGTH_LONG).show();
                    //        FirebaseAuth.getInstance().signOut();

                    Intent loginIntent1 = new Intent(MainActivity.this,LoginActivity.class);
                    loginIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent1);
                }
                if(firebaseAuth.getCurrentUser()==null&&intValue==0)
                {
                    Toast.makeText(MainActivity.this,"Jhavla",Toast.LENGTH_LONG).show();
            //        FirebaseAuth.getInstance().signOut();

                    Intent loginIntent1 = new Intent(MainActivity.this,LoginActivity.class);
                    loginIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent1);
                }
                /*if(firebaseAuth.getCurrentUser()==null&&LOGOUT_VAL==1)
                {
                    Toast.makeText(MainActivity.this,"Jhavla2",Toast.LENGTH_LONG).show();
                    //        FirebaseAuth.getInstance().signOut();

                    Intent loginIntent1 = new Intent(MainActivity.this,LoginActivity.class);
                    loginIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent1);
                }
*/
                /*  else {
                  mDatabaseUsers=FirebaseDatabase.getInstance().getReference().child("users");

                     mDatabaseUsers.keepSynced(true);

                    String user_id = mAuth.getCurrentUser().getUid();
                    mDatabaseUsers.child(user_id).child("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mUserName = dataSnapshot.getValue().toString();
                            setTitle("Hi, " + mUserName);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }*/
            }
        };





        setTitle("Messed Up");
        mDatabaseUsers=FirebaseDatabase.getInstance().getReference().child("users");
        mDatabaseGroups=FirebaseDatabase.getInstance().getReference().child("group");

        mDatabaseUsers.keepSynced(true);
        initiatelogic();//Store Offline data only string values and not images
                                            //WORK ON PICCASO TO STORE OFFLINE IMAGES USING FIREBASE
                                            //SEARCH INTERNET

       /* if(mAuth.getCurrentUser().getUid().equals("BDJYZEdkabXUMPQTaVfX5HfHh3M2"))
        {
            initiatelogic();
        }
*/



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1,false);     //PAGE NUMBER START 0,1,2 SET DEFAULT VIEW (QR CODE SCREEN =1)
                                                //WHEN APP STARTS



        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //TESTING FOR MESS SELECTION GUI NOT TO BE USED
                Intent selectIntent=new Intent(MainActivity.this, MessSelectionActivity.class);
                startActivity(selectIntent);
               // startActivity(loginIntent);

                /*Snackbar.make(getWindow().getDecorView().getRootView(), "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                      //  .setAction("Action", null).show();
            }
        });



    }

    private void initiatelogic() {

        mDatabaseGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int sum=0;

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                   int temp=dsp.child("size").getValue(Integer.class);
                    String gidtemp=dsp.getKey();

                    map.put(gidtemp,temp);

                    sum+=temp;
                    Log.v("E_VALUE","SIZE : "+temp);

                }
                Log.v("E_VALUE","Addition : "+sum);
                Group obj=new Group(map);
                Log.v("E_VALUE","MAP SIZE : "+obj.getmapsize());
                obj.assignset();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   //MENU ITEM IS THE THREE DOTS ON RIGHT TOP OF SCREEN
                                                            //CURRENTLY: SETTINGS AND LOGOUT BUTTON
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings_intent=new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(settings_intent);
        }

        if (id == R.id.action_logout) {

            LOGOUT_VAL=1;
            logout();

        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {


        mAuth.signOut();
       // Toast.makeText(MainActivity.this,"After Signout",Toast.LENGTH_LONG).show();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {      //USELESS AUTOGENERATED CODE
                                                                    //IF COMMENTS ARE GREEN THEN THAT CODE IS AUTO GENERATED
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {   //SHOWING UI OF THE SUB PAGE 1 : CALENDER WALA
                                                                //DO ANY CODING IN THIS AREA REGARDING THAT PAGE
            if(getArguments().getInt(ARG_SECTION_NUMBER)==1) {


                View rootView = inflater.inflate(R.layout.fragment_sub_page01,container,false);

                showmess=(Button)rootView.findViewById(R.id.messBtn);
                showmes=(TextView)rootView.findViewById(R.id.showmessTV);

                DatePicker datePicker = (DatePicker)rootView.findViewById(R.id.datePicker2);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                       // Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);

                        if(dayOfMonth%4==1){
                            showmes.setText("Your Mess on " + dayOfMonth + "/" + (month + 1) +" is : Vikas");
                        }
                        else if(dayOfMonth%4==2){
                            showmes.setText("Your Mess on " + dayOfMonth + "/" + (month + 1) +" is : Anand");
                        }
                        else if(dayOfMonth%4==3){
                            showmes.setText("Your Mess on " + dayOfMonth + "/" + (month + 1) +" is : Ashirwad");
                        }
                        if(dayOfMonth%4==0){
                            showmes.setText("Your Mess on " + dayOfMonth + "/" + (month + 1) +" is : Gujrati");
                        }

                       // Snackbar.make(getView(),"Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth , Snackbar.LENGTH_LONG)
                       //  .setAction("Action", null).show();

                    }
                });

             /*   showmess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity().getApplicationContext(),"Clicked!",Toast.LENGTH_LONG).show();
                        Intent settings_intent=new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                        startActivity(settings_intent);
                    }
                });
*/
                //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
               // textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                return rootView;
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==2) {
                                                    //CENTRE SCREEN VIEW CONTAINING QR CODE DO ANY CODING OF THAT
                                                    // IN THIS AREA ( TODO: ADD QR CODE OFFLINE CAPABILITY )
                //                                                  TODO: ADD PAYEMENT BUTTON IF NOT PAYED
                //                                                  TODO: OVER DEFAULT QR CODE PLACE BUTTON

                View rootView = inflater.inflate(R.layout.fragment_sub_page02,container,false);
                //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                // textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                return rootView;
            }
            else{


                View rootView = inflater.inflate(R.layout.fragment_sub_page03,container,false);

                //PROFILE SCREEN VIEW SAME AS ABOVE

               // TextView textView = (TextView) rootView.findViewById(R.id.section_label);
               // textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                return rootView;
            }
        }
    }

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
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "MONTHLY";
                case 1:
                    return "TODAY";
                case 2:
                    return "PROFILE";
            }
            return null;
        }
    }

    @Override
    protected void onStart() {

        mAuth.addAuthStateListener(mAuthListener);
        super.onStart();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
