package com.example.saurabh.mess2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UnderConstructionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_under_construction);

        Intent mainIntent1 = getIntent();
        final boolean intValue = mainIntent1.getBooleanExtra("updatefound", false);

        if(intValue)
        {
            showUpdateDialog();
        }

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
    private void showUpdateDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("UPDATE REQUIRED") //
                .setMessage("You are using an old version of the App that's no longer supported." +
                        " Update to get all the latest features.") //
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(20);
                        Uri uri = Uri.parse("http://www.google.com");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);

                    }
                }); //
        this.setFinishOnTouchOutside(false);
        builder.setCancelable(false);
        builder.show();

    }

}
