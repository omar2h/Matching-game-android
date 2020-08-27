package com.example.matchinggame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.os.Handler;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    Button btn1, btn2, btn3, btn4;
    ProgressBar bar;

    int counter;

    ArrayList<Integer> animals = new ArrayList<Integer>();
    ArrayList<Button> btns = new ArrayList<Button>();
    Countdown countdown = new Countdown();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        animals.add(R.drawable.cat);
        animals.add(R.drawable.dog);
        animals.add(R.drawable.cat);
        animals.add(R.drawable.dog);

        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);

        bar =findViewById(R.id.progressBar);


        btns.add(btn1);
        btns.add(btn2);
        btns.add(btn3);
        btns.add(btn4);


        final int catid = animals.get(0);
        int dogid = animals.get(1);


        final MediaPlayer dogmp = MediaPlayer.create(this, R.raw.dog);
        final MediaPlayer catmp = MediaPlayer.create(this, R.raw.cat);



        final int[] clicked = {0};
        final int[] lastClick = {-1};
        final int[] match = {0};
        Collections.shuffle(animals);
        showStartAlert();


        for (int i = 0; i < 4; i++) {
            btns.get(i).setText("cardBack");
            btns.get(i).setTextSize(0.00f);
            final int finalI = i;
            btns.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Button button = (Button) v;

                    if (button.getText().equals("cardBack") && clicked[0] < 2) {
                        Drawable drawable = getDrawable(animals.get(finalI));
                        button.setForeground(drawable);
                        button.setText(animals.get(finalI));
                        if(animals.get(finalI) == catid) {
                            catmp.start();
                        }
                        else{
                            dogmp.start();
                        }
                        if(clicked[0] == 0){
                            lastClick[0] =finalI;
                        }
                        clicked[0]++;
                    }
                    if (clicked[0] == 2) {
                        clicked[0] = 0;
                        for (int j = 0; j < 4; j++) {
                            btns.get(j).setEnabled(false);
                        }
                        new CountDownTimer(1000, 1000){

                            @Override
                            public void onTick(long millisUntilFinished) {

                            }
                            @Override
                            public void onFinish() {
                                if(button.getText().equals(btns.get(lastClick[0]).getText()) ){
                                    match[0]++;
                                    button.setVisibility(View.INVISIBLE);
                                    btns.get(lastClick[0]).setVisibility(View.INVISIBLE);
                                    if(match[0]>1){
                                        showWinAlert();
                                    }

                                }
                                else{
                                    lastClick[0]=-1;
                                }
                                for (int j = 0; j < 4; j++) {
                                    btns.get(j).setForeground(null);
                                    btns.get(j).setText("cardBack");
                                    btns.get(j).setEnabled(true);
                                }


                            }
                        }.start();


                    }

                }
            });
        }

    }

    public boolean showStartAlert(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Match all cards before time expires").setPositiveButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                countdown.execute();

            }
        }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        }).create();
        alert.show();
        return true;
    }

    public boolean showWinAlert(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("You are a Winner\n Score:"+counter+"\nPlay Again?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                countdown.execute();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        }).create();
        alert.show();
        return true;
    }


    public class Countdown extends AsyncTask<Integer, Integer, Integer> {
        long start_ts = 0;
        private Handler handler = new Handler();

        @Override
        protected Integer doInBackground(Integer... integers) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            start_ts = System.currentTimeMillis();
            counter = 20;
            bar.setProgress(counter);
            handler.postDelayed(updateBar, 1000);
        }

        private Runnable updateBar = new Runnable() {
            @Override
            public void run() {
                if (counter > 0) {
                    counter-=2;
                    bar.setProgress(counter);
                    handler.postDelayed(this, 1000);
                }
            }
        };
    };

}