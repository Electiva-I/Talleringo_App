package com.islam.talleringo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.islam.talleringo.R;
import com.islam.talleringo.activities.MainActivity;
import com.islam.talleringo.utils.utils;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("TYPE",verifySessionType()+ "");
        if (verifySessionType() == 1) {
            startActivity(utils.updateUI(currentUser, getApplicationContext()));
        }   else if (verifySessionType() == 0){
            startActivity(utils.goTo(getApplicationContext(), MainActivity.class));
        } else {
            startActivity(utils.goTo(getApplicationContext(), LoginActivity.class));
        }
        finish();
    }


    private int verifySessionType() {
        SharedPreferences preferences   = getApplicationContext()
                .getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return preferences.getInt("type", 1);
    }

}