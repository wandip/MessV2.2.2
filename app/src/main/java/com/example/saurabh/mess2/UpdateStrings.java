package com.example.saurabh.mess2;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.saurabh.mess2.MainActivity.BATCH;
import static com.example.saurabh.mess2.MainActivity.BUFFER;
import static com.example.saurabh.mess2.MainActivity.CURRENT;

/**
 * Created by saurabh on 18/6/17.
 */

public class UpdateStrings extends AppCompatActivity {



    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public void initiateUpdation() {

        Log.v("E_VALUE","UPDATION BATCH"+BATCH);


        final boolean[] Updated = {false};


        DatabaseReference mBatchDatabase = FirebaseDatabase.getInstance().getReference().child(BATCH);

        mBatchDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {




                CURRENT = dataSnapshot.child("current").getValue().toString();
                BUFFER = dataSnapshot.child("buffer").getValue().toString();

                Updated[0] =true;



                // updationLogic();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }

    /*private void updationLogic()
    {
        String TEMP = WORKING;

        WORKING = ADDITION;
        ADDITION = BUFFER;
        BUFFER = TEMP;

    }
*/

}
