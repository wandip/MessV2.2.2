package com.example.saurabh.mess2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
import static com.example.saurabh.mess2.MainActivity.BATCH;
import static com.example.saurabh.mess2.MainActivity.BUFFER;
import static com.example.saurabh.mess2.MainActivity.BUFFER_GRPID;
import static com.example.saurabh.mess2.MainActivity.CURRENT;
import static com.example.saurabh.mess2.MainActivity.UserDataObj;

import com.google.firebase.database.ValueEventListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PaymentActivity extends AppCompatActivity {

    String UserID;
    private TextView PaymentAmtTxtView,CostTxtView,NameTxtView,EmailTxtView,ContactTxtView,NeedHelpTxtView;
    private DatabaseReference mCurrentUser,mCheckQRCode;
    private String TotalAmtLim,TotalAmtUnLim;
    private int RECEIVED_TOKEN=0;
    private boolean connected=false;
    private Button PaymentBtn,BothBtnLim,BothBtnUnLim,onlyLunchBtn,onlyDinnerBtn;
    private String BothTimesGatewayUrl;/*="https://www.instamojo.com/@messedup9/lbefc4952fa694c228b6a7d19b480cf8d/";*/
    private PaymentGatewayActivity paymentGatewayActivity;
    private String paymenturllim,paymenturlunlim; //actual payment link
    private String CashString,onlyLunchGatewayUrl;
    private String onlyDinnerGatewayUrl;
    private DatabaseReference mAdminDatabase,mPaymentLinklim,mPaymentLinkunlim;
    private TextView termsncond;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_new_payment);

        //PaymentAmtTxtView=(TextView)findViewById(R.id.CostTxtView);
        /*NameTxtView=(TextView)findViewById(R.id.NameTextView);
        ContactTxtView=(TextView)findViewById(R.id.ContactEditText);
        EmailTxtView=(TextView)findViewById(R.id.EmailTextView);
        NeedHelpTxtView=(TextView)findViewById(R.id.HelptextView);*/
        CostTxtView=(TextView)findViewById(R.id.CostTxtView1);
        /*NameTxtView.setText(UserDataObj.getName());
        ContactTxtView.setText(UserDataObj.getContact());
        EmailTxtView.setText(UserDataObj.getEmail());*/
        termsncond=(TextView)findViewById(R.id.termsandconditions);

       // PaymentBtn=(Button)findViewById(R.id.paymentBtn);

        //BothBtnLim=(Button)findViewById(R.id.BothButtonLim);
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
        DatabaseReference mCashPaymentString=mAdminDatabase.child("cashpayment");

        mCashPaymentString.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CashString=dataSnapshot.getValue().toString();
                CostTxtView.setText(CashString);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
     /*   mAmmountLim.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TotalAmtLim=dataSnapshot.getValue().toString();
               // PaymentAmtTxtView.setText("Limited Thali     ₹  "+TotalAmtLim);
                BothBtnLim.setText("Limited Thali ₹  "+TotalAmtLim);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        mAmmountUnLim.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TotalAmtUnLim=dataSnapshot.getValue().toString();
                /*PaymentAmtTxtView.setText("Unlimited Thali   ₹  "+TotalAmtUnLim);*/



                BothBtnUnLim.setText("Continue to Pay ₹  "+TotalAmtUnLim);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        final Intent PaymentIntent1 = getIntent();
         UserID = PaymentIntent1.getStringExtra("UserID");
        mCurrentUser= FirebaseDatabase.getInstance().getReference().child("users").child(UserID);


        termsncond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               showTermsandCondDialog();

            }
        });


       /* BothBtnLim.setOnClickListener(new View.OnClickListener() {
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


        });*/
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

    private void showTermsandCondDialog() {



        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);


        final TextView terms = new TextView(PaymentActivity.this);
        terms.setText("");
        terms.setMovementMethod(new ScrollingMovementMethod());
        FrameLayout container = new FrameLayout(PaymentActivity.this);
        LayoutInflater inflater = PaymentActivity.this.getLayoutInflater();
        final View layout= inflater.inflate(R.layout.termstextview, null);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 50;
        params.rightMargin=50;
        layout.setLayoutParams(params);
        container.addView(terms);
        builder.setView(layout);
        builder.setTitle("TERMS AND CONDITIONS") //
                 //
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        dialog.dismiss();
                    }
                }); //
              /*  .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Vibrator v = (Vibrator) PaymentActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(20);
                        // TODO
                        dialog.dismiss();
                    }
                });*/
        builder.show();



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
