package com.example.saurabh.mess2;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import java.util.HashMap;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saurabh.mess2.BackendLogic.AssignMessLogic;
import com.example.saurabh.mess2.BackendLogic.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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
import com.kobakei.ratethisapp.RateThisApp;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity{


    public static String QRCODE,USERNAME;
    public static  boolean PAYEMENT_DONE,connected;
    static int count=0;

    private static Button showmess;
    private static TextView showmes,UserNameTxt;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers,mDatabaseGroups,mDatabaseInCurUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static Users UserDataObj;
    public static Group obj;
    private static String NameTemp;


    String resumeflag;

    private int noofdaysleft;
    private int LOGOUT_VAL;
    private HashMap<String,Integer> map=new HashMap<>();
    private static Button mAddGrpBtn;

    private static TextView UserScannedLunchTxtView,UserGroupIdTxtView,UserScannedDinnerTxtView,TodaysMessTxtView;
    private static View rootView3,rootView2;
    private static ImageView UserQRCodeImgView,BackGroundImg;
    private static TextView memberTxtView[]=new TextView[3];

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
        this.setFinishOnTouchOutside(true);
        LOGOUT_VAL=0;
        Intent mainIntent1 = getIntent();
        final int intValue = mainIntent1.getIntExtra("vf", 0);
        Intent mainIntent2 = getIntent();
        final int intValue2 = mainIntent2.getIntExtra("gf", 0);


            if(!isConnected())
            {
                 Toast.makeText(MainActivity.this,"Please Connect to the Internet to Update your Today's Mess",Toast.LENGTH_LONG).show();
                Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {1000, 75,100,75};

                // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
                v.vibrate(pattern, -1);
            }

            checkUpdate();



        memberTxtView[0] = (TextView)findViewById(R.id.member1TxtView);
        memberTxtView[1] = (TextView)findViewById(R.id.member2TxtView);



        Log.v("E_VALUE","CODE RUN 1");

        memberTxtView[2] = (TextView)findViewById(R.id.member3TxtView);




        DatabaseReference checkResumeFlag=FirebaseDatabase.getInstance().getReference().child("admin");

        checkResumeFlag.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                resumeflag=dataSnapshot.child("resumeflag").getValue().toString();
                if(resumeflag.equals("notworking"))
                {
                    Intent undercontructIntent=new Intent(MainActivity.this,UnderConstructionActivity.class);
                    undercontructIntent.putExtra("updatefound",false);
                    undercontructIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(undercontructIntent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        mAuth=FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null&&intValue==0&&intValue2==0)
                {
                   // Toast.makeText(MainActivity.this,"Jhavla",Toast.LENGTH_LONG).show();
                    //        FirebaseAuth.getInstance().signOut();

                    Intent loginIntent1 = new Intent(MainActivity.this,LoginActivity.class);
                    loginIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent1);
                }
                else
                {
                    checkUpdate();
                }
                if(firebaseAuth.getCurrentUser()==null&&intValue==0)
                {
                    //Toast.makeText(MainActivity.this,"Jhavla",Toast.LENGTH_LONG).show();
            //        FirebaseAuth.getInstance().signOut();

                    Intent loginIntent1 = new Intent(MainActivity.this,LoginActivity.class);
                    loginIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent1);
                }
                else
                {
                    checkUpdate();
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
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

/*

        // Monitor launch times and interval from installation
        RateThisApp.onCreate(getBaseContext());
        // If the condition is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(getBaseContext());

        // Custom condition: 3 days and 10 launches
        RateThisApp.Config config = new RateThisApp.Config(0, 2);
        RateThisApp.init(config);
        config.setTitle(R.string.my_own_title);
        config.setMessage(R.string.my_own_message);
        config.setYesButtonText(R.string.my_own_rate);
        config.setNoButtonText(R.string.my_own_thanks);
        config.setCancelButtonText(R.string.my_own_cancel);
        RateThisApp.init(config);
*/

    }


    private void checkUpdate() {


        final boolean[] UPDATE_FLAG = {true};

        DatabaseReference mUpdateRef = FirebaseDatabase.getInstance().getReference().child("admin");

        mUpdateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String VersionCode = dataSnapshot.child("versioncode").getValue().toString();
                int IntVersionCode = Integer.parseInt(VersionCode);

                try {
                    PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    int versionNumber = pinfo.versionCode;
                    Log.v("E_VALUE", "******VERSION NUMBER FROM PACKAGE********    " + versionNumber);
                    Log.v("E_VALUE", "******VERSION CODE FROM DATABASE********    " + IntVersionCode);

                    UPDATE_FLAG[0] = IntVersionCode > versionNumber;
                    Log.v("E_VALUE", "******UPDATE FLAG********    " + UPDATE_FLAG[0]);
                    if(UPDATE_FLAG[0])
                    {
                        Log.v("E_VALUE", "******UPDATE FLAG********    " + UPDATE_FLAG[0]);
                        Intent backIntent=new Intent(MainActivity.this,UnderConstructionActivity.class);
                        backIntent.putExtra("updatefound",true);
                        startActivity(backIntent);
                        finish();
                       // showUpdateDialog();
                    }
                   /* else
                    {
                        *//*Log.v("E_VALUE", "******UPjfjfjfjfDATE FLAG********    " + UPDATE_FLAG[0]);

                        //USEFULL
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() { *//*

                      *//*      }
                        },SPLASH_TIME_OUT);*//*

                    }

*/
                } catch (Exception e) {
                    Log.v("E_VALUE", "PACKAGE EXCEPTION CAUGHT!");

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }

    private void showUpdateDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("UPDATE REQUIRED") //
                .setMessage("Click to Update the App") //
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(20);
                        Uri uri = Uri.parse("http://www.google.com");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);

                    }
                }); //
        this.setFinishOnTouchOutside(false);
        builder.setCancelable(false);
        builder.show();

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
                    Subpage3obj.setDetails(UserDataObj.getCollege(),UserDataObj.getContact(),
                            UserDataObj.getEmail(),UserDataObj.getEndsub(),UserDataObj.getGroupid(),UserDataObj.getName(),UserDataObj.getQrcode(),UserDataObj.getScanneddinner(),UserDataObj.getScannedlunch());
                NameTemp=UserDataObj.getName();
               /* UserNameTxtView=(TextView)findViewById(R.id.userName001);
                //changeFragmentTextView(NameTemp);

                UserNameTxtView.setText(UserDataObj.getName());
                */


                    Log.v("E_VALUE","College : "+UserDataObj.getCollege());
                    Log.v("E_VALUE","Contact : "+UserDataObj.getContact());
                    Log.v("E_VALUE","Email : "+UserDataObj.getEmail());
                    Log.v("E_VALUE","GroupID : "+UserDataObj.getGroupid());
                    Log.v("E_VALUE","NAME : "+UserDataObj.getName());
                    Log.v("E_VALUE","QRCODE : "+UserDataObj.getQrcode());
                    setTitle("Hi, "+UserDataObj.getName());
                     setUserDetails2(UserDataObj.getCollege(),UserDataObj.getContact(),
                             UserDataObj.getEmail(),UserDataObj.getEndsub(),UserDataObj.getGroupid(),UserDataObj.getName(),UserDataObj.getQrcode(),UserDataObj.getScanneddinner(),UserDataObj.getScannedlunch());

                if(UserDataObj.getContact().equals("nocontact"))
                {


                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);


                    final EditText input = new EditText(getBaseContext());
                    builder.setView(input);
                    builder.setTitle("CONTACT DETAILS") //
                            .setMessage("Enter your Mobile Number") //
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                                    v.vibrate(20);
                                    String ContactNumber=input.getText().toString();

                                    FirebaseDatabase.getInstance().getReference().child("users")
                                            .child(mAuth.getCurrentUser().getUid()).child("contact").setValue(ContactNumber);
                                    dialog.dismiss();
                                }
                            }); //
                    /*.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Vibrator v = (Vibrator) SubPage2Context.getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(20);
                            // TODO
                           // dialog.dismiss();
                        }
                    });*/
                    builder.show();



                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

      //  mDatabaseUsers.child(uid).removeEventListener(userDetailsListener);




        if(uid.equals("4ALe90P970eTtifEAEfHlzwe03u1")) //checks if admin
        {
            Toast.makeText(MainActivity.this,"Hello, Sir! Welcome to MessedUp! Gyang",Toast.LENGTH_LONG).show();
           // initiatelogic();
        }
        /*else
        {
            Button mPowerFulButton1=(Button)rootView3.findViewById(R.id.SuperPowerfulButton);
            mPowerFulButton1.setEnabled(false);
            mPowerFulButton1.setVisibility(View.GONE);
            mPowerFulButton1.setClickable(false);

        }
*/




    }

    private void setUserDetails2(String college, String contact, String email, String endsub, String groupid, String name, final String qrcode,String scanneddinner,String scannedlunch)
    {
       /* UserNameTxtView=(TextView)findViewById(R.id.userName001);
        UserCollegeTxtView=(TextView)findViewById(R.id.userCollege);
        UserEmailTxtView=(TextView)findViewById(R.id.userEmail);*/
        UserGroupIdTxtView=(TextView)findViewById(R.id.userGroupID);
        UserScannedLunchTxtView=(TextView)findViewById(R.id.scannedlunchTxtView);
        UserScannedDinnerTxtView=(TextView)findViewById(R.id.scanneddinnerTxtView);
        TodaysMessTxtView=(TextView)findViewById(R.id.TodaysMessTxtView);


        setTodaysMessTxtView();

        setMemberTxtView();

       /* DatabaseReference mInCurrentUserGroupsTodaysMess=FirebaseDatabase.getInstance().getReference().child("group").child(groupid).child("todaysmess");

        mInCurrentUserGroupsTodaysMess.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TodaysMessTxtView.setText("Today's Mess is : "+dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/


        QRCODE=qrcode;
        USERNAME=name;
        DatabaseReference mAdminDatabase=FirebaseDatabase.getInstance().getReference().child("admin").child("daysleft");

        mAdminDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String daysleft=dataSnapshot.getValue().toString();


                //Toast.makeText(MainActivity.this,"No. of Days left : "+daysleft,Toast.LENGTH_LONG).show();
                noofdaysleft=Integer.parseInt(daysleft);

                if(qrcode.equals("default"))
                {

                    UserQRCodeImgView = (ImageView) findViewById(R.id.QRCodeImageView);
                    BackGroundImg=(ImageView)findViewById(R.id.backgroundImageView);
                    UserQRCodeImgView.setImageResource(R.drawable.before_payement_qrcode);
                    //  BackGroundImg.setImageResource(R.drawable.blur_background_3);
                    PAYEMENT_DONE=false;

                    //UserQRCodeImgView.setOnClickListener(new ExternalOnClickListener());
                }
                else if(qrcode.equals("paid"))
                {
                    if(noofdaysleft>0) {
                        UserQRCodeImgView = (ImageView) findViewById(R.id.QRCodeImageView);
                        UserQRCodeImgView.setImageResource(R.drawable.after_payment_qrcode);
                        BackGroundImg = (ImageView) findViewById(R.id.backgroundImageView);
                        // BackGroundImg.setImageResource(R.drawable.blur_background_3);
                        PAYEMENT_DONE = true;
                    }
                    else
                    {
                        UserQRCodeImgView = (ImageView) findViewById(R.id.QRCodeImageView);
                        BackGroundImg=(ImageView)findViewById(R.id.backgroundImageView);
                        UserQRCodeImgView.setImageResource(R.drawable.after_payment_qrcode);
                        //  BackGroundImg.setImageResource(R.drawable.blur_background_3);

                        PAYEMENT_DONE=false;
                    }
                }
                else {

                    if (noofdaysleft > 0) {
                        PAYEMENT_DONE = true;
                        UserQRCodeImgView = (ImageView) findViewById(R.id.QRCodeImageView);
                        BackGroundImg = (ImageView) findViewById(R.id.backgroundImageView);
                        // BackGroundImg.setImageResource(R.drawable.optimized_blurbackground2);
                        try {
                            Picasso.with(getBaseContext()).load(UserDataObj.getQrcode()).networkPolicy(NetworkPolicy.OFFLINE).into(UserQRCodeImgView, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {

                                    Picasso.with(getBaseContext()).load(qrcode).into(UserQRCodeImgView);

                                }
                            });
                        } catch (Exception e) {
                            Log.v("E_VALUE", e.getMessage());
                        }
                    }
                    else
                    {
                        UserQRCodeImgView = (ImageView) findViewById(R.id.QRCodeImageView);
                        BackGroundImg=(ImageView)findViewById(R.id.backgroundImageView);
                        UserQRCodeImgView.setImageResource(R.drawable.qrcode);
                        //  BackGroundImg.setImageResource(R.drawable.blur_background_3);
                        PAYEMENT_DONE=false;
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


   /*     UserNameTxtView.setText(name);
        UserCollegeTxtView.setText(college);
        UserEmailTxtView.setText(email);*/


        //UserContactTxtView.setText(contact);
try {

    if (groupid.equals("not paid")) {
        UserGroupIdTxtView = (TextView) findViewById(R.id.userGroupID);
        UserGroupIdTxtView.setText("Pay and Add UPTO 4 FRIENDS in your group & enjoy the app Together!");
    } else {
        UserGroupIdTxtView = (TextView) findViewById(R.id.userGroupID);
        UserGroupIdTxtView.setText("Your GROUP CODE : " + groupid/*.substring(0,5)*/);
    }


    if (!scannedlunch.equals("-1")) {
        UserScannedLunchTxtView = (TextView) findViewById(R.id.scannedlunchTxtView);
        UserScannedLunchTxtView.setText("You had Lunch at :" + scannedlunch);
    }
    if (!scanneddinner.equals("-1")) {
        UserScannedDinnerTxtView = (TextView) findViewById(R.id.scanneddinnerTxtView);
        UserScannedDinnerTxtView.setText("You had Dinner at :" + scanneddinner);
    }

}
catch(Exception e)
{
    Toast.makeText(this,"Exception Caught",Toast.LENGTH_LONG).show();
}



    }


    private void setMemberTxtView() {




try {


        Log.v("E_VALUE","CODE RUN 2");


   // if(memberTxtView[0]!=null)
    memberTxtView[0] = (TextView)findViewById(R.id.member1TxtView);

    memberTxtView[0].setText("");
   // if(memberTxtView[1]!=null)
    memberTxtView[1] = (TextView)findViewById(R.id.member2TxtView);

    memberTxtView[1].setText("");
  //  if(memberTxtView[2]!=null)
    memberTxtView[2] = (TextView)findViewById(R.id.member3TxtView);

    memberTxtView[2].setText("");

}
catch (Exception e)
{
    Log.v("E_VALUE","CODE RUN 3");

}

        DatabaseReference mInCurGrpMemberId=FirebaseDatabase.getInstance().getReference().child("group").child(UserDataObj.getGroupid())
                .child("memberid");



        mInCurGrpMemberId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final int[] i = {0};
                for(DataSnapshot dsp : dataSnapshot.getChildren())
                {


                    FirebaseDatabase.getInstance().getReference().child("users").child(dsp.getKey())
                            .child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            try {
                                if (!dataSnapshot.getValue().toString().equals(UserDataObj.getName())) {
                                    memberTxtView[2] = (TextView) findViewById(R.id.member3TxtView);
                                    memberTxtView[1] = (TextView) findViewById(R.id.member2TxtView);
                                    memberTxtView[0] = (TextView) findViewById(R.id.member1TxtView);
                                    memberTxtView[i[0]].setText((i[0] + 1) + ". " + dataSnapshot.getValue().toString());
                                    i[0]++;
                                }
                            }
                            catch(Exception e)
                            {
                                Toast.makeText(MainActivity.this,"Exception caught 2",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setTodaysMessTxtView() {





            DatabaseReference mInMess = FirebaseDatabase.getInstance().getReference().child("mess");

            mInMess.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean MESS_FOUND = false;
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                        for (DataSnapshot dsp2 : dsp.child("grouplist").getChildren()) {
                            if (UserDataObj.getGroupid().equals(dsp2.getKey())) {
                                TodaysMessTxtView=(TextView)findViewById(R.id.TodaysMessTxtView);
                                TodaysMessTxtView.setText("Today's Mess is : " + dsp.child("messname").getValue().toString());
                                MESS_FOUND = true;
                                break;
                            }

                        }

                        if (MESS_FOUND) {
                            break;
                        }

                    }
                    if (!MESS_FOUND) {
                        TodaysMessTxtView=(TextView)findViewById(R.id.TodaysMessTxtView);
                        TodaysMessTxtView.setText("Today's Mess : ................");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });








    }

    /* private void setUserDetails()
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

 */
    public void initiatelogic() {

        DatabaseReference mDatabaseGroups=FirebaseDatabase.getInstance().getReference().child("group");

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

    public void MonthChangeLogic()
    {
        DatabaseReference mGroupRef=FirebaseDatabase.getInstance().getReference().child("group");
        mGroupRef.setValue(null);
        DatabaseReference mMonthChange=FirebaseDatabase.getInstance().getReference().child("users");

        mMonthChange.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dsp : dataSnapshot.getChildren())
                {

                    dsp.child("groupid").getRef().setValue("not paid");
                    dsp.child("qrcode").getRef().setValue("default");

                }
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

        if(id==R.id.action_about_us)
        {
            Intent aboutus_intent=new Intent(getApplicationContext(),AboutUsActivity.class);
            startActivity(aboutus_intent);
        }
        if(id==R.id.share)
        {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Messed Up!");
                String sAux = "\nHey!\nCheckout and Download Messed Up! on Google Play. Join and enjoy " +
                        "different mess everyday!\n\n";
                sAux = sAux + "https://www.google.co.in \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Share to"));
            } catch(Exception e) {
                //e.toString();
            }
        }
        if(id==R.id.rateus)
        {
         //   Uri uri = Uri.parse("market://details?id=" + getBaseContext().getPackageName());
              Uri uri = Uri.parse("http://www.google.co.in");

            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.google.co.in")));

                        // Uri.parse("http://play.google.com/store/apps/details?id=" + getBaseContext().getPackageName())));
            }
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
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected=true;
            return true;
        }
        else
            connected = false;
        return false;
    }


    /*public void changeFragmentTextView(String s) {
        android.app.Fragment frag = getFragmentManager().findFragmentById(R.layout.fragment_sub_page03);
        ((TextView)rootView3.findViewById(R.id.userName001)).setText(s);
    }
*/

}
