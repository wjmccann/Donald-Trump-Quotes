package com.wjmccann.donaldtrumpquotes;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class spash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);

        new Handler().postDelayed(new Runnable() {

            public void run(){
                Intent intent = new Intent(spash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
