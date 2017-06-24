package com.messedup.saurabh.mess2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity {

    private Button mRegBtn;
    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mCPasswordField;
    private EditText mContactField;
    private EditText mCollegeField;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    private boolean connected=false;
    public static int SIGN_IN_CHECK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        mRegBtn=(Button)findViewById(R.id.regButton);
        mNameField=(EditText)findViewById(R.id.NameEditText);
        mEmailField=(EditText)findViewById(R.id.EmailEditText);
        mPasswordField=(EditText)findViewById(R.id.PassEditTex);
        mCPasswordField=(EditText)findViewById(R.id.CPassEditTex);
        mCollegeField=(EditText)findViewById(R.id.CollegeEditText);
       // mCollegeField.setText("PICT");
        mContactField=(EditText)findViewById(R.id.NumEditText);
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("users"); //root directory of Firebase and new child

        mProgress=new ProgressDialog(this);
        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isConnected()) {
                    Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(20);

                    if(validateForm()) {

                        final String name=mNameField.getText().toString().trim();
                        final String email=mEmailField.getText().toString().trim();
                        final String college=mCollegeField.getText().toString().trim();
                        final String contact=mContactField.getText().toString().trim();


                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

                        builder.setTitle("CONFIRM!") //
                                .setMessage("Name : "+name+"\n"+"College : "+college+"\n"+"Contact : "+contact+"\n"+"E-Mail : "+email+"\n"
                                        +"\nEMAIL VERIFICATION MAIL WOULD BE SENT TO YOUR EMAIL ID! \nPLEASE CONFIRM!") //
                                .setPositiveButton("CONFIRM DETAILS", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        startRegister();

                                    }
                                }) //
                                .setNegativeButton("EDIT DETAILS", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();

                                    }
                                });
                        builder.setCancelable(false);
                        builder.show();



                    }
                }
                else
                {
                    Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {0, 75,100,75};

                    // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
                    v.vibrate(pattern, -1);
                    Toast.makeText(getBaseContext(),"No Internet! Please Check your Connection",Toast.LENGTH_LONG).show();

                }
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    // NOTE: this Activity should get onpen only when the user is not signed in, otherwise
                    // the user will receive another verification email.
                    //sendVerificationEmail();
                } else {
                    // User is signed out
                   // FirebaseAuth.getInstance().signOut();

                }
                // ...
            }
        };

    }

    private void sendVerificationEmail() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent


                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            Intent afterregIntent=new Intent(RegisterActivity.this, LoginActivity.class);
                            //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));e
                            afterregIntent.putExtra("sentver",true);
                            startActivity(afterregIntent);
                           Toast.makeText(RegisterActivity.this,"Email Verification Mail Sent to " + user.getEmail(),Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });

    }

    private void startRegister() {


       final String name=mNameField.getText().toString().trim();
        final String email=mEmailField.getText().toString().trim();
        String password =mPasswordField.getText().toString().trim();
        final String college=mCollegeField.getText().toString().trim();
        final String contact=mContactField.getText().toString().trim();
        String cpassword=mCPasswordField.getText().toString().trim();

        if(validateForm())
        {
            mProgress.setMessage("Signing You Up...");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {

                        String user_id=mAuth.getCurrentUser().getUid(); //gives current users unique id


                        HashMap<String,Object> RegUser=new HashMap<String, Object>();
                        RegUser.put("name",name);
                        RegUser.put("qrcode","default");
                        RegUser.put("email",email);
                        RegUser.put("contact",contact);
                        RegUser.put("college",college);
                        RegUser.put("scannedlunch","-1");
                        RegUser.put("scanneddinner","-1");
                        RegUser.put("batch","not paid");
                        RegUser.put("paidnext","not paid");
                        RegUser.put("paidtime","not paid");
                        RegUser.put("buffgroupid","not paid");
                        RegUser.put("endsub","-56");
                        RegUser.put("groupid","not paid");

                        DatabaseReference current_user_db;

                        current_user_db= mDatabase.child(user_id); //goes inside the current user ref

                       /* current_user_db.child("name").setValue(name);
                        current_user_db.child("qrcode").setValue("default");
                        current_user_db.child("email").setValue(email);     // UNCCOMENT THIS TO ADD USER TO DATABASE
                        current_user_db.child("contact").setValue(contact);
                        current_user_db.child("college").setValue(college);
                        current_user_db.child("scannedlunch").setValue("-1");
                        current_user_db.child("scanneddinner").setValue("-1");
                        current_user_db.child("batch").setValue("not paid");
                        current_user_db.child("paidnext").setValue("not paid");
                        current_user_db.child("paidtime").setValue("not paid");
                        current_user_db.child("buffgroupid").setValue("not paid");
                        current_user_db.child("endsub").setValue("-56");
                        current_user_db.child("groupid").setValue("not paid");
*/

                        current_user_db.updateChildren(RegUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mProgress.dismiss();
                                SIGN_IN_CHECK++;
                                sendVerificationEmail();


                            }
                        });
                        Log.v("E_VALUE","babbaba : "+SIGN_IN_CHECK);

                        Log.v("E_VALUE","College : "+college);
                        Log.v("E_VALUE","Contact : "+contact);
                        Log.v("E_VALUE","Email : "+email);
                       // Log.v("E_VALUE","GroupID : "+UserDataObj.getGroupid());
                        Log.v("E_VALUE","NAME : "+name);
                        Log.v("E_VALUE","USER ID : "+user_id);




                        /*Intent logIntent=new Intent(RegisterActivity.this,LoginActivity.class);
                      // logIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(logIntent);*/
                    }
                    else
                    {
                        mProgress.dismiss();
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getBaseContext(),
                                    "User with this email already exist.", Toast.LENGTH_LONG).show();
                        }else
                        {
                            Toast.makeText(getBaseContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                        }
                    }

                }
            });
        }


    }

    private boolean validateForm() {  //VALIDATE REGISTRATION FORM AAFTERUSER CLICKS REGISTER BUTTON SHOWS RED COLOR
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required");
            valid = false;
        } else {
            mEmailField.setError(null);
        }
        if(!email.contains("@") || !email.contains(".co"))
        {
            mEmailField.setError("Enter Valid Email ID");
            valid=false;
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }


        String cpassword = mCPasswordField.getText().toString();
        if (TextUtils.isEmpty(cpassword)) {
            mCPasswordField.setError("Required");
            valid = false;
        } else {
            mCPasswordField.setError(null);
        }

        String name = mNameField.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mNameField.setError("Required");
            valid = false;
        } else {
            mNameField.setError(null);
        }

        String contact = mContactField.getText().toString();
        if (TextUtils.isEmpty(contact)) {
            mContactField.setError("Required");
            valid = false;
        } else {
            mContactField.setError(null);
        }

        if(contact.length()!=10)
        {
            mContactField.setError("Enter Valid 10 Digit Number");
            valid=false;
        }

        String college = mCollegeField.getText().toString();
        if (TextUtils.isEmpty(college)) {
            mCollegeField.setError("Required");
            valid = false;
        } else {
            mCollegeField.setError(null);
        }

        if(password.length()<6)
        {
            mPasswordField.setError("Minimum 6 Characters Required");
            valid=false;
        }



        if(!password.equals(cpassword))
        {
            mPasswordField.setError("Passwords Don't Match");
            mCPasswordField.setError("Passwords Don't Match");
            valid=false;
        }




        return valid;
    }

    @Override
    protected void onStart() {

        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
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

}
