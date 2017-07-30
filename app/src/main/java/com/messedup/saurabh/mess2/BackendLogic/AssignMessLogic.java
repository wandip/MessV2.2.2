package com.messedup.saurabh.mess2.BackendLogic;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.messedup.saurabh.mess2.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by saurabh on 2/5/17.
 */
import com.google.firebase.database.ValueEventListener;

public class AssignMessLogic extends AppCompatActivity {
    private static int messname, sets;
    private static ArrayList<MessInfo> m1 = new ArrayList<MessInfo>();
    private static MessInfo tempMess;
    private DatabaseReference mDatabase, mDatabaseInMessRef, mDatabaseMessRef;
    private String BatchToChange;



    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    public AssignMessLogic(String batchToChange) {
        BatchToChange = batchToChange;
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


    private void logic3(int date) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(BatchToChange.equals("batch1"))
            mDatabaseMessRef = mDatabase.child("mess");
        else if(BatchToChange.equals("batch2"))
            mDatabaseMessRef = mDatabase.child("mess2");

        DatabaseReference mInMessGroupList;
        mInMessGroupList= mDatabaseMessRef.child(m1.get(0).mid).child("grouplist");
        mInMessGroupList.setValue(null);
        mInMessGroupList= mDatabaseMessRef.child(m1.get(1).mid).child("grouplist");
        mInMessGroupList.setValue(null);
        mInMessGroupList= mDatabaseMessRef.child(m1.get(2).mid).child("grouplist");
        mInMessGroupList.setValue(null);


        for (int i = 1; i < 4; i++) {

            //System.out.print("mess for set " + i + "is ");
            messname=((i+date-2)%3);
            if (messname < 4)
                mInMessGroupList= mDatabaseMessRef.child(m1.get(messname).mid).child("grouplist");
           /* else if(messname==3)
                mInMessGroupList= mDatabaseMessRef.child(m1.get(0).mid).child("grouplist");
            else
                mInMessGroupList= mDatabaseMessRef.child(m1.get(3).mid).child("grouplist");

*/

            //mInMessGroupList.setValue(null);

            int j=Group.dip.get(i-1).size()-1;
            while (j>=0) {
                Log.v("E_VALUE","IN LOGIC 3: "+(i-1)+". "+Group.dip.get(i-1).get(j));
               /* DatabaseReference mTemp =*/ mInMessGroupList.child((String) Group.dip.get(i - 1).get(j)).setValue("0");

                j--;

            }
        }
    }



    private void logic4(int date) {
        mDatabase = FirebaseDatabase.getInstance().getReference();


        if(BatchToChange.equals("batch1"))
            mDatabaseMessRef = mDatabase.child("mess");
        else if(BatchToChange.equals("batch2"))
            mDatabaseMessRef = mDatabase.child("mess2");

        DatabaseReference mInMessGroupList;
        mInMessGroupList= mDatabaseMessRef.child(m1.get(0).mid).child("grouplist");
        mInMessGroupList.setValue(null);
        mInMessGroupList= mDatabaseMessRef.child(m1.get(1).mid).child("grouplist");
        mInMessGroupList.setValue(null);
        mInMessGroupList= mDatabaseMessRef.child(m1.get(2).mid).child("grouplist");
        mInMessGroupList.setValue(null);
        mInMessGroupList= mDatabaseMessRef.child(m1.get(3).mid).child("grouplist");
        mInMessGroupList.setValue(null);

        for (int i = 1; i < 5; i++) {
     //       System.out.print("mess for set " + i + " is ");
            messname = ((i + date - 2) % 4);
//
            if(messname==0)
           mInMessGroupList= mDatabaseMessRef.child(m1.get(0).mid).child("grouplist");
            if(messname==1)
                mInMessGroupList= mDatabaseMessRef.child(m1.get(1).mid).child("grouplist");
            if(messname==2)
                mInMessGroupList= mDatabaseMessRef.child(m1.get(0).mid).child("grouplist");
            if(messname==3)
                mInMessGroupList= mDatabaseMessRef.child(m1.get(2).mid).child("grouplist");



            int j=Group.dip.get(i-1).size()-1;
            while (j>=0) {
                Log.v("E_VALUE","IN LOGIC 4: "+(i-1)+". "+Group.dip.get(i-1).get(j));
                DatabaseReference mTemp = mInMessGroupList.child((String) Group.dip.get(i - 1).get(j));
                mTemp.setValue("0");
                j--;

            }

        }

      /*  for (int i = 1; i < 5; i++) {
            //       System.out.print("mess for set " + i + " is ");
            messname = ((i + date - 2) % 4);
//
            DatabaseReference mInMessMessName= mDatabaseMessRef.child(m1.get(messname).mid).child("messname");
            final String[] TodaysMessTemp = new String[1];
            mInMessMessName.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TodaysMessTemp[0] =dataSnapshot.getValue().toString();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            int j=Group.dip.get(i-1).size()-1;
            while (j>=0) {
                Log.v("E_VALUE","IN LOGIC 4: "+(i-1)+". "+Group.dip.get(i-1).get(j));


                DatabaseReference mInGroup=FirebaseDatabase.getInstance().getReference().child("group").child((String) Group.dip.get(i - 1).get(j));
                mInGroup.child("todaysmess").setValue(TodaysMessTemp[0]);



                j--;

            }

        }*/
/*
        DatabaseReference mInMessName= mDatabaseMessRef.child(m1.get(messname).mid).child("messname");
*/
    }

