package com.messedup.saurabh.mess2;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends Activity {
    private static int SPLASH_TIME_OUT=2000;
    private ImageView splashImageIcon,FirebaseImg,SecureImg;
    protected Animation fadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        splashImageIcon=(ImageView)findViewById(R.id.imageView9);
        splashImageIcon.setImageResource(R.drawable.logo_splash);

        splashImageIcon.setVisibility(View.VISIBLE);


        //fadeIn= AnimationUtils.loadAnimation(this,R.anim.fade_in);

        FirebaseImg=(ImageView)findViewById(R.id.firebase);
        Picasso.with(getBaseContext()).load(R.drawable.firebase_full_icon).into(FirebaseImg);
        SecureImg=(ImageView)findViewById(R.id.secureImgView);
        Picasso.with(getBaseContext()).load(R.drawable.instamojo_secure_inverted).into(SecureImg);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent=new Intent(SplashActivity.this,IntroActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);

        // checkUpdate();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
                    if(true)
                    {
                        Log.v("E_VALUE", "******UPDATE FLAG********    " + UPDATE_FLAG[0]);

                        showUpdateDialog();
                    }
                    else
                    {
                        Log.v("E_VALUE", "******UPjfjfjfjfDATE FLAG********    " + UPDATE_FLAG[0]);

                        //USEFULL
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent homeIntent=new Intent(SplashActivity.this,IntroActivity.class);
                                startActivity(homeIntent);
                                finish();
                            }
                        },SPLASH_TIME_OUT);

                    }


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



}
