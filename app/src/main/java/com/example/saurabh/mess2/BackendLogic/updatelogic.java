package com.example.saurabh.mess2.BackendLogic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * Created by saurabh on 23/4/17.
 */

public class updatelogic extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseGroups;
    private String adminurl="BDJYZEdkabXUMPQTaVfX5HfHh3M2";
    private ArrayList<Integer> groupList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseRef=FirebaseDatabase.getInstance().getReference();   //......points to the base of database
        mDatabaseUsers=mDatabaseRef.child("users");   //.....points to 'users' node of the database
        mDatabaseGroups=mDatabaseRef.child("groups"); //.....points to 'groups' node of the database

        mAuth=FirebaseAuth.getInstance();
        if(adminurl==mAuth.getCurrentUser().getUid())
        {
            initiatelogic();
        }




    }

    private void initiatelogic() {

        ValueEventListener getGroupDetails=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Group groupdata=dataSnapshot.getValue(Group.class);
               // collectGroupInfo((Map<String,Object>) dataSnapshot.getValue());

                groupList=new ArrayList<Integer>();
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
        mDatabaseGroups.addValueEventListener(getGroupDetails);


    }

    private void collectGroupInfo(Map<String, Object> groups) {

        ArrayList<Integer> groupSize = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : groups.entrySet()){

            //Get group map
            Map singleGroup = (Map) entry.getValue();
            //Get phone field and append to list
            groupSize.add((int) singleGroup.get("size"));
        }

        System.out.println(groupSize.toString());
    }
}
