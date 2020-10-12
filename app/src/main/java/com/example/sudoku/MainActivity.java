package com.example.sudoku;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

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
                            if (completed()) tv.setText("Congrats! I've WON");
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
        boolean correct = true;
        boolean[] seen= new boolean[10];
        int[] seeni = new int[10];
        int[] seenj = new int[10];
        for (int i=0;i<=9; i++) seen[i]=false;

        for (int i=i1; i<i2; i++){
            for(int j= j1; j < j2; j++){
                int value = table[i][j].value;
                if (value!=0) {
                    if (seen[value]){
                        table[seeni[value]][seenj[value]].bt.setTextColor(Color.RED);
                        table[i][j].bt.setTextColor(Color.RED);
                        correct = false;
                    }else {
                        seen[value] = true;
                    }
                    seeni[value] = i;
                    seenj[value] = j;
                }
            }
        }
        return correct;
    }

    boolean correct(){
        boolean correct = true;
        for(int i =0; i<9; i++){
            for (int j=0; j<9; j++){
                Cell cell = table[i][j];
                if (cell.fixed) cell.bt.setTextColor(Color.BLACK);
                else cell.bt.setTextColor(Color.BLUE);
            }
        }
        for (int i=0; i<9; i++)
            if(!correct(i,0, i+1,9)) correct = false;
        for (int j=0; j<9; j++)
            if(!correct(0,j,9,j+1)) correct =  false;
        for(int i =0; i<3; i++){
            for (int j=0; j<3; j++){
                if(!correct(3*i,3*j,3*i+3, 3*j+3)){
                    correct = false;
                }
            }
        }
        return correct;
    }

    Cell[][] table;
    String input;
    String sudoku;
    TableLayout tl;
    TextView tv;
    LinearLayout linlay;
    Button reset;
    int actual = 1;
    boolean finish;
    final String TAG = "SUDOKU ASSETS";
    Button btSave;
    Button btRestore;
    String SudokuSave;
    int save = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        input ="";
        actual = getIntent().getIntExtra("EXTRA_INT", 1);
        save = getIntent().getIntExtra("EXTRA_INT_SAVE", 0);
        SudokuSave = "SudokuSave"+ actual + ".txt";
        readInput();
        Log.d(TAG, input);
        String[] split=input.split("[ ]+");
        tl = new TableLayout(this);
        reset = new Button(this);
        reset.setText("Clear");

        table = new Cell[9][9];
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

        //cargar juego modo easy
        if(save != 0) RestoreAction();
        btSave = new Button(this);
        btSave.setText("SAVE");
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAction();
            }
        });
        btRestore = new Button(this);
        btRestore.setText("RESTORE");
        btRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestoreAction();
            }
        });

        restart(reset);
        tl.setShrinkAllColumns(true);
        tl.setStretchAllColumns(true);
        tv= new TextView(this);
        tv.setTextSize(20);
        linlay= new LinearLayout(this);
        linlay.addView(tl);
        linlay.addView(reset);
        linlay.addView(btSave);
        linlay.addView(btRestore);
        linlay.addView(tv);
        linlay.setOrientation(LinearLayout.VERTICAL);
        linlay.setGravity(Gravity.CENTER_HORIZONTAL);
        setContentView(linlay);
    }

    private void readInput(){
        listAssetSudokus(actual);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open(sudoku)));
            String line;
            while ((line=reader.readLine()) != null) {
                input += line + " ";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean listAssetSudokus(int actual){
        String [] f = new String[0];
        int i = 1;
        try {
            f = getAssets().list("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(String f1 : f){
            if (f1.contains("txt")){
                if (i == actual){
                    sudoku = f1;
                    return true;
                }else i++;
                Log.v("Test names",f1);
            }
        }
        return false;
    }

    private void SaveAction(){
        String text="";
        for (int i = 0; i<9; i++){
            for(int j = 0; j<9; j++){
                Cell cell=table[i][j];
                if (j>0) text+=" ";
                text+=String.valueOf(cell.value);
            }
            text += "\n";
        }
        File file = new File(getFilesDir(),SudokuSave);
        try {
            FileWriter writer= new FileWriter(file);
            writer.append(text);
            writer.flush();
            writer.close();
        } catch (IOException e1){
            e1.printStackTrace();
        }
    }

    private boolean RestoreAction(){
        String text="";
        File file = new File(getFilesDir(),SudokuSave);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line=reader.readLine()) != null){
                text+=line+"\n";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            MessageError();
            return false;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String[] array = text.split("[ \n]+");
        for (int i = 0; i<9; i++) {
            for (int j = 0; j < 9; j++) {
                Cell cell = table[i][j];
                cell.value = Integer.parseInt(array[i * 9 + j]);
                if (cell.value == 0) {
                    cell.bt.setText("");
                } else {
                    cell.bt.setText(String.valueOf(cell.value));
                }
            }
        }
        return true;
    }

    private void restart(Button bt){
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
                        table[i][j].bt.setTextColor(Color.BLACK);
                    }
                }

            }
        });
    }

    private void MessageError(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Caution");
        alertDialog.setMessage("No saved game detected");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "SAVE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SaveAction();
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}