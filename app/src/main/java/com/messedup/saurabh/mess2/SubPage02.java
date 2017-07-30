package com.messedup.saurabh.mess2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
/*
import com.journeyapps.barcodescanner.BarcodeEncoder;
*/
import com.kobakei.ratethisapp.RateThisApp;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static com.messedup.saurabh.mess2.MainActivity.BATCH;
import static com.messedup.saurabh.mess2.MainActivity.BUFFER;
import static com.messedup.saurabh.mess2.MainActivity.CURRENT;
import static com.messedup.saurabh.mess2.MainActivity.PAID_TIME;
import static com.messedup.saurabh.mess2.MainActivity.PAYEMENT_DONE;
import static com.messedup.saurabh.mess2.MainActivity.QRCODE;
import static com.messedup.saurabh.mess2.MainActivity.UserDataObj;

import static com.messedup.saurabh.mess2.R.id.QRCodeImageView;
import static com.messedup.saurabh.mess2.R.id.dark;
/*
import static com.example.saurabh.mess2.UpdateStrings.BUFFER;
*/

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
    private DatabaseReference mGroupDatabase,mInGroupUserGrp;
    private Bitmap bitmap;
    private String qrcode;
    private Button UpcomingMonthBtn;
    public static Map<String,String> timeMap;


    private boolean connected;


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
        UpcomingMonthBtn=(Button)rootView2.findViewById(R.id.PayNextBtn);
        image=(ImageView)rootView2.findViewById(QRCodeImageView);
        mGeneratingQRCode=new ProgressDialog(SubPage2Context);
        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null)
        {
            return rootView2;
        }



       /* FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("batch").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String Bat=dataSnapshot.getValue().toString();
                Log.v("E_VALUE","BATCH : "+Bat);
                if(Bat.equals("not paid"))
                {
                    SimpleDateFormat dateFormat=new SimpleDateFormat("dd MM yyyy");
                  //  Date dateToday=dateFormat.parse()
                    *//*Calendar c=Calendar.getInstance();
                    String formatdate=dateFormat.format(c.getTime());*//*

                    String newString=new SimpleDateFormat("dd MM yyyy").format(new Date());
                    try {
                        Date cellDate=dateFormat.parse(newString);

                        checkDate2(cellDate);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else if(Bat.equals("batch1"))
                {
                    setBatch1();
                    Log.v("E_VALUE"," IN BATCH 1 : "+Bat);

                }
                else if(Bat.equals("batch2"))
                {
                    Log.v("E_VALUE"," IN BATCH 2 : "+Bat);

                    setBatch2();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/

        // Monitor launch times and interval from installation
        RateThisApp.onCreate(SubPage2Context);
        // If the condition is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(SubPage2Context);

        // Custom condition: 3 days and 10 launches
        final RateThisApp.Config config = new RateThisApp.Config(7,30);

        config.setTitle(R.string.my_own_title);
        config.setMessage(R.string.my_own_message);
        config.setYesButtonText(R.string.my_own_rate);
        config.setNoButtonText(R.string.my_own_thanks);
        config.setCancelButtonText(R.string.my_own_cancel);

        config.setUrl("https://play.google.com/store/apps/details?id=com.messedup.saurabh.mess2"); //change if want to override

        RateThisApp.init(config);

        RateThisApp.setCallback(new RateThisApp.Callback() {
            @Override
            public void onYesClicked() {
                Vibrator v = (Vibrator) SubPage2Context.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(20);
            }

            @Override
            public void onNoClicked() {
                Vibrator v = (Vibrator) SubPage2Context.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(20);            }

            @Override
            public void onCancelClicked() {
                Vibrator v = (Vibrator) SubPage2Context.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(20);            }
        });




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


        /*final TextView TodaysMessTxt=(TextView)rootView2.findViewById(R.id.TodaysMessTxtView);

        TodaysMessTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTutorialHandler.cleanUp();
                mTutorialHandler.setToolTip(new ToolTip().setTitle("Hey there!").setDescription("Just the middle man").setGravity(Gravity.BOTTOM|Gravity.LEFT)).playOn(image);

            }
        });



*/



        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               // mTutorialHandler.cleanUp();
               // mTutorialHandler.setToolTip(new ToolTip().setTitle("Hey...").setDescription("It's time to say goodbye").setGravity(Gravity.TOP|Gravity.RIGHT)).playOn(UpcomingMonthBtn);


                DatabaseReference mCurrentUser=FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());
                DatabaseReference  mCheckQRCode=mCurrentUser.child("qrcode");

                if(QRCODE.equals("paid")&&PAID_TIME.equals("not paid"))
                {
                    if(isConnected())
                    {
                        Toast.makeText(SubPage2Context,"Generating QR CODE Please Wait!",Toast.LENGTH_SHORT).show();

                        Vibrator v = (Vibrator) SubPage2Context.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(20);

                        String from="Main";


                        setbatch("Main");
                        if(from.equals("Main")) {
                            mGeneratingQRCode.setMessage("Generating your QR Code");
                            mGeneratingQRCode.show();
                            Log.v("E_VALUE", "In setqrcodeimg");


                            generateQRCodeMethod();
                        }


                    }
                    else
                    {
                        Vibrator v = (Vibrator) SubPage2Context.getSystemService(Context.VIBRATOR_SERVICE);
                        long[] pattern = {0, 75,100,75};

                        // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
                        v.vibrate(pattern, -1);
                        Toast.makeText(SubPage2Context,"No Internet! Please Check your Connection",Toast.LENGTH_LONG).show();
                    }

                }
                else if(PAID_TIME.equals("not paid"))
                {
                    Intent PaymentIntent=new Intent(SubPage2Context,PaymentActivity.class);
                    PaymentIntent.putExtra("UserID", mAuth.getCurrentUser().getUid());
                    //startActivityForResult(PaymentIntent,0);
                    SubPage2Context.startActivity(PaymentIntent);

                }


            }
        });


        if(QRCODE_GENERATED_FLAG)
        {
            image.setImageBitmap(bitmap);

        }


        UpcomingMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  mTutorialHandler.cleanUp();
                Intent NextMonthIntent=new Intent(SubPage2Context,NextMonthActivity.class);
               // NextMonthIntent.putExtra("UserID", mAuth.getCurrentUser().getUid());
                //startActivityForResult(PaymentIntent,0);
                SubPage2Context.startActivity(NextMonthIntent);

            }
        });


       // mTutorialHandler.playOn(TodaysMessTxt);
        return rootView2;
    }

    private void setBatch1() {

        FirebaseDatabase.getInstance().getReference().child("admin").child("batch1start")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String batchuser = dataSnapshot.getValue().toString();
                        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

                        try {
                            Date b1 = myFormat.parse(batchuser);

                            UpcomingMonthBtn.setText("Pay for the month starting from " + b1);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void setBatch2() {

        FirebaseDatabase.getInstance().getReference().child("admin").child("batch2start")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String batchuser = dataSnapshot.getValue().toString();
                        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

                        try {
                            Date b1 = myFormat.parse(batchuser);

                            UpcomingMonthBtn.setText("Pay for the month starting from " + b1);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    public void setbatch(final String from) {

        timeMap=ServerValue.TIMESTAMP;

        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {

            FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("paidtime").setValue(timeMap);
        }


        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("paidtime").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                SimpleDateFormat dateFormat=new SimpleDateFormat("dd MM yyyy");

                Date paidDate=new Date(((Long) dataSnapshot.getValue()));

                checkDate(paidDate,from);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


       // onPayClicked();

    }

    private void checkDate(final Date paidDate, final String from) {

        final String[] batch2start = {null};
        final String[] batch1start = {null};

        FirebaseDatabase.getInstance().getReference().child("admin").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                if(s==null)
                {
                  batch1start[0] = dataSnapshot.getValue().toString();
                    Log.v("E_VALUE", "BATCH 1 START "+ batch1start[0]);

                }
                else if(s.equals("batch1start"))
                {
                    batch2start[0] =dataSnapshot.getValue().toString();
                    Log.v("E_VALUE", "BATCH 2 START "+ batch2start[0]);

                }


                if(batch1start[0]!=null&&batch2start[0]!=(null)) {
                    updateBatch(batch1start[0], batch2start[0], paidDate,from);
                    batch1start[0]=null;
                    batch2start[0]=null;
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {



                if(s==null)
                {
                    batch1start[0]= dataSnapshot.getValue().toString();
                    Log.v("E_VALUE", "BATCH 1 START "+batch1start[0]);


                }
                else if(s.equals("batch1start"))
                {
                    batch2start[0]=dataSnapshot.getValue().toString();
                    Log.v("E_VALUE", "BATCH 2 START "+batch2start[0]);

                }


                if(batch1start[0]!=null&&batch2start[0]!=(null))
                {
                    updateBatch(batch1start[0], batch2start[0], paidDate,from);
                    batch1start[0]=null;
                    batch2start[0]=null;

                }




            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void updateBatch(String batch1start, String batch2start, Date paidDate,String from) {


        SimpleDateFormat myFormat=new SimpleDateFormat("dd MM yyyy");

        try {
            Date b1=myFormat.parse(batch1start);
            Date b2=myFormat.parse(batch2start);

            long diff1=b1.getTime()-paidDate.getTime();
            long diff2=b2.getTime()-paidDate.getTime();



            if(diff1<diff2)
            {
                BATCH="batch1";


                Log.v("E_VALUE","SUB PAGE BATCH"+BATCH);


                FirebaseDatabase.getInstance().getReference().child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("batch").setValue("batch1");
                onPayClicked(from);
            }
            else
            {
                BATCH="batch2";

                FirebaseDatabase.getInstance().getReference().child("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("batch").setValue("batch2");
                onPayClicked(from);
            }



        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
    private void checkDate2(final Date paidDate) {


        Log.v("E_VALUE"," IN CheckDate2 1 : ");

        final String[] batch2start = {null};
        final String[] batch1start = {null};

        FirebaseDatabase.getInstance().getReference().child("admin").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                if(s==null)
                {
                    batch1start[0] = dataSnapshot.getValue().toString();
                    Log.v("E_VALUE", "BATCH 1 START "+ batch1start[0]);

                }
                else if(s.equals("batch1start"))
                {
                    batch2start[0] =dataSnapshot.getValue().toString();
                    Log.v("E_VALUE", "BATCH 2 START "+ batch2start[0]);

                }


                if(batch1start[0]!=null&&batch2start[0]!=(null)) {
                    updateBatch2(batch1start[0], batch2start[0], paidDate);
                    batch1start[0]=null;
                    batch2start[0]=null;
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {



                if(s==null)
                {
                    batch1start[0]= dataSnapshot.getValue().toString();
                    Log.v("E_VALUE", "BATCH 1 START "+batch1start[0]);


                }
                else if(s.equals("batch1start"))
                {
                    batch2start[0]=dataSnapshot.getValue().toString();
                    Log.v("E_VALUE", "BATCH 2 START "+batch2start[0]);

                }


                if(batch1start[0]!=null&&batch2start[0]!=(null))
                {
                    updateBatch2(batch1start[0], batch2start[0], paidDate);
                    batch1start[0]=null;
                    batch2start[0]=null;

                }




            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void updateBatch2(String batch1start, String batch2start, Date paidDate) {


        Log.v("E_VALUE"," IN CheckDate2 1 : ");
        Log.v("E_VALUE"," IN CheckDate2 batch1start : "+batch1start);
        Log.v("E_VALUE"," IN CheckDate2 batch2start : "+batch2start);


        SimpleDateFormat myFormat=new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date b1=myFormat.parse(batch1start);
            Date b2=myFormat.parse(batch2start);

            long diff1=b1.getTime()-paidDate.getTime();
            long diff2=b2.getTime()-paidDate.getTime();



            if(diff1<diff2)
            {
                //BATCH="batch1";



                UpcomingMonthBtn.setText("Pay for the month starting "+b1);


                Log.v("E_VALUE","SUB PAGE BATCH"+BATCH);


            }
            else
            {
                //BATCH="batch2";
                UpcomingMonthBtn.setText("Pay for the month starting "+b2);


            }



        } catch (ParseException e) {
            e.printStackTrace();
        }


    }



    public void onPayClicked(final String from) {


        DatabaseReference mBatchDatabase = FirebaseDatabase.getInstance().getReference().child(BATCH);

        mBatchDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                CURRENT = dataSnapshot.child("current").getValue().toString();
                BUFFER = dataSnapshot.child("buffer").getValue().toString();
                updateGrpQRCode(from);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    private void updateGrpQRCode(String from) {


        Log.v("E_VALUE","BUFFER 1 STRING VALUE==="+BUFFER);


        if(BUFFER!=null) {


            Log.v("E_VALUE","BUFFER STRING VALUE==="+BUFFER);

            mGroupDatabase = FirebaseDatabase.getInstance().getReference().child(BUFFER);
            mInGroupUserGrp = mGroupDatabase.push();
            mInGroupUserGrp.child("memberid").child(UserDataObj.getuid()).setValue("100");
            mInGroupUserGrp.child("todaysmess").setValue(".............");

            DatabaseReference mInCurUser = FirebaseDatabase.getInstance().getReference().child("users").child(UserDataObj.getuid());


            mInCurUser.child("buffgroupid").setValue(mInGroupUserGrp.getKey());

            //  Long endsubvalue=Long.parseLong("56");
            mInCurUser.child("endsub").setValue("56");

            if(from.equals("Main")) {
                mGeneratingQRCode.setMessage("Generating your QR Code");
                mGeneratingQRCode.show();
                Log.v("E_VALUE", "In setqrcodeimg");


                generateQRCodeMethod();
            }
        }
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
        /*try{
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
        }*/
        try {
            bitmap = TextToImageEncode(UserDataObj.getuid());

            ImageView image=(ImageView)rootView2.findViewById(QRCodeImageView);


            image.setImageBitmap(bitmap);

            String imageencodedString= encodeBitmapAndSaveToFirebase(bitmap);

            decodeimagefromFirebase64(imageencodedString);
            PAYEMENT_DONE=true;
            QRCODE_GENERATED_FLAG=true;
            Log.v("E_VALUE","QRCODE_GENERATED_FLAG value clicked  : "+QRCODE_GENERATED_FLAG);



        } catch (WriterException e) {
            e.printStackTrace();
        }


    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    500, 500, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        SubPage2Context.getResources().getColor(R.color.Black):SubPage2Context.getResources().getColor(R.color.BackgroundWhiteColor1);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
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
                Uri downloadUrl =taskSnapshot.getDownloadUrl();
                String DOWNLOAD_URL = downloadUrl.getPath();
                mGeneratingQRCode.dismiss();

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

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) SubPage2Context.getSystemService(SubPage2Context.CONNECTIVITY_SERVICE);
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
