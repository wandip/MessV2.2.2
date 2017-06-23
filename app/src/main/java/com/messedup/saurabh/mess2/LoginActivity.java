package com.messedup.saurabh.mess2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.messedup.saurabh.mess2.RegisterActivity.SIGN_IN_CHECK;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn;
    private Button SignupBtn;
    private ImageButton PassVisBtn;
    private EditText emailidedtxt,passwordedtxt;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseUsers,temp,mDatabase;
    private ProgressDialog mProgress,mProgressG;
    private SignInButton mGoogleBtn;
    private TextView mForgotPass;
    private static final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG ="LOGIN_ACTIVITY";
    public int VERIFIED_FLAG=0;
    private int count=0;
    private boolean connected=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        Typeface ralewaysemibold =Typeface.createFromAsset(getAssets(),"Raleway-SemiBold.ttf");

        final Typeface ralewayreg=Typeface.createFromAsset(getAssets(),"Raleway-Regular.ttf");
        mForgotPass =(TextView)findViewById(R.id.forgottextView);
        mForgotPass.setTypeface(ralewayreg);
        mAuth=FirebaseAuth.getInstance();
        loginBtn=(Button)findViewById(R.id.loginButton);
        SignupBtn=(Button)findViewById(R.id.MessSelectBtn);
        mGoogleBtn=(SignInButton)findViewById(R.id.googleBtn);
        PassVisBtn=(ImageButton)findViewById(R.id.passvisBtn);
        emailidedtxt=(EditText)findViewById(R.id.EmaileditText) ;
        passwordedtxt=(EditText)findViewById(R.id.PasswordeditText);
       emailidedtxt.setTypeface(ralewayreg);
        passwordedtxt.setTypeface(ralewayreg);


        loginBtn.setTypeface(ralewayreg);
        SignupBtn.setTypeface(ralewayreg);

        mProgress=new ProgressDialog(LoginActivity.this);
        mProgressG=new ProgressDialog(LoginActivity.this);
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("users");
        mDatabaseUsers.keepSynced(true);


        mForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(isConnected()) {


                    Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(20);

                    if(emailValid())
                    generateDialogueBoxPassRes();

                    //sendPasswordResetMail();


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

        PassVisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(20);

                if(count%2==0)
                {
                    passwordedtxt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordedtxt.setSelection(passwordedtxt.length());
                    passwordedtxt.setTypeface(ralewayreg);
                    count++;
                }
                else
                {
                    passwordedtxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordedtxt.setSelection(passwordedtxt.length());
                    passwordedtxt.setTypeface(ralewayreg);

                    count++;
                }
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected()) {
                    Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(20);

                    checkLogin();
                }
                else
                {
                    Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {0, 75,100,75};

                    // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
                    v.vibrate(pattern, -1);
                    Toast.makeText(getBaseContext(),"No Internet! Please Check your Connection",Toast.LENGTH_LONG).show();
                }
                /*Intent loginIntent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(loginIntent);*/
            }
        });



        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected()) {
                    Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(20);


                    if(SIGN_IN_CHECK>0)
                    {
                          final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                            builder.setTitle("SORRY!") //
                                    .setMessage("Please Re-Install the App to Register Another User") //
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();

                                        }
                                    }); //

                            builder.setCancelable(false);
                            builder.show();



                    }
                    else
                    {
                        Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(regIntent);
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





        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)
                {
                    mProgress.dismiss();
                   // mProgress=null;
                    //startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
                else
                {

                }
            }
        };


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient=new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this,"Sign In Failed",Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isConnected()) {
                    Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(20);
                    mProgressG.setMessage("Fetching your Google Accounts..");
                    mProgressG.show();
                    signIn();
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


        ValueEventListener getUserDetails=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // Group groupdata=dataSnapshot.getValue(Group.class);
                // collectGroupInfo((Map<String,Object>) dataSnapshot.getValue());
               // Log.v("E_VALUE","Data : "+groupdata.getName());



                /*// Result will be holded Here
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    //groupList.add(Integer.valueOf(dsp.getKey()));
                    //int ocunt=(Integer)dsp.child("size").getValue();
                    Group obj=

                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        temp=mDatabaseUsers.child("BDJYZEdkabXUMPQTaVfX5HfHh3M2");
        temp.addValueEventListener(getUserDetails);





    }

    private boolean emailValid() {

        boolean valid=true;
        String email = emailidedtxt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailidedtxt.setError("Required.");
            valid = false;
        } else {
            emailidedtxt.setError(null);
        }

        return valid;

    }

    private void generateDialogueBoxPassRes() {
        final String email = emailidedtxt.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Forgot Password?");
        builder.setMessage("Send Password Reset Email to "+email);

        builder.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                sendPasswordResetMail();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();




    }

    private void sendPasswordResetMail() {


        boolean valid = true;

        final String email = emailidedtxt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailidedtxt.setError("Required");
            valid = false;
        } else {
            emailidedtxt.setError(null);
        }
        if(!email.contains("@") || !email.contains(".co"))
        {
            emailidedtxt.setError("Enter Valid Email ID");
            valid=false;
        }

        if(valid)
        {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,"Password Reset Email Sent to "+email,Toast.LENGTH_LONG).show();
                        }
                    }
                });
        }

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
    protected void onStart() {

        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            mProgressG.dismiss();

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                mProgress.setMessage("Signing You in...");
                mProgress.show();





                // Google Sign In was successful, authenticate with Firebase
                final GoogleSignInAccount account = result.getSignInAccount();



                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                            boolean USER_FOUND_FLAG=false;
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                                    String emailtemp=dsp.child("email").getValue(String.class);


                                    String req_user_email_trim= account.getEmail().trim();



                                    if(req_user_email_trim.equals(emailtemp)) //FINDS THE USER ID corresponding to EMAIL Given
                                    {
                                        USER_FOUND_FLAG=true;

                                    }

                                }
                                if(!USER_FOUND_FLAG)
                                {
                                    mDatabase=FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference current_user_db= mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()); //goes inside the current user ref

                                   /* current_user_db.child("name").setValue(account.getDisplayName().trim());
                                    current_user_db.child("qrcode").setValue("default");
                                    current_user_db.child("email").setValue(account.getEmail().trim());     // UNCCOMENT THIS TO ADD USER TO DATABASE
                                    current_user_db.child("contact").setValue("nocontact");
                                    current_user_db.child("college").setValue("nocollege");
                                    current_user_db.child("scannedlunch").setValue("-1");
                                    current_user_db.child("scanneddinner").setValue("-1");
                                    current_user_db.child("batch").setValue("not paid");
                                    current_user_db.child("paidnext").setValue("not paid");
                                    current_user_db.child("paidtime").setValue("not paid");
                                    current_user_db.child("buffgroupid").setValue("not paid");
                                    current_user_db.child("endsub").setValue("-56");
                                    current_user_db.child("groupid").setValue("not paid");
*/

                                    HashMap<String,Object> RegUser=new HashMap<String, Object>();
                                    RegUser.put("name",account.getDisplayName().trim());
                                    RegUser.put("qrcode","default");
                                    RegUser.put("email",account.getEmail().trim());
                                    RegUser.put("contact","nocontact");
                                    RegUser.put("college","nocollege");
                                    RegUser.put("scannedlunch","-1");
                                    RegUser.put("scanneddinner","-1");
                                    RegUser.put("batch","not paid");
                                    RegUser.put("paidnext","not paid");
                                    RegUser.put("paidtime","not paid");
                                    RegUser.put("buffgroupid","not paid");
                                    RegUser.put("endsub","-56");
                                    RegUser.put("groupid","not paid");

                                    current_user_db.updateChildren(RegUser);




                                }

                                Intent GSignIntent=new Intent(LoginActivity.this,MainActivity.class);
                                GSignIntent.putExtra("gf",1);
                                GSignIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(GSignIntent);
                                Toast.makeText(LoginActivity.this,"Google Signed In",Toast.LENGTH_SHORT).show();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithCredential", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }





                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });





                        // ...
                    }
                });

    }


    private void checkLogin() {

        String email=emailidedtxt.getText().toString().trim();
        String password=passwordedtxt.getText().toString().trim();

        if(validateForm())
        {
            mProgress.setMessage("Logging You in...");
            mProgress.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        mProgress.dismiss();
                        checkIfEmailVerified();

                       // checkuserexists();

                    }
                    else
                    {
                        mProgress.dismiss();
                        Toast.makeText(LoginActivity.this,"Invalid Username or Password",Toast.LENGTH_LONG).show();

                    }
                }
            });

        }

    }

    private void checkIfEmailVerified() {

        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();

        if (user1.isEmailVerified())
        {
            VERIFIED_FLAG=1;
            // user is verified, so you can finish this activity or send user to activity which you want.
            Intent mainIntent1 = new Intent(LoginActivity.this,MainActivity.class);
            mainIntent1.putExtra("vf",1);
            mainIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent1);


            //Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(LoginActivity.this, "Please Verify your Email!", Toast.LENGTH_LONG).show();
            FirebaseAuth.getInstance().signOut();
            Intent reIntent = new Intent(LoginActivity.this,LoginActivity.class);
          //  mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           // startActivity(reIntent);

            //restart this activity

        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailidedtxt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailidedtxt.setError("Required.");
            valid = false;
        } else {
            emailidedtxt.setError(null);
        }

        String password = passwordedtxt.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordedtxt.setError("Required.");
            valid = false;
        } else {
            passwordedtxt.setError(null);
        }

        return valid;

    }

    private void checkuserexists() {

       final String uid= mAuth.getCurrentUser().getUid();
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(uid))
                {
                    Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Please Sign Up !",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
