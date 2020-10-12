package com.example.loginregistrationdemo;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;
    public static final String  IS_REMEMBERME = "isRememberMe";

    public static final String KEY_EMAIL = "email";
    public static final String  KEY_PASSWORD = "password";

    public static final String SESSOIN_REMEMBERME = "rememberMe";

    public SessionManager(Context _context, String seassion){
        context =_context;
        userSession = context.getSharedPreferences(seassion,Context.MODE_PRIVATE);
        editor = userSession.edit();
    }
    public  void  creatRememberMeSession(String email,String password){

        editor.putBoolean(IS_REMEMBERME,true);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_PASSWORD,password);

        editor.commit();

    }
    public HashMap<String,String> getRememberingData(){
        HashMap<String,String> userData = new HashMap<>();

        userData.put(KEY_EMAIL,userSession.getString(KEY_EMAIL,null));
        userData.put(KEY_PASSWORD,userSession.getString(KEY_PASSWORD,null));

        return  userData;
    }
    public  boolean checkRememberMe(){
        if(userSession.getBoolean(SESSOIN_REMEMBERME,false)){
                return  true;
        }else {
            return  false;
        }
    }

}
