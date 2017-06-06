package com.example.saurabh.mess2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaControllerCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.encoder.QRCode;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import static com.example.saurabh.mess2.MainActivity.PAYEMENT_DONE;
import static com.example.saurabh.mess2.MainActivity.QRCODE;
import static com.example.saurabh.mess2.MainActivity.UserDataObj;
import static com.example.saurabh.mess2.R.id.QRCodeImageView;
import static com.example.saurabh.mess2.R.id.start;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubPage02.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubPage02#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubPage02 extends Fragment  {

    private boolean QRCODE_GENERATED_FLAG;
    private int RECEIVED_TOKEN;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View rootView2;
    private ImageView image;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mCurrentUser,mCheckQRCode;  // creating an instance of Firebase Storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    //creating a storage reference. Replace the below URL with your Firebase storage URL.
    StorageReference storageRef = storage.getReferenceFromUrl("gs://messed-up.appspot.com");
    private ProgressDialog mGeneratingQRCode;
    private Bitmap bitmap;
    private String qrcode;
    private Button paybtn;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Context SubPage2Context;

    public SubPage02() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubPage02.
     */
    // TODO: Rename and change types and number of parameters
    public static SubPage02 newInstance(String param1, String param2) {
        SubPage02 fragment = new SubPage02();
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


        }
    }

    public void passContext(Context context)
    {
        SubPage2Context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        rootView2=inflater.inflate(R.layout.fragment_sub_page02, container, false);
        paybtn=(Button)rootView2.findViewById(R.id.PayButton);
        image=(ImageView)rootView2.findViewById(QRCodeImageView);
        mGeneratingQRCode=new ProgressDialog(SubPage2Context);
        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null)
        {
            return rootView2;
        }
        mCurrentUser=FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());
        mCheckQRCode=mCurrentUser.child("qrcode");
        mCheckQRCode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                qrcode= (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.v("E_VALUE","PAYEMENT DONE value in oncreate  : "+PAYEMENT_DONE);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference mCurrentUser=FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());
                DatabaseReference  mCheckQRCode=mCurrentUser.child("qrcode");
                if(QRCODE.equals("default"))
                {

                    Intent PaymentIntent=new Intent(SubPage2Context,PaymentActivity.class);
                    PaymentIntent.putExtra("UserID", mAuth.getCurrentUser().getUid());
                    //startActivityForResult(PaymentIntent,0);
                    SubPage2Context.startActivity(PaymentIntent);

                }
                else if(QRCODE.equals("paid"))
                {
                    onPayClicked();
                }


            }
        });


        if(QRCODE_GENERATED_FLAG)
        {
            image.setImageBitmap(bitmap);

        }



        return rootView2;
    }



    public void onPayClicked() {
        mGeneratingQRCode.setMessage("Generating your QR Code");
        mGeneratingQRCode.show();
        Log.v("E_VALUE","In setqrcodeimg");
        generateQRCodeMethod();

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

    public void setQR(Bitmap bitmap) {
        Log.v("E_VALUE","In subpage2");

    }
    private void generateQRCodeMethod() {


        Log.v("E_VALUE","In generateqrcodemethod");


        MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
        try{
            BitMatrix bitMatrix=multiFormatWriter.encode(UserDataObj.getuid(), BarcodeFormat.QR_CODE,270,270);
            BarcodeEncoder barcodeEncoder =new BarcodeEncoder();
            bitmap=barcodeEncoder.createBitmap(bitMatrix);
           ImageView image=(ImageView)rootView2.findViewById(QRCodeImageView);


            image.setImageBitmap(bitmap);
           String imageencodedString= encodeBitmapAndSaveToFirebase(bitmap);
            decodeimagefromFirebase64(imageencodedString);
            PAYEMENT_DONE=true;
            QRCODE_GENERATED_FLAG=true;
            Log.v("E_VALUE","QRCODE_GENERATED_FLAG value clicked  : "+QRCODE_GENERATED_FLAG);

        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }

    }


    private String encodeBitmapAndSaveToFirebase(Bitmap bitmap) {

        StorageReference imageRef= storageRef.child("qrcodes").child(UserDataObj.getuid()+".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data=baos.toByteArray();
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
               Log.v("E_VALUE","TASK FAILED");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.v("E_VALUE","TASK SUCCESS");
                mGeneratingQRCode.dismiss();
                Uri downloadUrl =taskSnapshot.getDownloadUrl();
                String DOWNLOAD_URL = downloadUrl.getPath();

                Log.v("E_VALUE","DOWNLOAD URL :"+ DOWNLOAD_URL);
                mCurrentUser= FirebaseDatabase.getInstance().getReference().child("users").child(UserDataObj.getuid());
                mCurrentUser.child("qrcode").setValue(downloadUrl.toString());

            }
        });

        return imageEncoded;


    }

    private void decodeimagefromFirebase64(String encodedImage)
    {
        byte[] decodedString=Base64.decode(encodedImage,Base64.DEFAULT);
        Bitmap decodedbyte= BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
        image.setImageBitmap(decodedbyte);
    }

    public void setOnlick() {

        ImageView image=(ImageView)rootView2.findViewById(QRCodeImageView);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("E_VALUE","PAYEMENT DONE value in oncreate  : "+PAYEMENT_DONE);

                onPayClicked();

            }


        });

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

        mAuth.addAuthStateListener(mAuthListener);
        super.onStart();
    }


}
