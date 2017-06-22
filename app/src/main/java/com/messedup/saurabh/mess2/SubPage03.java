package com.messedup.saurabh.mess2;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.messedup.saurabh.mess2.MainActivity.UserDataObj;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubPage03.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubPage03#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubPage03 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context SubPage3Context;
    private View rootView3;
    private TextView UserNameTxtView,UserEmailTxtView,UserGroupIdTxtView,UserContactTxtView,UserCollegeTxtView;
    private String name,email,groupid,contact,college,qrcode;
    private HashMap<String, Object> UserInfoMap= new HashMap<>();
    private DatabaseReference mCurrentUser;
    private boolean connected=false;
    public  Button mPowerFulButton1;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SubPage03() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubPage03.
     */
    // TODO: Rename and change types and number of parameters
    public static SubPage03 newInstance(String param1, String param2) {
        SubPage03 fragment = new SubPage03();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
           /* UserNameTxtView=(TextView)rootView3.findViewById(R.id.userName);
            //UserNameTxtView.setText(name);
            //   UserNameTxtView.setText(MainActivity.UserDataObj.getName());
              Log.v("E_VALUE","IN ONCREATE NAMe iS "+name);
            UserEmailTxtView=(TextView)rootView3.findViewById(R.id.userEmail);
            //   UserEmailTxtView.setText(email);
            UserCollegeTxtView=(TextView)rootView3.findViewById(R.id.userCollege);
            //   UserCollegeTxtView.setText(college);
            UserGroupIdTxtView=(TextView)rootView3.findViewById(R.id.userGroupID);
            //   UserGroupIdTxtView.setText(groupid);
            UserContactTxtView=(TextView)rootView3.findViewById(R.id.userContact);
            //   UserContactTxtView.setText(contact);
*/

        }
    }

    public void passContext(Context context)
    {
        SubPage3Context=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView3=inflater.inflate(R.layout.fragment_sub_page03, container, false);
        Button mAddGrpBtn = (Button) rootView3.findViewById(R.id.AddGroupBtn);


        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {

            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals("4ALe90P970eTtifEAEfHlzwe03u1")) {


                mPowerFulButton1 = (Button) rootView3.findViewById(R.id.SuperPowerfulButton);
                Button mPowerFulButton2 = (Button) rootView3.findViewById(R.id.SuperPowerfulButton2);

                mPowerFulButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showAdminPassDialogMethod();


                    }
                });

                mPowerFulButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showAdminPassDialogMethod2();

                    }
                });


            } else {
                Button mPowerFulButton1 = (Button) rootView3.findViewById(R.id.SuperPowerfulButton);
                mPowerFulButton1.setEnabled(false);
                mPowerFulButton1.setVisibility(View.GONE);
                mPowerFulButton1.setClickable(false);
                Button mPowerFulButton2 = (Button) rootView3.findViewById(R.id.SuperPowerfulButton2);
                mPowerFulButton2.setEnabled(false);
                mPowerFulButton2.setVisibility(View.GONE);
                mPowerFulButton2.setClickable(false);

            }
        }
        else
        {
            Button mPowerFulButton1 = (Button) rootView3.findViewById(R.id.SuperPowerfulButton);
            mPowerFulButton1.setEnabled(false);
            mPowerFulButton1.setVisibility(View.GONE);
            mPowerFulButton1.setClickable(false);
        }


           mAddGrpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  if(isConnected()) {


                if(isConnected())
                {
                    Vibrator v = (Vibrator) SubPage3Context.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(20);
                    final AddFriendHandler mAddFriendHandler = new AddFriendHandler(SubPage3Context);

                    if(!UserDataObj.getQrcode().equals("default")&&!UserDataObj.getQrcode().equals("paid"))
                    {

                        mAddFriendHandler.showDialogMethod();
                    }
                    else
                    {
                        mAddFriendHandler.showNotPaidDialogue();
                    }

                }
                else
                {
                    Vibrator v = (Vibrator) SubPage3Context.getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {0, 75,100,75};

                    // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
                    v.vibrate(pattern, -1);
                    Toast.makeText(SubPage3Context,"No Internet! Please Check your Connection",Toast.LENGTH_LONG).show();
                }





             //   }
                /*else
                {
                    Vibrator v = (Vibrator) SubPage3Context.getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {0, 75,100,75};

                    // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
                    v.vibrate(pattern, -1);
                    Toast.makeText(SubPage3Context,"No Internet! Please Check your Connection",Toast.LENGTH_LONG).show();
                }
*/
            }
        });


        //UserInfoMap=MainActivity.UserDataObj.toMap();

