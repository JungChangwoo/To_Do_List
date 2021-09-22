package org.myapp.mobileprogramming_todolist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new splashHandler(), 5000);
    }

    private class splashHandler implements Runnable {
        public void run(){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }
}