    /*
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
     */

    private void logic5(int date) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(BatchToChange.equals("batch1"))
            mDatabaseMessRef = mDatabase.child("mess");
        else if(BatchToChange.equals("batch2"))
            mDatabaseMessRef = mDatabase.child("mess2");

        DatabaseReference mInMessGroupList;
        mInMessGroupList= mDatabaseMessRef.child(m1.get(0).mid).child("grouplist");
        mInMessGroupList.setValue(null);
        mInMessGroupList= mDatabaseMessRef.child(m1.get(1).mid).child("grouplist");
        mInMessGroupList.setValue(null);
        mInMessGroupList= mDatabaseMessRef.child(m1.get(2).mid).child("grouplist");
        mInMessGroupList.setValue(null);
        mInMessGroupList= mDatabaseMessRef.child(m1.get(3).mid).child("grouplist");
        mInMessGroupList.setValue(null);

        for (int i = 1; i < 6; i++) {

            //System.out.print("mess for set " + i + "is ");
            messname = ((i + date - 2) % 5);

            if (messname < 3)
                mInMessGroupList= mDatabaseMessRef.child(m1.get(messname).mid).child("grouplist");
            else if(messname==3)
                mInMessGroupList= mDatabaseMessRef.child(m1.get(0).mid).child("grouplist");
            else
                mInMessGroupList= mDatabaseMessRef.child(m1.get(1).mid).child("grouplist");



            //mInMessGroupList.setValue(null);

            int j=Group.dip.get(i-1).size()-1;
            while (j>=0) {
                Log.v("E_VALUE","IN LOGIC 5: "+(i-1)+". "+Group.dip.get(i-1).get(j));
               /* DatabaseReference mTemp =*/ mInMessGroupList.child((String) Group.dip.get(i - 1).get(j)).setValue("0");

                j--;

            }
        }
    }

    private void logic6(int date) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(BatchToChange.equals("batch1"))
            mDatabaseMessRef = mDatabase.child("mess");
        else if(BatchToChange.equals("batch2"))
            mDatabaseMessRef = mDatabase.child("mess2");

        DatabaseReference mInMessGroupList;
        mInMessGroupList= mDatabaseMessRef.child(m1.get(0).mid).child("grouplist");
        mInMessGroupList.setValue(null);
        mInMessGroupList= mDatabaseMessRef.child(m1.get(1).mid).child("grouplist");
        mInMessGroupList.setValue(null);
        mInMessGroupList= mDatabaseMessRef.child(m1.get(2).mid).child("grouplist");
        mInMessGroupList.setValue(null);
        mInMessGroupList= mDatabaseMessRef.child(m1.get(3).mid).child("grouplist");
        mInMessGroupList.setValue(null);

        for (int i = 1; i < 7; i++) {

            //System.out.print("mess for set " + i + "is ");/////ABCabD
            messname = ((i + date - 2) % 3);

            if (messname < 4)
                mInMessGroupList= mDatabaseMessRef.child(m1.get(messname).mid).child("grouplist");

            //mInMessGroupList.setValue(null);

            int j=Group.dip.get(i-1).size()-1;
            while (j>=0) {
                Log.v("E_VALUE","IN LOGIC 6: "+(i-1)+". "+Group.dip.get(i-1).get(j));
                DatabaseReference mTemp = mInMessGroupList.child((String) Group.dip.get(i - 1).get(j));
                mTemp.setValue("0");
                j--;

            }
        }

    }

    private void logic7(int date) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(BatchToChange.equals("batch1"))
            mDatabaseMessRef = mDatabase.child("mess");
        else if(BatchToChange.equals("batch2"))
            mDatabaseMessRef = mDatabase.child("mess2");

        DatabaseReference mInMessGroupList;
        mInMessGroupList= mDatabaseMessRef.child(m1.get(0).mid).child("grouplist");
        mInMessGroupList.setValue(null);
        mInMessGroupList= mDatabaseMessRef.child(m1.get(1).mid).child("grouplist");
        mInMessGroupList.setValue(null);
        mInMessGroupList= mDatabaseMessRef.child(m1.get(2).mid).child("grouplist");
        mInMessGroupList.setValue(null);
        mInMessGroupList= mDatabaseMessRef.child(m1.get(3).mid).child("grouplist");
        mInMessGroupList.setValue(null);

        for (int i = 1; i < 8; i++) {

            //System.out.print("mess for set " + i + "is ");
            messname=((i+date-2+((date-1)/7))%7);

            if(messname==0)
                mInMessGroupList= mDatabaseMessRef.child(m1.get(1).mid).child("grouplist");
            if(messname==1)
                mInMessGroupList= mDatabaseMessRef.child(m1.get(0).mid).child("grouplist");

            if(messname==2)
                mInMessGroupList= mDatabaseMessRef.child(m1.get(2).mid).child("grouplist");

            if(messname==3)
                mInMessGroupList= mDatabaseMessRef.child(m1.get(1).mid).child("grouplist");

            if(messname==4)
                mInMessGroupList= mDatabaseMessRef.child(m1.get(0).mid).child("grouplist");

            if(messname==5)
                mInMessGroupList= mDatabaseMessRef.child(m1.get(2).mid).child("grouplist");

            if(messname==6)
                mInMessGroupList= mDatabaseMessRef.child(m1.get(0).mid).child("grouplist");



            //mInMessGroupList.setValue(null);

            int j=Group.dip.get(i-1).size()-1;
            while (j>=0) {
                Log.v("E_VALUE","IN LOGIC 4: "+(i-1)+". "+Group.dip.get(i-1).get(j));
                DatabaseReference mTemp = mInMessGroupList.child((String) Group.dip.get(i - 1).get(j));
                mTemp.setValue("0");
                j--;

            }
        }
    }

    private void logic9(int date) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(BatchToChange.equals("batch1"))
            mDatabaseMessRef = mDatabase.child("mess");
        else if(BatchToChange.equals("batch2"))
            mDatabaseMessRef = mDatabase.child("mess2");
        DatabaseReference mInMessGroupList;
        mInMessGroupList= mDatabaseMessRef.child(m1.get(0).mid).child("grouplist");
        mInMessGroupList.setValue(null);
        mInMessGroupList= mDatabaseMessRef.child(m1.get(1).mid).child("grouplist");
        mInMessGroupList.setValue(null);
        mInMessGroupList= mDatabaseMessRef.child(m1.get(2).mid).child("grouplist");
        mInMessGroupList.setValue(null);
        mInMessGroupList= mDatabaseMessRef.child(m1.get(3).mid).child("grouplist");
        mInMessGroupList.setValue(null);


        for (int i = 1; i < 10; i++) {

            //System.out.print("mess for set " + i + "is ");/////ABCabD
            messname = ((i + date - 2) % 3);

            if (messname < 4)
                mInMessGroupList= mDatabaseMessRef.child(m1.get(messname).mid).child("grouplist");

            int j=Group.dip.get(i-1).size()-1;
            while (j>=0) {
                Log.v("E_VALUE","IN LOGIC 9: "+(i-1)+". "+Group.dip.get(i-1).get(j));
                DatabaseReference mTemp = mInMessGroupList.child((String) Group.dip.get(i - 1).get(j));
                mTemp.setValue("0");
                j--;

            }
        }
    }
    public void switchlogic() {



        //Get system date
        Date date = new Date();

        int d = date.getDate();
        sets = MainActivity.obj.cnt;
        switch (sets) {

            case 3:
                logic3(d);
                break;
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
            case 9:
                logic9(d);
        }
    }
}




