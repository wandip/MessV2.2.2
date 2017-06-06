package com.example.saurabh.mess2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import static com.example.saurabh.mess2.MainActivity.PAYEMENT_DONE;
import static com.example.saurabh.mess2.MainActivity.QRCODE;
import static com.example.saurabh.mess2.MainActivity.UserDataObj;
import com.instamojo.android.activities.PaymentDetailsActivity;

import java.net.URL;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import static com.example.saurabh.mess2.MainActivity.QRCODE;

public class PaymentActivity extends AppCompatActivity {

    String UserID;
    private TextView PaymentAmtTxtView,NameTxtView,EmailTxtView,ContactTxtView;
    private DatabaseReference mCurrentUser,mCheckQRCode;
    private int TotalAmt=3050;
    private int RECEIVED_TOKEN=0;
    private boolean connected=false;
    Button PaymentBtn;
    private String BothTimesGatewayUrl="https://www.instamojo.com/@messedup9/lbefc4952fa694c228b6a7d19b480cf8d/";
    private PaymentGatewayActivity paymentGatewayActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        PaymentAmtTxtView=(TextView)findViewById(R.id.CostTxtView);
        NameTxtView=(TextView)findViewById(R.id.NameTextView);
        ContactTxtView=(TextView)findViewById(R.id.ContactEditText);
        EmailTxtView=(TextView)findViewById(R.id.EmailTextView);

        NameTxtView.setText(UserDataObj.getName());
        ContactTxtView.setText(UserDataObj.getContact());
        EmailTxtView.setText(UserDataObj.getEmail());

        PaymentAmtTxtView.setText("₹  "+TotalAmt);
        PaymentBtn=(Button)findViewById(R.id.paymentBtn);
          final Intent PaymentIntent1 = getIntent();
         UserID = PaymentIntent1.getStringExtra("UserID");
        mCurrentUser= FirebaseDatabase.getInstance().getReference().child("users").child(UserID);


        PaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view,"This Will Initiate The Payment"+UserID,Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

               /* Intent AfterPaymentIntent=new Intent(PaymentActivity.this,SubPage02.class);
                AfterPaymentIntent.putExtra("RECEIVED_TOKEN",true);
                startActivity(AfterPaymentIntent);*/

                if(isConnected()) {
                    Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(20);

                    initiatePayement();



                   /* String Updated_QRCODE="https://firebasestorage.googleapis.com/v0/b/messed-up.appspot.com/o/qrcodes%2FxZLuBRV5cCcjxuCb3eXyJNkcESB3.jpg?alt=media&token=722800db-c7e8-4ca3-a59a-4e7c1a48a9ec";
                    mCurrentUser.child("qrcode").setValue(Updated_QRCODE);
                    Intent FinishedPayement=new Intent(PaymentActivity.this,MainActivity.class);
                    startActivity(FinishedPayement);*/
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

    private void initiatePayement() {


        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://www.instamojo.com/@messedup9/lbefc4952fa694c228b6a7d19b480cf8d/"));
     //   startActivity(i);

        Intent InAppBrowserIntent = new Intent(PaymentActivity.this, BrowserActivity.class);
        InAppBrowserIntent.putExtra("url", BothTimesGatewayUrl);
        startActivity(InAppBrowserIntent);


      //  paymentGatewayActivity.createTransactionDetails();

        //TODO: This method call initializes order of InstaMojo


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
