/*
package com.example.saurabh.mess2.FDLogic;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.saurabh.mess2.MainActivity.UserDataObj;

*/
/**
 * Created by saurabh on 18/6/17.
 *//*


public class FDUpdate extends AppCompatActivity {

    public static String WORKING;
    public static String ADDITION;
    public static String BUFFER;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public void initiateUpdation() {


        DatabaseReference mFDLogicDatabase = FirebaseDatabase.getInstance().getReference().child("fdlogic");
        DatabaseReference mWorkingDatabase = FirebaseDatabase.getInstance().getReference().child("fdlogic").child("working");
        DatabaseReference mAdditionDatabase = FirebaseDatabase.getInstance().getReference().child("fdlogic").child("addition");
        DatabaseReference mBufferDatabase = FirebaseDatabase.getInstance().getReference().child("fdlogic").child("buffer");

        mFDLogicDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    WORKING = dsp.child("working").getValue(String.class);
                    ADDITION = dsp.child("addition").getValue(String.class);
                    BUFFER = dsp.child("buffer").getValue(String.class);

                }

                updationLogic();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

        private void updationLogic()
        {
            String TEMP = WORKING;

            WORKING = ADDITION;
            ADDITION = BUFFER;
            BUFFER = TEMP;

        }

}*/
