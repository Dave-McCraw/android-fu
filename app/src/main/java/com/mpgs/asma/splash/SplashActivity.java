package com.mpgs.asma.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.mpgs.asma.catalogue.ItemListActivity;

/*
    This activity just holds the splash image up for a while then moves on.
 */
public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = new Intent(SplashActivity.this, ItemListActivity.class);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY);


    }

}
