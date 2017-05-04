package com.example.saurabh.mess2;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saurabh.mess2.BackendLogic.AssignMessLogic;
import com.example.saurabh.mess2.BackendLogic.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {


    public static String QRCODE;
    public static  boolean PAYEMENT_DONE;
    static int count=0;

    private static Button showmess;
    private static TextView showmes,UserNameTxt;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers,mDatabaseGroups,mDatabaseInCurUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static Users UserDataObj;
    public static Group obj;
    private static String NameTemp;
    private int LOGOUT_VAL;
    private HashMap<String,Integer> map=new HashMap<>();
    private static Button mAddGrpBtn;

    private static TextView UserNameTxtView,UserEmailTxtView,UserContactTxtView,UserGroupIdTxtView,UserCollegeTxtView;
    private static View rootView3,rootView2;
    private static ImageView UserQRCodeImgView;
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


            }
        };



        mDatabaseUsers=FirebaseDatabase.getInstance().getReference().child("users"); //Database USER Node Ref
        mDatabaseGroups=FirebaseDatabase.getInstance().getReference().child("group");//Database GROUP Node Ref

        mDatabaseUsers.keepSynced(true);    //Offline Capabilities of Firebase

        if(mAuth.getCurrentUser()!=null) //Get User Data and Store in UserDataObj Object
        {

            getUserdata();


        }

       /* mDatabaseInCurUser=mDatabaseUsers.child(UserDataObj.getuid());
        mDatabaseInCurUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(mAuth.getCurrentUser()!=null)
                {
                    getUserdata();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(mAuth.getCurrentUser()!=null)
                {
                    getUserdata();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(mAuth.getCurrentUser()!=null)
                {
                    getUserdata();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                if(mAuth.getCurrentUser()!=null)
                {
                    getUserdata();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
                                        //SEARCH INTERNET



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0,false);     //PAGE NUMBER START 0,1,2 SET DEFAULT VIEW (QR CODE SCREEN =1)
                                                //WHEN APP STARTS



        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {    //TESTING FOR MESS SELECTION GUI NOT TO BE USED
                /*Intent selectIntent=new Intent(MainActivity.this, MessSelectionActivity.class);
                startActivity(selectIntent);*/
                Log.v("E_VALUE","clicked fab");
                setQRCodeImg();

               // startActivity(loginIntent);

                /*Snackbar.make(getWindow().getDecorView().getRootView(), "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                      //  .setAction("Action", null).show();
            }
        });



    }

    private void setQRCodeImg() {

        if(UserDataObj.getQrcode().equals("default"))
        {
            //generate qr code
            Log.v("E_VALUE","In setqrcodeimg");
            generateQRCodeMethod();


        }


    }

    private void generateQRCodeMethod() {

        Log.v("E_VALUE","In generateqrcodemethod");


        MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
        try{
            BitMatrix bitMatrix=multiFormatWriter.encode(UserDataObj.getuid(), BarcodeFormat.QR_CODE,270,270);
            BarcodeEncoder barcodeEncoder =new BarcodeEncoder();
            Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
           // SubPage02 msubPage02=new SubPage02(bitmap);


        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }

    }

    private void getUserdata() {



        final String uid=mAuth.getCurrentUser().getUid();

        ValueEventListener userDetailsListener;

        mDatabaseUsers.child(uid).addValueEventListener(userDetailsListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    UserDataObj=dataSnapshot.getValue(Users.class);
                     UserDataObj.setuid(uid);
                    Log.v("E_VALUE",UserDataObj.getuid());

                    SubPage03 Subpage3obj=new SubPage03();
                    Subpage3obj.setDetails(UserDataObj.getAge(),UserDataObj.getCollege(),UserDataObj.getContact(),
                            UserDataObj.getEmail(),UserDataObj.getGroupid(),UserDataObj.getName(),UserDataObj.getQrcode());
                NameTemp=UserDataObj.getName();
               /* UserNameTxtView=(TextView)findViewById(R.id.userName001);
                //changeFragmentTextView(NameTemp);

                UserNameTxtView.setText(UserDataObj.getName());
                */

                    Log.v("E_VALUE","Age : "+UserDataObj.getAge());
                    Log.v("E_VALUE","College : "+UserDataObj.getCollege());
                    Log.v("E_VALUE","Contact : "+UserDataObj.getContact());
                    Log.v("E_VALUE","Email : "+UserDataObj.getEmail());
                    Log.v("E_VALUE","GroupID : "+UserDataObj.getGroupid());
                    Log.v("E_VALUE","NAME : "+UserDataObj.getName());
                    Log.v("E_VALUE","QRCODE : "+UserDataObj.getQrcode());
                    setTitle("Hi, "+UserDataObj.getName());
                     setUserDetails2(UserDataObj.getAge(),UserDataObj.getCollege(),UserDataObj.getContact(),
                             UserDataObj.getEmail(),UserDataObj.getGroupid(),UserDataObj.getName(),UserDataObj.getQrcode());





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

      //  mDatabaseUsers.child(uid).removeEventListener(userDetailsListener);


        if(uid.equals("xZLuBRV5cCcjxuCb3eXyJNkcESB3")) //checks if admin
        {
            initiatelogic();
        }





    }

    private void setUserDetails2(String age, String college, String contact, String email, String groupid, String name, final String qrcode)
    {
        UserNameTxtView=(TextView)findViewById(R.id.userName001);
        UserCollegeTxtView=(TextView)findViewById(R.id.userCollege);
        UserEmailTxtView=(TextView)findViewById(R.id.userEmail);
        UserGroupIdTxtView=(TextView)findViewById(R.id.userGroupID);
        UserContactTxtView=(TextView)findViewById(R.id.userContact);
         QRCODE=qrcode;

        if(qrcode.equals("default"))
        {

            UserQRCodeImgView = (ImageView) findViewById(R.id.QRCodeImageView);
           UserQRCodeImgView.setImageResource(R.drawable.qrcode);
           PAYEMENT_DONE=false;

            //UserQRCodeImgView.setOnClickListener(new ExternalOnClickListener());
        }
        else {
            PAYEMENT_DONE=true;
            UserQRCodeImgView = (ImageView) findViewById(R.id.QRCodeImageView);


            Picasso.with(getBaseContext()).load(qrcode).networkPolicy(NetworkPolicy.OFFLINE).into(UserQRCodeImgView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(getBaseContext()).load(qrcode).into(UserQRCodeImgView);

                }
            });
        }

        UserNameTxtView.setText(name);
        UserCollegeTxtView.setText(college);
        UserEmailTxtView.setText(email);
        UserGroupIdTxtView.setText(groupid);
        UserContactTxtView.setText(contact);

    }

    private void setUserDetails()
    {
        UserNameTxtView=(TextView)findViewById(R.id.userName001);
        UserCollegeTxtView=(TextView)findViewById(R.id.userCollege);
        UserEmailTxtView=(TextView)findViewById(R.id.userEmail);
        UserGroupIdTxtView=(TextView)findViewById(R.id.userGroupID);
        UserContactTxtView=(TextView)findViewById(R.id.userContact);


        UserNameTxtView.setText(UserDataObj.getName());
        UserCollegeTxtView.setText(UserDataObj.getCollege());
        UserEmailTxtView.setText(UserDataObj.getEmail());
        UserGroupIdTxtView.setText(UserDataObj.getGroupid());
        UserContactTxtView.setText(UserDataObj.getContact());

    }


    private void initiatelogic() {

        mDatabaseGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int sum=0;

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                  // int temp=dsp.child("size").getValue(Integer.class);
                    int temp= ((int) dsp.child("memberid").getChildrenCount());
                    String gidtemp=dsp.getKey();
                    if(temp>0) {
                        map.put(gidtemp, temp);

                        sum += temp;
                        Log.v("E_VALUE", "SIZE : " + temp);
                    }
                }
                Log.v("E_VALUE","Addition : "+sum);
                obj=new Group(map);
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
                                 Bundle savedInstanceState) {
                                                    //SHOWING UI OF THE SUB PAGE 1 : CALENDER WALA
                                                                //DO ANY CODING IN THIS AREA REGARDING THAT PAGE
            if(getArguments().getInt(ARG_SECTION_NUMBER)==0) {




                View rootView = inflater.inflate(R.layout.fragment_sub_page01,container,false);

              /*  showmess=(Button)rootView.findViewById(R.id.messBtn);
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

*/
                return rootView;
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==1) {
                                                   //CENTRE SCREEN VIEW CONTAINING QR CODE DO ANY CODING OF THAT
                                                    // IN THIS AREA ( TODO: ADD QR CODE OFFLINE CAPABILITY )
                //                                                  TODO: ADD PAYEMENT BUTTON IF NOT PAYED
                //                                                  TODO: OVER DEFAULT QR CODE PLACE BUTTON
                SubPage02 subpage02Obj=new SubPage02();

                //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                // textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                subpage02Obj.passContext(getActivity());

                rootView2=subpage02Obj.onCreateView(inflater,container,savedInstanceState);
                return rootView2;
            }
            else
            {


                //PROFILE SCREEN VIEW SAME AS ABOVE
                SubPage03 subpage03Obj=new SubPage03();
                Log.v("E_VALUE","In rootview3 "+NameTemp);

                subpage03Obj.passContext(getActivity());
                rootView3=subpage03Obj.onCreateView(inflater,container,savedInstanceState);
                count++;
                return rootView3;
            }

        }




       /* @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            TextView userNameTxtView2 = (TextView) rootView3.findViewById(R.id.userName);
            userNameTxtView2.setText(NameTemp);
        }*/
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
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                /*case 0:
                    return "MONTHLY";*/
                case 0:
                    return "TODAY";
                case 1:
                    return "PROFILE";
            }
            return null;
        }
    }

    @Override
    protected void onStart() {

        mAuth.addAuthStateListener(mAuthListener);
        super.onStart();
        /*if(mAuth.getCurrentUser()!=null) //Get User Data and Store in UserDataObj Object
        {

            getUserdata();


        }
*/
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void changeFragmentTextView(String s) {
        android.app.Fragment frag = getFragmentManager().findFragmentById(R.layout.fragment_sub_page03);
        ((TextView)rootView3.findViewById(R.id.userName001)).setText(s);
    }


}
