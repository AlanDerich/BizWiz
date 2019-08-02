package com.example.derich.bizwiz;

import com.example.derich.bizwiz.credentials.LoginActivity;

public class PreferenceHelper {

    final public static String KEY_USERNAME = "Demo Name";


    public static void setUsername(String value) {
        LoginActivity.sharedPreferences.edit().putString(KEY_USERNAME,value).commit();
    }

    public static String getUsername() {
        return LoginActivity.sharedPreferences.getString(KEY_USERNAME, "");
    }
}
