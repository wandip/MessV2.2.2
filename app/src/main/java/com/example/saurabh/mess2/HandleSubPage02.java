package com.example.saurabh.mess2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by saurabh on 25/3/17.
 */

public class HandleSubPage02 extends AppCompatActivity {
    private Button bButton1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sub_page02);


       // bButton1 = (Button) findViewById(R.id.LoginBtn);
       /* bButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent=new Intent(HandleSubPage02.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
*/

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
