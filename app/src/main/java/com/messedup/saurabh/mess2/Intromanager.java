package com.messedup.saurabh.mess2;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by saurabh on 4/4/17.
 */

public class Intromanager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    public Intromanager(Context context)
    {
        this.context=context;
        pref=context.getSharedPreferences("first",0);
        editor=pref.edit();
    }

    public void setFirst(boolean isFirst)
    {
        editor.putBoolean("check",isFirst);
        editor.commit();
    }
    public boolean Check()
    {
        return pref.getBoolean("check",true);
    }

    public void setFirst2(boolean isFirst)
    {
        editor.putBoolean("check2",isFirst);
        editor.commit();
    }
    public boolean Check2()
    {
        return pref.getBoolean("check2",true);
    }


}
