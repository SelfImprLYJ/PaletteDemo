package com.tangramdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void palettle(View view) {
        startActivity(new Intent(this,PalettleActivity.class));
    }

    public void tablayout(View view) {
        startActivity(new Intent(this,TablayoutActivity.class));
    }
}
