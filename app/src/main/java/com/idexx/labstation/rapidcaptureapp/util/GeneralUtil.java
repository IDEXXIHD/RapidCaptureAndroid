package com.idexx.labstation.rapidcaptureapp.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by mhansen on 4/21/2017.
 */
public class GeneralUtil
{
    public static void hideKeyboard(View view)
    {
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
