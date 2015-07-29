package com.example.shopbroker;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/*
 * Needed to prevent Parse from being loaded every time an activity is created. Set once so it
 * doesn't cause any issues.
 */
public class App extends Application{
    public void onCreate() {
        Parse.enableLocalDatastore(this);
        //uses string values from strings.xml
        Parse.initialize(this);
        /*
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });*/
    }
}
