package com.messedup.saurabh.mess2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MessSelectionActivity extends AppCompatActivity implements View.OnClickListener {


    private ToggleButton mMessBtn1;
    private ToggleButton mMessBtn2;
    private ToggleButton mMessBtn3;
    private ToggleButton mMessBtn4;
    private ToggleButton mMessBtn5;
    private ToggleButton mMessBtn6;
    private Button mSelectMessBtn;
    private int COUNT_1,COUNT_2,COUNT_3,COUNT_4,COUNT_5,COUNT_6;
    private int MESS_COUNT;
   private boolean[] MessCheckArray;
    private Button Btnarr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_selection);
        COUNT_1=COUNT_2=COUNT_3=COUNT_4=COUNT_5=COUNT_6=0;
        MESS_COUNT=0;
        mMessBtn1=(ToggleButton)findViewById(R.id.Mess1Btn);
        mMessBtn1.setOnClickListener(this);
        mMessBtn2=(ToggleButton)findViewById(R.id.Mess2Btn);
        mMessBtn2.setOnClickListener(this);
        mMessBtn3=(ToggleButton)findViewById(R.id.Mess3Btn);
        mMessBtn3.setOnClickListener(this);
        mMessBtn4=(ToggleButton)findViewById(R.id.Mess4Btn);
        mMessBtn4.setOnClickListener(this);
        mMessBtn5=(ToggleButton)findViewById(R.id.Mess5Btn);
        mMessBtn5.setOnClickListener(this);
        mMessBtn6=(ToggleButton)findViewById(R.id.Mess6Btn);
        mMessBtn6.setOnClickListener(this);
        mSelectMessBtn=(Button)findViewById(R.id.MessSelectBtn);
        mSelectMessBtn.setOnClickListener(this);
        MessCheckArray=new boolean[7];
        Button mMessBtn[] = new Button[7];




    }


    @Override
    public void onClick(View view) {



        switch (view.getId()) {

            case R.id.Mess1Btn:{
                // do your code
                MessCheckArray[1]=toggleButton(mMessBtn1);
                checkClickable();


                break;
            }


            case R.id.Mess2Btn:{
                // do your code
                MessCheckArray[2]=toggleButton(mMessBtn2);
                checkClickable();

                break;
            }
            case R.id.Mess3Btn:{
                // do your code
                MessCheckArray[3]=toggleButton(mMessBtn3);
                checkClickable();

                break;
            }
            case R.id.Mess4Btn:{
                // do your code
                MessCheckArray[4]=toggleButton(mMessBtn4);
                checkClickable();

                break;
            }
            case R.id.Mess5Btn:{
                // do your code
                MessCheckArray[5]=toggleButton(mMessBtn5);
                checkClickable();

                break;
            }

            case R.id.Mess6Btn:  {
                // do your code
                MessCheckArray[6]=toggleButton(mMessBtn6);
                checkClickable();

                break;
            }

            case R.id.MessSelectBtn:
            {
                if(checkcount())
                    selectMess();
            }
            default:
                break;
        }

    }

    private void selectMess() {

        Toast.makeText(MessSelectionActivity.this,"You Selected :",Toast.LENGTH_SHORT).show();


        for(int i=1;i<7;i++)
        {
            if(MessCheckArray[i])
                Toast.makeText(MessSelectionActivity.this,"Mess "+i,Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkcount() {
        if(MESS_COUNT<4)
        {
            Toast.makeText(MessSelectionActivity.this,"Select "+(4-MESS_COUNT)+" more Mess",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(MESS_COUNT>4)
        {
            Toast.makeText(MessSelectionActivity.this,"MESS_COUNT :"+MESS_COUNT,Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }

    }

    private boolean toggleButton(ToggleButton button) {
        if(button.isChecked())
        {
            MESS_COUNT++;
            return true;

        }
        else
        {
            MESS_COUNT--;
            return false;

        }


    }

    private void checkClickable() {

        if(MESS_COUNT<4)
        {
            mMessBtn1.setClickable(true);
            mMessBtn2.setClickable(true);
            mMessBtn3.setClickable(true);
            mMessBtn4.setClickable(true);
            mMessBtn5.setClickable(true);
            mMessBtn6.setClickable(true);

        }
        else if(MESS_COUNT>=4)
        {
            for(int i=1;i<7;i++)
            {
                if(!MessCheckArray[i]) {
                    switch (i) {
                        case 1: {
                            mMessBtn1.setClickable(false);
                            break;
                        }

                        case 2: {
                            mMessBtn2.setClickable(false);
                            break;
                        }
                        case 3: {
                            mMessBtn3.setClickable(false);
                            break;
                        }
                        case 4: {
                            mMessBtn4.setClickable(false);
                            break;
                        }
                        case 5: {
                            mMessBtn5.setClickable(false);
                            break;
                        }
                        case 6: {
                            mMessBtn6.setClickable(false);
                            break;
                        }

                    }

                }
            }
        }
    }

    private void checkMax() {

        if(MESS_COUNT>=4)
        {
            for(int i=1;i<7;i++)
            {
                if(!MessCheckArray[i])
                    switch (i){
                        case 1:
                            mMessBtn1.setClickable(false);
                        case 2:
                            mMessBtn2.setClickable(false);
                        case 3:
                            mMessBtn3.setClickable(false);
                        case 4:
                            mMessBtn4.setClickable(false);
                        case 5:
                            mMessBtn5.setClickable(false);
                        case 6:
                            mMessBtn6.setClickable(false);

                    }


            }
        }

    }


    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
