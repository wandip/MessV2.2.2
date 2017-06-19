package com.example.saurabh.mess2;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import static android.text.InputType.TYPE_CLASS_PHONE;
import static android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
import static com.example.saurabh.mess2.MainActivity.BATCH;
import static com.example.saurabh.mess2.MainActivity.BUFFER;
import static com.example.saurabh.mess2.MainActivity.BUFFER_GRPID;
import static com.example.saurabh.mess2.MainActivity.CURRENT;
import static com.example.saurabh.mess2.MainActivity.UserDataObj;


import com.example.saurabh.mess2.BackendLogic.Group;
import com.example.saurabh.mess2.BackendLogic.GroupAssignLogic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by saurabh on 24/4/17.
 */

public class AddFriendHandler extends AppCompatActivity{

    private Context context;
    private String CUR_USER_ID,REQ_USER_ID;
    private String CUR_USER_GRP_ID,REQ_USER_GRP_ID;


    public AddFriendHandler(Context context)
    {
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    private DatabaseReference mDatabase,mDatabaseGroups,mDatabaseUsers,mDatabaseInCurUser,mDatabaseInReqUser,mDatabaseInCurUserGrp;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    String REQUEST_USER_EMAIL,REQ_USER_ORIG_GRP;
    String uidtemp,GROUP_SIZE;
    private int USER_FOUND_FLAG;
    private boolean connected=false;


    public void showDialogMethod() {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        final EditText input = new EditText(context);
        input.setInputType(TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setSingleLine();
        FrameLayout container = new FrameLayout(context);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 50;
        params.rightMargin=50;
        input.setLayoutParams(params);
        container.addView(input);
        builder.setView(container);
        builder.setTitle("ADD FRIEND") //
                .setMessage("Enter Email ID of your Friend") //
                .setPositiveButton("Add Friend", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(20);
                            REQUEST_USER_EMAIL = input.getText().toString();
                        DatabaseReference mBatchDatabase = FirebaseDatabase.getInstance().getReference().child(BATCH);

                        mBatchDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                CURRENT = dataSnapshot.child("current").getValue().toString();
                                BUFFER = dataSnapshot.child("buffer").getValue().toString();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });




                        FirebaseDatabase.getInstance().getReference().child("admin").child("groupmaxsize")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        GROUP_SIZE= (String) dataSnapshot.getValue();
                                        continue1(BUFFER_GRPID,REQUEST_USER_EMAIL);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });




