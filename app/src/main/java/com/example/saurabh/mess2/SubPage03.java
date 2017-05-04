package com.example.saurabh.mess2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

import static com.example.saurabh.mess2.MainActivity.UserDataObj;

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
    private String name,email,groupid,contact,college;
    private HashMap<String, Object> UserInfoMap= new HashMap<>();
    private DatabaseReference mCurrentUser;
    private boolean connected=false;


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
        mAddGrpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  if(isConnected()) {

                    Vibrator v = (Vibrator) SubPage3Context.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(20);
                    final AddFriendHandler mAddFriendHandler = new AddFriendHandler(SubPage3Context);

                    mAddFriendHandler.showDialogMethod();
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

    /*private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService();
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected=true;
            return true;
        }
        else
            connected = false;
        return false;
    }*/

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

    public void setDetails(String age, String college, String contact, String email,
                           String groupid, String name, String qrcode)
    {

        this.name=name;
        this.email=email;
        this.college=college;
        this.contact=contact;
        this.groupid=groupid;

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

        UserNameTxtView=(TextView)rootView3.findViewById(R.id.userName001);
        UserNameTxtView.setText(name);
        Log.v("E_VALUE","IN ONSTART : "+name);

    }

    @Override
    public void onResume() {
        super.onResume();
        UserNameTxtView=(TextView)rootView3.findViewById(R.id.userName001);
        UserNameTxtView.setText(UserDataObj.getName());
        Log.v("E_VALUE","IN RESUME : "+UserDataObj.getName());


    }

    void updateNameTextView(String Text)
    {
        TextView t = (TextView)rootView3.findViewById(R.id.userName001);
        t.setText(Text);
    }
}
