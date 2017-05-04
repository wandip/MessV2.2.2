package com.example.saurabh.mess2.BackendLogic;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.saurabh.mess2.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by saurabh on 2/5/17.
 */
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AssignMessLogic extends AppCompatActivity {
    private static int messname, sets;
    private static ArrayList<MessInfo> m1 = new ArrayList<MessInfo>();
    private static MessInfo tempMess;
    private DatabaseReference mDatabase, mDatabaseInMessRef, mDatabaseMessRef;



    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }


    public void getMessInfo() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseMessRef = mDatabase.child("mess");

        mDatabaseMessRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    // int temp=dsp.child("size").getValue(Integer.class);
                    String tempmessname= ((String)dsp.child("messname").getValue());
                    String tempmessid=dsp.getKey();
                    tempMess = new MessInfo(tempmessid, tempmessname);
                    Log.v("E_VALUE",""+tempMess);
                    m1.add(tempMess);

                }

                switchlogic();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void logic4(int date) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseMessRef = mDatabase.child("mess");


        for (int i = 1; i < 5; i++) {
     //       System.out.print("mess for set " + i + " is ");
            messname = ((i + date - 2) % 4);
//
           DatabaseReference mInMessGroupList= mDatabaseMessRef.child(m1.get(messname).mid).child("grouplist");
            mInMessGroupList.setValue(null);

            int j=Group.dip.get(i-1).size()-1;
            while (j>=0) {
                Log.v("E_VALUE","IN LOGIC 4: "+(i-1)+". "+Group.dip.get(i-1).get(j));
                DatabaseReference mTemp = mInMessGroupList.child((String) Group.dip.get(i - 1).get(j));
                mTemp.setValue("0");
                j--;

            }

        }
    }

    private void logic5(int date) {
        for (int i = 1; i < 6; i++) {

            System.out.print("mess for set " + i + "is ");
            messname = ((i + date - 2) % 5);

            if (messname <= 3)
                System.out.println(m1.get(messname).name);/////normal mapping
            else
                System.out.println(m1.get(1).name);///5th mess is second one B
        }
    }

    private static void logic6(int date) {
        for (int i = 1; i < 7; i++) {

            System.out.print("mess for set " + i + "is ");/////ABCabD
            messname = ((i + date - 2) % 6);

            if (messname < 3)
                System.out.println(m1.get(messname).name);
            else if (messname < 5)
                System.out.println(m1.get(messname - 3).name);
            else
                System.out.println(m1.get(3).name);
        }

    }

    private static void logic7(int date) {
        for (int i = 1; i < 8; i++) {

            System.out.print("mess for set " + i + "is ");
            messname = (((i + date - 2) % 7) % 4);

            System.out.println(m1.get(messname).name);
        }
    }

    public void switchlogic() {



        //Get system date
        Date date = new Date();

        int d = date.getDate();
        sets = MainActivity.obj.cnt;
        switch (sets) {
            case 4:
                logic4(d);
                break;
            case 5:
                logic5(d);
                break;
            case 6:
                logic6(d);
                break;
            case 7:
                logic7(d);
                break;
        }
    }
}