                            dialog.dismiss();
                    }
                }) //
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(20);
                        // TODO
                        dialog.dismiss();
                    }
                });
        builder.show();

    }

    public void AddFriendOnClick(final String REQUEST_USER_EMAIL)  //Initialises Database Refrences and gets The Current User ID
    {

        USER_FOUND_FLAG = 0;


        final String[] BufferGroupId = new String[1];
      //  if (isConnected()) {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(20);


            FirebaseDatabase.getInstance().getReference().child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("buffgroupid").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    BufferGroupId[0] = dataSnapshot.getValue().toString();
                    continue0(BufferGroupId[0], REQUEST_USER_EMAIL);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
      //  }
    }

    private void continue0(final String s, final String request_user_email) {


            DatabaseReference mBatchDatabase = FirebaseDatabase.getInstance().getReference().child(BATCH);

            mBatchDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    CURRENT = dataSnapshot.child("current").getValue().toString();
                    BUFFER = dataSnapshot.child("buffer").getValue().toString();
                   continue1(s,request_user_email);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });






    }

    public void continue1(String BufferGroupId,final String REQUEST_USER_EMAIL)
    {
        final int[] number_of_cur_grp_mem = new int[1];

        mDatabaseGroups=FirebaseDatabase.getInstance().getReference().child(BUFFER);

        Log.v("E_VALUE","BUFFERGRPID"+BufferGroupId);

        mDatabaseGroups.child(BufferGroupId).child("memberid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                number_of_cur_grp_mem[0] =((int) dataSnapshot.getChildrenCount());
                if(number_of_cur_grp_mem[0]>=Integer.parseInt(GROUP_SIZE))
                {
                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {0, 75,100,75};
                    Log.v("E_VALUE","NUMBER OF CUR USER GRP MEM 2:"+number_of_cur_grp_mem[0]);
                    // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
                    v.vibrate(pattern, -1);
                    Toast.makeText(context,"Sorry! Your Group Size cannot exceed "+ GROUP_SIZE+"!",Toast.LENGTH_LONG).show();

                }
                else {

                    mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
                    mDatabaseInCurUser = mDatabaseUsers.child(mAuth.getCurrentUser().getUid());

                    Log.v("E_VALUE", "THE CURRENT USER ID IS: " + MainActivity.UserDataObj.getuid());
                    CUR_USER_ID = MainActivity.UserDataObj.getuid();

                    searchRequestUID(REQUEST_USER_EMAIL);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.v("E_VALUE","NUMBER OF CUR USER GRP MEM 1:"+number_of_cur_grp_mem[0]);



    }

    private void searchRequestUID(final String request_user_email) //Searches the User With The Given EMAIL ID
    {

        Log.v("E_VALUE","THE REQUEST USER EMAIL ID IS "+request_user_email);
        


       mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int sum=0;

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    String emailtemp=dsp.child("email").getValue(String.class);
                    String req_buff_grpid=dsp.child("buffgroupid").getValue(String.class);
                    String req_paid_next=dsp.child("paidnext").getValue(String.class);
                    String req_username=dsp.child("name").getValue(String.class);
                    String req_user_batch=dsp.child("batch").getValue(String.class);


                    String req_user_email_trim= request_user_email.trim();

                    if(req_user_email_trim.equals(UserDataObj.getEmail()))
                    {
                        Toast.makeText(context,"Caught You ;P Don't Test Us we are full Proof !",Toast.LENGTH_LONG).show();
                        return;
                    }


                    if(req_user_email_trim.equals(emailtemp)) //FINDS THE USER ID corresponding to EMAIL Given
                    {

                        if(!req_buff_grpid.equals("not paid")&&!req_paid_next.equals("not paid")&&BATCH.equals(req_user_batch))
                        {
                            uidtemp=dsp.getKey();
                            REQ_USER_ORIG_GRP=dsp.child("buffgroupid").getValue(String.class);
                            Log.v("E_VALUE","EMAIL FOUND : "+emailtemp);

                            Log.v("E_VALUE","UID FOUND : "+uidtemp);
                            REQ_USER_ID=uidtemp;
                            USER_FOUND_FLAG=1;
                            updateGroupId(uidtemp);
                        }
                        else
                        {
                            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            long[] pattern = {0, 75,100,75};

                            // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
                            v.vibrate(pattern, -1);
                            Toast.makeText(context,"Your Friend "+req_username+" has not Confirmed Payment! OR is not in your Batch ",Toast.LENGTH_LONG).show();
                            USER_FOUND_FLAG=1;
                        }





                    }

                }
                if(USER_FOUND_FLAG==0)
                {
                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {0, 75,100,75};

                    // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
                    v.vibrate(pattern, -1);
                    Toast.makeText(context,"Email ID Not Found! Please Enter Valid Email ID!",Toast.LENGTH_LONG).show();
                    showDialogMethod();
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        


    }

    private void updateGroupId(String uidtemp2) {


        mDatabaseInReqUser=mDatabaseUsers.child(uidtemp2);

        final String[] BufferGroupId = new String[1];

            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(20);


            /*FirebaseDatabase.getInstance().getReference().child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("buffgroupid").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    BufferGroupId[0] = dataSnapshot.getValue().toString();
                    continue0(BufferGroupId[0], REQUEST_USER_EMAIL);
                    mDatabaseInReqUser.child("buffgroupid").setValue(BufferGroupId[0]);



                    Log.v("E_VALUE","REQ USER GRPID ORIG METHOD : "+REQ_USER_ORIG_GRP);


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
*/

        mDatabaseInReqUser.child("buffgroupid").setValue(BUFFER_GRPID);

        CUR_USER_GRP_ID=BUFFER_GRPID;


        GroupAssignLogic groupAssignLogic=new GroupAssignLogic(CUR_USER_ID,REQ_USER_ID,CUR_USER_GRP_ID,REQ_USER_ORIG_GRP);





        //  groupAssignLogic.initiateAssign();


      //  searchReqUserGrpIfPresent(REQ_USER_ORIG_GRP);

       // mDatabaseInReqUser.child("buffgroupid").setValue(BufferGroupId[0]);




      // mDatabaseGroups.child(CURRENT_USER_GROUP_ID).

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

    public void showNotPaidDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);



        builder.setTitle("ADD FRIEND") //
                .setMessage("Please Pay First, wait for Confirmation if already Paid!") //
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(20);


                        dialog.dismiss();
                    }
                });

        builder.show();


    }
}
