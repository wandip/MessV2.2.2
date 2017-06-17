package com.example.saurabh.mess2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.saurabh.mess2.MainActivity.UserDataObj;

import com.google.firebase.database.ValueEventListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PaymentActivity extends AppCompatActivity {

    String UserID;
    private TextView PaymentAmtTxtView,NameTxtView,EmailTxtView,ContactTxtView,NeedHelpTxtView;
    private DatabaseReference mCurrentUser,mCheckQRCode;
    private String TotalAmtLim,TotalAmtUnLim;
    private int RECEIVED_TOKEN=0;
    private boolean connected=false;
    private Button PaymentBtn,BothBtnLim,BothBtnUnLim,onlyLunchBtn,onlyDinnerBtn;
    private String BothTimesGatewayUrl;/*="https://www.instamojo.com/@messedup9/lbefc4952fa694c228b6a7d19b480cf8d/";*/
    private PaymentGatewayActivity paymentGatewayActivity;
    private String paymenturllim,paymenturlunlim; //actual payment link
    private String onlyLunchGatewayUrl;
    private String onlyDinnerGatewayUrl;
    private DatabaseReference mAdminDatabase,mPaymentLinklim,mPaymentLinkunlim;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //PaymentAmtTxtView=(TextView)findViewById(R.id.CostTxtView);
        NameTxtView=(TextView)findViewById(R.id.NameTextView);
        ContactTxtView=(TextView)findViewById(R.id.ContactEditText);
        EmailTxtView=(TextView)findViewById(R.id.EmailTextView);
        NeedHelpTxtView=(TextView)findViewById(R.id.HelptextView);

        NameTxtView.setText(UserDataObj.getName());
        ContactTxtView.setText(UserDataObj.getContact());
        EmailTxtView.setText(UserDataObj.getEmail());


        PaymentBtn=(Button)findViewById(R.id.paymentBtn);

        BothBtnLim=(Button)findViewById(R.id.BothButtonLim);
        BothBtnUnLim=(Button)findViewById(R.id.BothButtonUnLim);

       // onlyLunchBtn=(Button)findViewById(R.id.onlyLunchButton);
       // onlyDinnerBtn=(Button)findViewById(R.id.onlyDinnerButton);

        /*NeedHelpTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
*/


        mAdminDatabase=FirebaseDatabase.getInstance().getReference().child("admin");
        mPaymentLinklim=mAdminDatabase.child("paymentlinklim"); //change to bothgatewayurl
        mPaymentLinkunlim=mAdminDatabase.child("paymentlinkunlim"); //change to bothgatewayurl

        DatabaseReference mAmmountLim=mAdminDatabase.child("costlimited");
        DatabaseReference mAmmountUnLim=mAdminDatabase.child("costunlimited");


        mPaymentLinklim.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                paymenturllim=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mPaymentLinkunlim.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                paymenturlunlim=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mAmmountLim.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TotalAmtLim=dataSnapshot.getValue().toString();
               // PaymentAmtTxtView.setText("Limited Thali     ₹  "+TotalAmtLim);
                BothBtnLim.setText("Limited Thali ₹  "+TotalAmtLim);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mAmmountUnLim.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TotalAmtUnLim=dataSnapshot.getValue().toString();
                /*PaymentAmtTxtView.setText("Unlimited Thali   ₹  "+TotalAmtUnLim);*/
                BothBtnUnLim.setText("Unlimited Thali ₹  "+TotalAmtUnLim);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        final Intent PaymentIntent1 = getIntent();
         UserID = PaymentIntent1.getStringExtra("UserID");
        mCurrentUser= FirebaseDatabase.getInstance().getReference().child("users").child(UserID);




        BothBtnLim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isConnected()) {
                    Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(20);

                    initiatePayementBothLim();

                }
                else
                {
                    Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {0, 75,100,75};

                    // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
                    v.vibrate(pattern, -1);
                    Toast.makeText(getBaseContext(),"No Internet! Please Check your Connection", Toast.LENGTH_LONG).show();
                }

            }


        });
        BothBtnUnLim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isConnected()) {
                    Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(20);

                    initiatePayementBothUnLim();

                }
                else
                {
                    Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {0, 75,100,75};

                    // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
                    v.vibrate(pattern, -1);
                    Toast.makeText(getBaseContext(),"No Internet! Please Check your Connection", Toast.LENGTH_LONG).show();
                }

            }


        });




    }

    private void initiatePayementBothLim() {

        Intent InAppBrowserIntent = new Intent(PaymentActivity.this, BrowserActivity.class);
        InAppBrowserIntent.putExtra("url", paymenturllim);
        startActivity(InAppBrowserIntent);

    }
    private void initiatePayementBothUnLim() {

        Intent InAppBrowserIntent = new Intent(PaymentActivity.this, BrowserActivity.class);
        InAppBrowserIntent.putExtra("url", paymenturlunlim);
        startActivity(InAppBrowserIntent);

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


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
