package com.islam.talleringo.utils;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.islam.talleringo.activities.LoginActivity;
import com.islam.talleringo.activities.MainActivity;

public class utils {
    public static final int GOOGLE_SIGN_IN_FLAG = 901;

    public static Intent  updateUI(FirebaseUser user, Context context){
        Intent intent;
        if (user != null){
            intent = goTo(context, MainActivity.class);
        }else {
            intent = goTo(context, LoginActivity.class);
        }
        return  intent;
    }
    public static Intent  goTo( Context context, Class to){
        return new Intent(context, to);
    }

    public static void signOut(){
        FirebaseAuth.getInstance().signOut();
    }
}
