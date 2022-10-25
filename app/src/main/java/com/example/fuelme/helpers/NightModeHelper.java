package com.example.fuelme.helpers;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;

/*
 * IT19014128
 * A.M.W.W.R.L. Wataketiya
 *
 * This class helps determine the color mode of the app
 * */


public class NightModeHelper {

    //return the current mode of the app as a string
    public static String getMode(Context context){
        int nightModeFlags = context.getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK; //set night mode mask
        String mode;
        switch (nightModeFlags){
            case Configuration.UI_MODE_NIGHT_NO:
                //night mode is not enabled. handle black text
                mode = "light";
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                //night mode is enabled handle white text
                mode = "dark";
                break;
            default:
                //not defined
                mode = "undefined";
                break;
        }

        return mode;
    }
}
