package com.paulo.joao.mymovies.util;

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
}
