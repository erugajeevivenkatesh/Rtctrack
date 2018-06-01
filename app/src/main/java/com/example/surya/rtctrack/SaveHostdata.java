package com.example.surya.rtctrack;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by SURYA on 8/5/2017.
 */

public class SaveHostdata {
    SharedPreferences pref;
    private SharedPreferences.Editor editor,ranging;

    // Context
    private Context _context;
    private int PRIVATE_MODE = 0;
    public boolean data=false;
    // Sharedpref file name
    private static final String PREFER_NAME = "RoutedetailsPref";
    public static final String Bus = "Busno";
    private static final String IS_USER_LOGIN = "dataissuccess";
    protected static final String RANGE ="ranging";
    private static final String IS_Range = "range";
    // Email address (make variable public to access from outside)
    public static final String Fromm = "From";
    public static final String to = "To";


    public SaveHostdata(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
        ranging=pref.edit();
    }
    public void Savinghost(String BusNo, String From,String To){
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        // Storing name in pref
        editor.putString(Bus, BusNo);
        editor.putString(Fromm, From);
        editor.putString(to, To);
        editor.commit();

    }

    public HashMap<String, String> gethostdetails(){

        //Use hashmap to store user credentials
        HashMap<String, String> route = new HashMap<String, String>();

        // user name
        route.put(Bus, pref.getString(Bus, null));
        route.put(Fromm, pref.getString(Fromm, null));
        route.put(to, pref.getString(to, null));

        return route;
    }
    public boolean dataissuccess(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
    public void logoutUser(){

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();


    }
    public void Savingrange(String range){
        // Storing login value as TRUE
        ranging.putBoolean(IS_Range, true);

        // Storing name in pref
        ranging.putString(RANGE, range);
        ranging.commit();

    }
    public HashMap<String ,String> setrange(){
        HashMap<String, String> rng = new HashMap<String, String>();
        rng.put(RANGE,pref.getString(RANGE,null));
        return rng;
    }



    public boolean isrange(){
        return pref.getBoolean(IS_Range, false);
    }
    public void rangeclear(){
        ranging.clear();
        ranging.commit();
    }

}
