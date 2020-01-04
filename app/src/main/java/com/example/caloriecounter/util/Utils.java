package com.example.caloriecounter.util;

import java.text.DecimalFormat;

public class Utils {
    public  static String formatnumber(int value){
        DecimalFormat formatter=new DecimalFormat("#,###,###");
        return  formatter.format(value);
    }
}
