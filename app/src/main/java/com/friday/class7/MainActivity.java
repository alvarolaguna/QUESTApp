package com.friday.class7;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void loadMap(View v){

        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }

    public void readCSV(View v){

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("value","csv");
        startActivity(intent);
    }


}
