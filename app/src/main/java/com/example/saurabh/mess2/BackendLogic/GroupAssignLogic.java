package com.example.saurabh.mess2.BackendLogic;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by saurabh on 1/5/17.
 */

public class GroupAssignLogic extends AppCompatActivity
{
    /////current user id : The user who types the another user in textbox
    /////req user id : The user which is to be added in group
    private String CUR_USER_ID,REQ_USER_ID;
    private String CUR_USER_GRP_ID,REQ_USER_GRP_ID;
    private int size1,size2;
    private DatabaseReference mDatabaseGroups;
    private DatabaseReference mDatabaseInReqGrp;
    private DatabaseReference mDatabaseInCurGrp;
    private int orig_cur_size,orig_cur_size2 ;


    public GroupAssignLogic(String CUR_USER_ID, String REQ_USER_ID, String CUR_USER_GRP_ID, String REQ_USER_GRP_ID) {
        this.CUR_USER_ID = CUR_USER_ID;
        this.REQ_USER_ID = REQ_USER_ID;
        this.CUR_USER_GRP_ID = CUR_USER_GRP_ID;
        this.REQ_USER_GRP_ID = REQ_USER_GRP_ID;
        mDatabaseGroups=FirebaseDatabase.getInstance().getReference().child("group");
        mDatabaseInCurGrp=mDatabaseGroups.child(CUR_USER_GRP_ID);
        mDatabaseInReqGrp=mDatabaseGroups.child(REQ_USER_GRP_ID);

    }




    public void initiateAssign() {

          deleteReq();
        addInCurGrp();


    }


    void addInCurGrp() {
        DatabaseReference newUser = mDatabaseInCurGrp.child("memberid").child(REQ_USER_ID);
        newUser.setValue("100");

    }
    void deleteReq()
    {

        mDatabaseInReqGrp.child("memberid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long membercount=dataSnapshot.getChildrenCount();
                if(membercount==1)
                {
                    DatabaseReference mReqUser= mDatabaseInReqGrp.child("memberid").child(REQ_USER_ID);
                    mReqUser.setValue(null);

                    mDatabaseInReqGrp.child("todaysmess").setValue(null);
                }
                else
                {
                    DatabaseReference mReqUser= mDatabaseInReqGrp.child("memberid").child(REQ_USER_ID);
                    mReqUser.setValue(null);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
