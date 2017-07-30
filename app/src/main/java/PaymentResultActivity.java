import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

import java.io.ByteArrayOutputStream;

import static com.messedup.saurabh.mess2.MainActivity.PAYEMENT_DONE;
import static com.messedup.saurabh.mess2.MainActivity.UserDataObj;
import static com.messedup.saurabh.mess2.R.id.QRCodeImageView;

/**
 * Created by saurabh on 5/5/17.
 */

public class PaymentResultActivity extends Activity {


    private Bitmap bitmap;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mCurrentUser,mCheckQRCode;
    private ImageView image;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    //creating a storage reference. Replace the below URL with your Firebase storage URL.
    StorageReference storageRef = storage.getReferenceFromUrl("gs://messed-up.appspot.com");

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


        image=(ImageView)findViewById(QRCodeImageView);


    }

    /*private void generateQRCodeMethod() {


        Log.v("E_VALUE","In generateqrcodemethod");


        MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
        try{
            BitMatrix bitMatrix=multiFormatWriter.encode(UserDataObj.getuid(), BarcodeFormat.QR_CODE,270,270);
            BarcodeEncoder barcodeEncoder =new BarcodeEncoder();
            bitmap=barcodeEncoder.createBitmap(bitMatrix);
            ImageView image=(ImageView)findViewById(QRCodeImageView);


            image.setImageBitmap(bitmap);
            String imageencodedString= encodeBitmapAndSaveToFirebase(bitmap);
            decodeimagefromFirebase64(imageencodedString);
            PAYEMENT_DONE=true;


        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }

    }*/
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

                mAuth= FirebaseAuth.getInstance();
                mCurrentUser=FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());
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


    @Override
    public void onStart() {

        mAuth.addAuthStateListener(mAuthListener);
        super.onStart();
    }
}
