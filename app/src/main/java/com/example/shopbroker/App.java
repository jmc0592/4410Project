package com.example.shopbroker;

import android.app.Application;

import com.parse.Parse;

//needed to resolve app crash
public class App extends Application{
    public void onCreate() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);//uses string values from strings.xml
    }
}
