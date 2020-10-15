package com.example.sudoku;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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
    Button  btload, bteasy, btmedium, bthard;
    boolean loadstate = false;

    private class ButtonMenu{
        Button bt;
        int actual;
        public ButtonMenu(String text, final int type, int actualin, Context THIS){
            actual = actualin;
            bt = new Button(THIS);
            bt.setText(text);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean start = true;
                    Intent intent = new Intent(ChooseGameActivity.this, MainActivity.class);
                    intent.putExtra("EXTRA_INT", actual);
                    if (type == 1) {
                        if (listSavedSudokus(actual)) intent.putExtra("EXTRA_INT_SAVE", actual);
                        else start = false;
                    }
                    if (start) startActivity(intent);
                    else MessageError();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] listnames = {"EASY","MEDIUM","HARD","SAVE1","SAVE2","SAVE3"};
        linlay = new LinearLayout(this);
        linlay2 = new LinearLayout(this);
        tv = new TextView(this);
        tv.setText("CHOOSE GAME");
        tv2 = new TextView(this);
        tv2.setText("SAVED GAMES");
        tv2.setTextSize(25);
        btload = new Button(this);
        btload.setText("LOAD");

        linlay2.addView(tv2);
        linlay2.setVisibility(View.GONE);
        //Design();
        ButtonLoad();
        tv.setTextSize(30);
        linlay= new LinearLayout(this);
        linlay.addView(tv);
        for (int i=0, j=3; i<3; i++,j++){
            linlay.addView((new ButtonMenu(listnames[i],0,i+1,this)).bt);
            linlay2.addView((new ButtonMenu(listnames[j],1,i+1,this)).bt);
        }
        linlay.addView(btload);
        linlay.addView(linlay2);
        linlay.setOrientation(LinearLayout.VERTICAL);
        linlay.setGravity(Gravity.CENTER_HORIZONTAL);
        linlay2.setOrientation(LinearLayout.VERTICAL);
        linlay2.setGravity(Gravity.CENTER_HORIZONTAL);
        setContentView(linlay);
    }

    public void ButtonLoad(){
        btload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loadstate){
                    linlay2.setVisibility(View.VISIBLE);
                    btload.setText("BACK");
                } else {
                    linlay2.setVisibility(View.GONE);
                    btload.setText("LOAD");
                }
                loadstate = !loadstate;
            }
        });
    }

    private boolean listSavedSudokus(int actual){
        String [] f;
        String SudokuSave = "SudokuSave"+ actual + ".txt";
        f = getFilesDir().list();
        assert f != null;
        for(String f1 : f){
            if (f1.contains(SudokuSave)){
                Log.v("Test names",f1);
                return true;
            }
        }
        return false;
    }


    private void MessageError(){
        AlertDialog alertDialog = new AlertDialog.Builder(ChooseGameActivity.this).create();
        alertDialog.setTitle("Caution");
        alertDialog.setMessage("No saved game detected");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    // Not implemented (still have to mix-it with the MenuButton Class)
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
        btload.setLayoutParams(params);
    }
}
