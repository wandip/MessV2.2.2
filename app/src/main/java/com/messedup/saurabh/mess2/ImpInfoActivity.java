package com.messedup.saurabh.mess2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ImpInfoActivity extends AppCompatActivity {


    private TextView mInfoTxtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imp_info);


        mInfoTxtView=(TextView)findViewById(R.id.ImpInfotxtView);
        mInfoTxtView.setText("Points to be noted :\n" +
                "\n" +
                "        \n\nMess is assigned to the user, everyday which is open for lunch and dinner.\n" +
                "        \nA unique QR code is generated for the user when the payment gets confirmed.\n" +
                "\n" +
                "        \n\n'Groups':\n" +
                "        \nWhy groups? : As mess is assigned to you everyday, there's a chance that you could receive another mess than the one of your friend's. To enjoy the day with your friend you could add him in your group so that we assign the same mess !\n" +
                "\n" +
                "        \n\nChanging of groups between the month isn't allowed.Will be made available in the upcoming updates.\n" +
                "        \nMaximum members allowed in a group are 3 as of now!\n" +
                "\n" +
                "        \n\nYou may subscribe for next month at any time you wish. You can add your friends for the next month once they have confirmed their payment as well. Thus your group can be changed every month.\n" +
                "        \nYour friends for this month won't be carry forwarded");
mInfoTxtView.setTextColor(getResources().getColor(android.R.color.background_dark));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
