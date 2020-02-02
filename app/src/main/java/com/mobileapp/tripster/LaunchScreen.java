package com.mobileapp.tripster;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LaunchScreen extends AppCompatActivity {

    private static final int DELAY_MILLIS = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This loads the splash screen in without title bar (battery etc)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.activity_launch_screen);

        Handler navigationHandler = new Handler();
        navigationHandler.postDelayed(() -> {
            Intent mainActivityIntent = new Intent(LaunchScreen.this, MainActivity.class);
            startActivity(mainActivityIntent);
        }, DELAY_MILLIS);

    }

}
