package com.example.bijen.onlinedhaba.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bijen.onlinedhaba.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Thread thread = new Thread(runnable);
        thread.start();
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try{
                Thread.sleep(1000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            Intent intent = new Intent(Splash.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
}
