package com.example.saurabh.mess2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn;
    private Button SignupBtn;
    private ImageButton PassVisBtn;
    private EditText emailidedtxt,passwordedtxt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Typeface ralewaysemibold =Typeface.createFromAsset(getAssets(),"Raleway-SemiBold.ttf");

        Typeface ralewayreg=Typeface.createFromAsset(getAssets(),"Raleway-Regular.ttf");
        TextView forgottxt =(TextView)findViewById(R.id.forgottextView);
        forgottxt.setTypeface(ralewayreg);

        loginBtn=(Button)findViewById(R.id.loginButton);
        SignupBtn=(Button)findViewById(R.id.SignUpButton);
        PassVisBtn=(ImageButton)findViewById(R.id.passvisBtn);
        emailidedtxt=(EditText)findViewById(R.id.EmaileditText) ;
        passwordedtxt=(EditText)findViewById(R.id.PasswordeditText);
        emailidedtxt.setTypeface(ralewayreg);
        passwordedtxt.setTypeface(ralewayreg);

        loginBtn.setTypeface(ralewayreg);
        SignupBtn.setTypeface(ralewayreg);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(loginIntent);
            }
        });

        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(regIntent);
            }
        });


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
