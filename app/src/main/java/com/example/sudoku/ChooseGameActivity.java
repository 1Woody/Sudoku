package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChooseGameActivity extends AppCompatActivity {

    LinearLayout linlay, linlay2;
    TextView tv,tv2;
    Button bteasy, btmedium, bthard;
    boolean resumestate = false;
    Button btresume, btsave1, btsave2, btsave3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linlay = new LinearLayout(this);
        tv = new TextView(this);
        tv.setText("CHOOSE GAME");
        bteasy = new Button(this);
        bteasy.setText("EASY");
        btmedium = new Button(this);
        btmedium.setText("MEDIUM");
        bthard = new Button(this);
        bthard.setText("HARD");
        btresume = new Button(this);
        btresume.setText("LOAD");

        linlay2 = new LinearLayout(this);
        tv2 = new TextView(this);
        tv2.setText("CHOOSE SAVED GAME");
        tv2.setTextSize(25);
        btsave1 = new Button(this);
        btsave1.setText("SAVE1");
        btsave2 = new Button(this);
        btsave2.setText("SAVE2");
        btsave3 = new Button(this);
        btsave3.setText("SAVE3");
        linlay2.addView(tv2);
        linlay2.addView(btsave1);
        linlay2.addView(btsave2);
        linlay2.addView(btsave3);
        linlay2.setVisibility(View.GONE);
        Design();
        Buttonfunctions();
        tv.setTextSize(30);
        linlay= new LinearLayout(this);
        linlay.addView(tv);
        linlay.addView(bteasy);
        linlay.addView(btmedium);
        linlay.addView(bthard);
        linlay.addView(btresume);
        linlay.addView(linlay2);
        linlay.setOrientation(LinearLayout.VERTICAL);
        linlay.setGravity(Gravity.CENTER_HORIZONTAL);
        linlay2.setOrientation(LinearLayout.VERTICAL);
        linlay2.setGravity(Gravity.CENTER_HORIZONTAL);
        setContentView(linlay);
    }

    public void Buttonfunctions(){
        btresume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!resumestate){
                    linlay2.setVisibility(View.VISIBLE);
                    btresume.setText("BACK");
                } else {
                    linlay2.setVisibility(View.GONE);
                    btresume.setText("LOAD");
                }
                resumestate = !resumestate;
            }
        });
        bteasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseGameActivity.this, MainActivity.class);
                intent.putExtra("EXTRA_INT", 1);
                startActivity(intent);
            }
        });

        btmedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseGameActivity.this, MainActivity.class);
                intent.putExtra("EXTRA_INT", 2);
                startActivity(intent);
            }
        });

        bthard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseGameActivity.this, MainActivity.class);
                intent.putExtra("EXTRA_INT", 3);
                startActivity(intent);
            }
        });
    }

    public void Design(){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 30, 0, 0);
        linlay2.setLayoutParams(params);
        tv.setLayoutParams(params);
        bteasy.setLayoutParams(params);
        btmedium.setLayoutParams(params);
        bthard.setLayoutParams(params);
        btresume.setLayoutParams(params);

    }

}
