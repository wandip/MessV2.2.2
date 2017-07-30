package com.messedup.saurabh.mess2;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.messedup.saurabh.mess2.R.color.base_dark;

public class AboutUsActivity extends AppCompatActivity {

    private ImageView app_icon,saurabh_img,dipak_img,yash_img,about_us;
    private TextView versionText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about_us);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(base_dark));

        }


       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        setTitle("About Us");*/
        versionText=(TextView)findViewById(R.id.versionTextView);
        about_us=(ImageView)findViewById(R.id.imageView11);
        Picasso.with(getBaseContext()).load(R.drawable.aboutus2).into(about_us);

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;

        versionText.setText("version: "+pInfo.versionName);



/*

        app_icon=(ImageView)findViewById(R.id.imageView10);
        app_icon.setImageResource(R.drawable.app_icon_1);

        app_icon.setVisibility(View.VISIBLE);

        saurabh_img=(ImageView)findViewById(R.id.saurabhimg);
        saurabh_img.setImageResource(R.drawable.saurabh_img);

        saurabh_img.setVisibility(View.VISIBLE);



        dipak_img=(ImageView)findViewById(R.id.dipakimg);
        dipak_img.setImageResource(R.drawable.dipak_img);

        dipak_img.setVisibility(View.VISIBLE);

        yash_img=(ImageView)findViewById(R.id.yashimg);
        yash_img.setImageResource(R.drawable.yash_img);

        yash_img.setVisibility(View.VISIBLE);
*/


    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
