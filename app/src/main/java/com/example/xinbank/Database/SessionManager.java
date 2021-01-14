package com.example.xinbank.Database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    //variable
    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_IC = "ic";
    public static final String KEY_IMEI = "imei";
    public static final String KEY_PHONE = "phone";

    public SessionManager(Context _context) {
        context = _context;
        userSession = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public void createLoginSession(String id, String username, String ic, String imei, String phone) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_ID, id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_IC, ic);
        editor.putString(KEY_IMEI, imei);
        editor.putString(KEY_PHONE, phone);

        editor.commit();
    }
    public void createLoginSession(String id, String username) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_ID, id);
        editor.putString(KEY_USERNAME, username);

        editor.commit();
    }

    public HashMap<String, String> getUserDeatilFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_ID, userSession.getString(KEY_ID, null));
        userData.put(KEY_USERNAME, userSession.getString(KEY_USERNAME, null));
        userData.put(KEY_IC, userSession.getString(KEY_IC, null));
        userData.put(KEY_IMEI, userSession.getString(KEY_IMEI, null));
        userData.put(KEY_PHONE, userSession.getString(KEY_PHONE, null));

        return userData;
    }

    public boolean checkLogin() {
        if (userSession.getBoolean(IS_LOGIN, false)) {
            return true;
        } else {
            return false;
        }
    }

    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }
}
