package com.example.comsense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class SplashScreenActivity extends AppCompatActivity {
        private static final String TAG = SplashScreenActivity.class.getSimpleName();
    private final int SplashTimer = 2000;
    private final boolean splashActive = true;
    private final boolean paused = false;
    private long ms = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        Thread thread = new Thread(){
            public void run() {
                try {
                    while (splashActive && ms < SplashTimer) {
                        if(!paused)
                            ms=ms+100;
                        sleep(100);
                    }
                } catch(Exception e) {
                    Log.e(TAG, String.valueOf(e));
                }
                finally {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();

    }
}