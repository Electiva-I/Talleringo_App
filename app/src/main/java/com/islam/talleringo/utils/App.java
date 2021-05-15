package com.islam.talleringo.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.islam.talleringo.database.LiveData.DataViewModel;

public class App extends Application {
    private static App instance;

    public static App getInstance(){  return instance;   }
    public static Context getContext(){ return instance;    }


    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