/*
        UserNameTxtView=(TextView)rootView3.findViewById(R.id.userName);
        UserNameTxtView.setText("");

*/




        return rootView3;


    }

    private void showAdminPassDialogMethod2() {



        AlertDialog.Builder builder = new AlertDialog.Builder(SubPage3Context);


        final EditText input = new EditText(SubPage3Context);

        builder.setView(input);
        builder.setTitle("ENTER CREDENTIALS") //
                .setMessage("Enter Admin Root Password") //
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Vibrator v = (Vibrator) SubPage3Context.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(20);
                        String PasswordInput = input.getText().toString();
                        if(PasswordInput.equals("BulltGyangBatchOne"))
                        {
                            FirebaseDatabase.getInstance().getReference().child("admin").child("resumeflag").setValue("notworking");
                            MainActivity MainObj=new MainActivity();
                           MainObj.MonthChangeLogic();

                            Toast.makeText(SubPage3Context,"Logic Run!",Toast.LENGTH_SHORT).show();

                            dialog.dismiss();

                        }
                        else if(PasswordInput.equals("BulltGyangBatchTwo"))
                        {
                            FirebaseDatabase.getInstance().getReference().child("admin").child("resumeflag").setValue("notworking");
                            MainActivity MainObj=new MainActivity();
                            MainObj.MonthChangeLogic2();
                            dialog.dismiss();
                            Toast.makeText(SubPage3Context,"Logic Run!",Toast.LENGTH_SHORT).show();


                        }
                        else
                        {
                            Toast.makeText(SubPage3Context,"WRONG PASSWORD!",Toast.LENGTH_SHORT).show();

                        }

                    }
                }) //
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Vibrator v = (Vibrator) SubPage3Context.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(20);
                        // TODO
                        dialog.dismiss();
                    }
                });
        builder.show();




    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setDetails(String college, String contact, String email, String endsub,
                           String groupid, String name, String qrcode,String scanneddinner,String scannedlunch)
    {

        this.name=name;
        this.email=email;
        this.college=college;
        this.contact=contact;
        this.groupid=groupid;
        this.qrcode=qrcode;

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStart() {
        super.onStart();

       /* UserNameTxtView=(TextView)rootView3.findViewById(R.id.userName001);
        UserNameTxtView.setText(name);*/
        Log.v("E_VALUE","IN ONSTART : "+name);

    }

    @Override
    public void onResume() {
        super.onResume();
        /*UserNameTxtView=(TextView)rootView3.findViewById(R.id.userName001);
        UserNameTxtView.setText(UserDataObj.getName());*/
        Log.v("E_VALUE","IN RESUME : "+UserDataObj.getName());


    }

   /* void updateNameTextView(String Text)
    {
        TextView t = (TextView)rootView3.findViewById(R.id.userName001);
        t.setText(Text);
    }*/


    public void showAdminPassDialogMethod() {


        final String[] curr1 = new String[1];
        final String[] curr2 = new String[1];



        AlertDialog.Builder builder = new AlertDialog.Builder(SubPage3Context);


        final EditText input = new EditText(SubPage3Context);

        builder.setView(input);
        builder.setTitle("ENTER CREDENTIALS") //
                .setMessage("Enter Admin Root Password") //
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Vibrator v = (Vibrator) SubPage3Context.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(20);
                        String PasswordInput = input.getText().toString();
                        if(PasswordInput.equals("UpdateBatchOne"))
                        {
                            FirebaseDatabase.getInstance().getReference().child("batch1").child("current")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            curr1[0] =dataSnapshot.getValue().toString();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                            Toast.makeText(SubPage3Context,"Logic Run 1! "+curr1[0],Toast.LENGTH_SHORT).show();

                            MainActivity MainObj=new MainActivity();
                            MainObj.initiatelogic(curr1[0]);
                            dialog.dismiss();

                        }
                        else if(PasswordInput.equals("UpdateBatchTwo"))
                        {

                            FirebaseDatabase.getInstance().getReference().child("batch2").child("current")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            curr2[0] =dataSnapshot.getValue().toString();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                            Toast.makeText(SubPage3Context,"Logic Run 2! "+curr2[0],Toast.LENGTH_SHORT).show();


                            MainActivity MainObj=new MainActivity();
                            MainObj.initiatelogic(curr2[0]);
                            dialog.dismiss();

                        }
                        else
                        {
                            Toast.makeText(SubPage3Context,"WRONG PASSWORD!",Toast.LENGTH_SHORT).show();

                        }

                    }
                }) //
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Vibrator v = (Vibrator) SubPage3Context.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(20);
                        // TODO
                        dialog.dismiss();
                    }
                });
        builder.show();

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) SubPage3Context.getSystemService(SubPage3Context.CONNECTIVITY_SERVICE);
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
