package com.omdhanwant.permissionsbackgroundtasksmedia;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class SeekBarWithThread extends AppCompatActivity {

    SeekBar mSeekBar;
    int maxCounter = 100;
    TextView counterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seekbar_thread);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mSeekBar = (SeekBar) findViewById(R.id.seekbarcounter);
        counterText = (TextView) findViewById(R.id.tvCounter);
        mSeekBar.setMax(maxCounter);

    }


    boolean isRunning = false;
    int counter = 0;

    // start action
    public void start(View view) {
        isRunning = true;
        SeekBarHandler seekBarHandler = new SeekBarHandler();
        seekBarHandler.start();


    }

    // stop action
    public void stop(View view) {
        isRunning = false;
    }

    class SeekBarHandler extends Thread {


        @Override
        public void run() {

            while (isRunning) {
                if(counter <= maxCounter) {
                    counter += 1;


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSeekBar.setProgress(counter);
                            counterText.setText("Counter: " + counter);
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    isRunning = false;
                }
            }
        }
    }
}
