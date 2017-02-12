package com.paulo.joao.mymovies.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;

import com.paulo.joao.mymovies.model.repository.MoviesRepository;

/**
 * Created by joao.paulo.d.ribeiro on 08/02/2017.
 */

public class Utils {

    public static String formatYear(String year){
        return " (" + year + ")";
    }

    public static String formatUrlParam(String param){
        String returnString = " ";
        for (int i = 0; i < param.length(); i++) {
            if (param.charAt(i) <= ' ') {

            } else {
                returnString.concat(String.valueOf(param.charAt(i)));
            }
        }
        return null;
    }

    public static void setStatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(color);
        }
    }
}
