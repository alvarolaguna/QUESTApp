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

    private ArrayList<Zone> zones;

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

        try{
            InputStream is = getResources().openRawResource(R.raw.parklog);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line = "";
            StringTokenizer st = null;
            int row = 0;
            Zone tmp = null;
            while ((line = br.readLine()) != null) {
                st = new StringTokenizer(line, ",");
                if (tmp == null){
                    tmp = new Zone(st.nextToken(),Integer.parseInt(st.nextToken()),Integer.parseInt(st.nextToken()));
                } else {
                    Zone tmp2 = new Zone(st.nextToken(),Integer.parseInt(st.nextToken()),Integer.parseInt(st.nextToken()));
                    if(!tmp.getName().equals(tmp2.getName())){
                        zones.add(tmp);
                        System.out.println(tmp.getName()+" Max "+tmp.getMaxCar()+" Cur "+tmp.getCurCar());
                    }else{
                        tmp2.setCurCar(tmp.getCurCar()+tmp2.getCurCar());
                    }
                    tmp = tmp2;
                }
            }
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("value","csv");
        startActivity(intent);
    }


}
