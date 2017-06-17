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
import android.widget.EditText;
import android.widget.Toast;

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
    String uidtemp;
    private int USER_FOUND_FLAG;
    private boolean connected=false;


    public void showDialogMethod() {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        final EditText input = new EditText(context);
        builder.setView(input);
        builder.setTitle("ADD FRIEND") //
                .setMessage("Enter Email ID of your Friend") //
                .setPositiveButton("Add Friend", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(20);
                            REQUEST_USER_EMAIL = input.getText().toString();



                            AddFriendOnClick(REQUEST_USER_EMAIL);
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
        final int[] number_of_cur_grp_mem = new int[1];
        USER_FOUND_FLAG=0;

        mDatabaseGroups=FirebaseDatabase.getInstance().getReference().child("group");


        mDatabaseGroups.child(UserDataObj.getGroupid()).child("memberid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                number_of_cur_grp_mem[0] =((int) dataSnapshot.getChildrenCount());
                if(number_of_cur_grp_mem[0]>=4)
                {
                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {0, 75,100,75};
                    Log.v("E_VALUE","NUMBER OF CUR USER GRP MEM 2:"+number_of_cur_grp_mem[0]);
                    // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
                    v.vibrate(pattern, -1);
                    Toast.makeText(context,"Sorry! Your Group Size cannot exceed 4!",Toast.LENGTH_LONG).show();

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
                    String paidstatus=dsp.child("qrcode").getValue(String.class);
                    String req_username=dsp.child("name").getValue(String.class);

                    String req_user_email_trim= request_user_email.trim();

                    if(req_user_email_trim.equals(UserDataObj.getEmail()))
                    {
                        Toast.makeText(context,"Caught You ;P Don't Test Us we are full Proof !",Toast.LENGTH_LONG).show();
                        return;
                    }


                    if(req_user_email_trim.equals(emailtemp)) //FINDS THE USER ID corresponding to EMAIL Given
                    {
                        if(paidstatus.equals("default")||paidstatus.equals("paid"))
                        {
                            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                            long[] pattern = {0, 75,100,75};

                            // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
                            v.vibrate(pattern, -1);
                            Toast.makeText(context,"Your Friend "+req_username+" has not Confirmed Payment!",Toast.LENGTH_LONG).show();
                            USER_FOUND_FLAG=1;
                        }
                        else
                        {
                            uidtemp=dsp.getKey();
                            REQ_USER_ORIG_GRP=dsp.child("groupid").getValue(String.class);
                            Log.v("E_VALUE","EMAIL FOUND : "+emailtemp);

                            Log.v("E_VALUE","UID FOUND : "+uidtemp);
                            REQ_USER_ID=uidtemp;
                            USER_FOUND_FLAG=1;
                            updateGroupId(uidtemp);

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
        Log.v("E_VALUE","REQ USER UID INUPDATEGROUP METHOD : "+uidtemp2);

        Log.v("E_VALUE","CURRENT USER GRPID INUPDATEGROUP METHOD : "+MainActivity.UserDataObj.getGroupid());
        CUR_USER_GRP_ID=MainActivity.UserDataObj.getGroupid();

       /* mDatabaseInCurUser.child("groupid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                REQ_USER_ORIG_GRP = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
        Log.v("E_VALUE","REQ USER GRPID ORIG METHOD : "+REQ_USER_ORIG_GRP);


        GroupAssignLogic groupAssignLogic=new GroupAssignLogic(CUR_USER_ID,REQ_USER_ID,CUR_USER_GRP_ID,REQ_USER_ORIG_GRP);
        groupAssignLogic.initiateAssign();


      //  searchReqUserGrpIfPresent(REQ_USER_ORIG_GRP);

        mDatabaseInReqUser.child("groupid").setValue(MainActivity.UserDataObj.getGroupid());




      // mDatabaseGroups.child(CURRENT_USER_GROUP_ID).

    }

    private void searchReqUserGrpIfPresent(final String req_user_orig_grp) {

        mDatabaseGroups.child(req_user_orig_grp).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    String searchgrpmemid = dsp.child("memberid").getValue(String.class);

                    if(req_user_orig_grp.equals(searchgrpmemid))
                    {
                        String searchgrpid=dsp.getKey();
                        int searchgrpsize=dsp.child("size").getValue(Integer.class);
                        if(searchgrpsize==1)
                        {
                            Log.v("E_VALUE","GRP SIZE IS 1, GRP ID FOUND IS: "+searchgrpid);
                            //grpsizecase1(searchgrpid);
                        }

                    }
                 //   String searchgrpid = dsp.child("email").getValue(String.class);



                }

                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


       // mDatabaseGroups.child(req_user_orig_grp).child("memberid").child(uidtemp).remove();

    }

    private void grpsizecase1(String searchgrpid) {

      mDatabaseGroups.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {

          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });

        mDatabaseInCurUserGrp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.child("size").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                }); //
               /* .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(20);
                        // TODO
                        dialog.dismiss();
                    }
                })*/
        builder.show();


    }
}
