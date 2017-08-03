package com.project.ics.day30servicetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent startIntent=new Intent(this,MyService.class);
        startService(startIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent stopIntent=new Intent(this,MyService.class);
        stopService(stopIntent);
    }
}
