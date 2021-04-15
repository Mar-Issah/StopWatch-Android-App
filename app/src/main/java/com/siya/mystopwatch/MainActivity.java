package com.siya.mystopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import java.util.Locale;

public class MainActivity extends Activity {

    private int seconds = 0;
    private boolean running;
    private TextView textViewTime;
    private Handler handler;
    private boolean wasRunning;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    // retrieve the values of the variable after it was lost
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        runTimer();             //call the runTimer () method running on a different thread every one second
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        wasRunning = savedInstanceState.getBoolean("wasRunning", wasRunning);
    }

    //if the activity pause due to lack of focus, stop the watch
    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running = true;
    }

    //if it resume start it again if it was previously running
    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    //if on stop then stop the app from running
    @Override
    protected void onStop() {
        super.onStop();
        wasRunning = running;
        running = false;
    }

    //if on start start run the stopwatch
    @Override
    protected void onStart() {
        super.onStart();
        if (wasRunning) {
            running = true;
        }
    }

    private void runTimer() {
        textViewTime = findViewById(R.id.textViewTime);
        //create a new thread for the method
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                //format the seconds into hours, minutes and seconds
                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);

                textViewTime.setText(time);

                if (running) {
                    seconds++;
                }
                //delay by one second
                handler.postDelayed(this, 1000);
            }
        });
    }

    //code to run when each button is clicked
    public void onClickStart(View view) {
        running = true;
    }

    public void onClickStop(View view) {
        running = false;
    }

    public void onClickReset(View view) {
        running = false;
        seconds = 0;
    }
}