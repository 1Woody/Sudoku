package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private class Cell {
        int value;
        boolean fixed;
        Button bt;

        public Cell (int initvalue, Context THIS){
            value = initvalue;
            if (value != 0) fixed = true;
            else fixed = false;

            bt= new Button(THIS);
            bt.setTypeface(Typeface.DEFAULT_BOLD);
            if (fixed) {
                bt.setText(String.valueOf(value));
                bt.setTextSize(18);
            }
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!finish){
                        if (fixed) return;
                        else bt.setTextColor(Color.BLUE);
                        value++;
                        if(value>9) value=1;
                        bt.setText(String.valueOf(value));
                        if (correct()) {
                            tv.setText("");
                        } else if(completed()){
                            tv.setText("Congrats! I've WON");
                        } else{
                            tv.setText("There's a repeated digit!");
                        }
                    }
                }
            });
        }
    }

    boolean completed(){
        for (int i=0; i<9; i++){
            for (int j = 0; j < 9; j++){
                if (table[i][j].value == 0) return false;
            }
        }
        finish = true;
        return true;
    }

    boolean correct(int i1, int j1, int i2, int j2){
        //extra arrays to detect where are the errors and mark them red
        //when its fixed by the user reset color to black
        boolean[] seen= new boolean[10];
        for (int i=0;i<=9; i++) seen[i]=false;

        for (int i=i1; i<i2; i++){
            for(int j= j1; j < j2; j++){
                int value = table[i][j].value;
                if (value!=0) {
                    if (seen[value]){
                        //table[i][j].bt.setTextColor(Color.RED);
                        return false;
                    }
                    seen[value] = true;
                }
            }
        }
        return true;
    }

    boolean correct(){
        for (int i=0; i<9; i++)
            if(!correct(i,0, i+1,9)) return false;
        for (int j=0; j<9; j++)
            if(!correct(0,j,9,j+1)) return false;
        for(int i =0; i<3; i++)
            for (int j=0; j<3; j++)
                if(!correct(3*i,3*j,3*i+3, 3*j+3))
                    return false;
        return true;
    }


    Cell[][] table;
    String input;
    TableLayout tl;
    TextView tv;
    LinearLayout linlay;
    Button reset;
    boolean finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        input=  "2 9 ? 7 4 3 8 6 1 " +
                "4 ? 1 8 6 5 9 ? 7 " +
                "8 7 6 1 9 2 5 4 3 " +
                "3 8 7 4 5 9 2 1 6 " +
                "6 1 2 3 ? 7 4 ? 5 " +
                "? 4 9 2 ? 6 7 3 8 " +
                "? ? 3 5 2 4 1 8 9 " +
                "9 2 8 6 7 1 ? 5 4 " +
                "1 5 4 9 3 ? 6 7 2 ";

        String[] split=input.split(" ");
        tl = new TableLayout(this);
        reset = new Button(this);
        reset.setText("Restart");

        table = new Cell[9][9];
        //createSudoku(split, table);
        for (int i=0; i<9; i++){
            TableRow tr = new TableRow(this);
            for(int j=0; j<9; j++){
                String s=split[i*9 + j];
                char c = s.charAt(0);
                table[i][j] = new Cell(c=='?'?0:c-'0', this);
                tr.addView(table[i][j].bt);
            }
            tl.addView(tr);
        }
        restart(reset, split);
        tl.setShrinkAllColumns(true);
        tl.setStretchAllColumns(true);
        tv= new TextView(this);
        tv.setTextSize(20);
        linlay= new LinearLayout(this);
        linlay.addView(tl);
        linlay.addView(reset);
        linlay.addView(tv);
        linlay.setOrientation(LinearLayout.VERTICAL);
        linlay.setGravity(Gravity.CENTER_HORIZONTAL);
        setContentView(linlay);
    }

    private void restart(Button bt, final String[] split){
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i<9; i++){
                    for (int j = 0; j<9; j++){
                        if (!table[i][j].fixed){
                            table[i][j].value = 0;
                            table[i][j].bt.setText("");
                            finish = false;
                            tv.setText("");
                        }
                    }
                }
            }
        });
    }
    /*
    private void createSudoku(String[] split, Cell[][] table){
        for (int i=0; i<9; i++){
            TableRow tr = new TableRow(this);
            for(int j=0; j<9; j++){
                String s=split[i*9 + j];
                char c = s.charAt(0);
                table[i][j] = new Cell(c=='?'?0:c-'0', this);
                tr.addView(table[i][j].bt);
            }
            tl.addView(tr);
        }
    }*/
}