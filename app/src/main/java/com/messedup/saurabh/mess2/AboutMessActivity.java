package com.messedup.saurabh.mess2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.messedup.saurabh.mess2.R.color.base_dark;

public class AboutMessActivity extends AppCompatActivity {


    TextView tv1,tv2,tv3,tv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about_mess);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(base_dark));

        }
        tv1=(TextView)findViewById(R.id.textView36);
       // tv1.setMovementMethod(LinkMovementMethod.getInstance());
        tv2=(TextView)findViewById(R.id.textView37);
       // tv2.setMovementMethod(LinkMovementMethod.getInstance());
        tv3=(TextView)findViewById(R.id.textView38);
       // tv3.setMovementMethod(LinkMovementMethod.getInstance());
        tv4=(TextView)findViewById(R.id.textView39);
       // tv4.setMovementMethod(LinkMovementMethod.getInstance());
     /*   Spannable spans = (Spannable) tv1.getText();
        Spannable spans1 = (Spannable) tv2.getText();
        Spannable spans2 = (Spannable) tv3.getText();
        Spannable spans3= (Spannable) tv4.getText();

      //  ClickableSpan clickSpan = new ClickableSpan() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.textView22 :
                        Intent browser1= new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/hAv7v5a53mu"));
                        startActivity(browser1);
                        break;
                    case R.id.textView27 :
                        Intent browser2= new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/Tvfbrc63GDo"));
                        startActivity(browser2);
                        break;
                    case R.id.textView30 :
                        Intent browser3= new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/rno6Y5c3uk52"));
                        startActivity(browser3);
                        break;
                    case R.id.textView33 :
                        Intent browser4= new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/2qswx6VYWw52"));
                        startActivity(browser4);
                        break;
                }

            }*/
      //  };
       /* spans.setSpan(clickSpan, 0, spans.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spans.setSpan(clickSpan, 0, spans1.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spans.setSpan(clickSpan, 0, spans2.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spans.setSpan(clickSpan, 0, spans3.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/
       /* tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/


        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browser1= new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/hAv7v5a53mu"));
                startActivity(browser1);
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browser1= new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/Tvfbrc63GDo"));
                startActivity(browser1);
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browser1= new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/rno6Y5c3uk52"));
                startActivity(browser1);
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browser1= new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/2qswx6VYWw52"));
                startActivity(browser1);
            }
        });


    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
