package com.example.fuelme.helpers;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;

public class NightModeHelper {

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
