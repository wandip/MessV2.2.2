package com.example.saurabh.mess2;

import android.content.Context;
import android.view.View;

/**
 * Created by saurabh on 3/5/17.
 */

public class ExternalOnClickListener implements View.OnClickListener {

    private Context mContext;

    public ExternalOnClickListener() {
        // keep references for your onClick logic
    }

    @Override public void onClick(View v) {
        // TODO: add code here

        mContext = v.getContext();
        switch (v.getId()) {
            case R.id.QRCodeImageView:
            {

                SubPage02 mSubPage02=new SubPage02();
                mSubPage02.passContext(mContext);
                mSubPage02.onPayClicked();


                break;
            }

            default:
                break; }}

}
