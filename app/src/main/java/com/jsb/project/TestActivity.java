package com.jsb.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;



public class TestActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ConstraintLayout root = findViewById(R.id.root);
        final FloatingButton fb = findViewById(R.id.floatingBtn);
        fb.setState(2);



        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable[] runnable = new Runnable[1];
        final Handler popEffectHandler = new Handler(Looper.getMainLooper());
        final Runnable[] popEffectRunnable = new Runnable[1];
        fb.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                fb.setState(1);
                fb.setState(4);
             //   v.startAnimation(scale);


                final int[] pressTimeElapsed = new int[1];
                pressTimeElapsed[0] = 1;
                popEffectRunnable[0] = new Runnable() {
                    @Override
                    public void run() {
                        if(pressTimeElapsed[0] < 15) {
                            fb.setTime(pressTimeElapsed[0]++);
                            popEffectHandler.postDelayed(this, 10);
                        }
                    }
                };
                popEffectHandler.postDelayed(popEffectRunnable[0], 10);

//                final int[] finalProgress = new int[1];
//                finalProgress[0] = 0;
//
//                runnable[0] = new Runnable() {
//                    @Override
//                    public void run() {
//                        fb.setProgress(finalProgress[0]++);
//                        if(fb.progress < 100) {
//                            handler.postDelayed(this, 15);
//                        }
//
//                    }
//                };
//
//
//
//                handler.postDelayed(runnable[0], 15);

                return false;
            }
        });











//
//
//
//

        fb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    {

                        fb.setState(3);
                        //fb.startAnimation(scale);
                    }
                        // PRESSED
                        break;
                    case MotionEvent.ACTION_UP:{
                        if(runnable[0] != null) {
//                            finalProgress[0] = 0;
                            fb.setProgress(0);
                            handler.removeCallbacks(runnable[0]);
                        }
                        if(popEffectRunnable[0] != null) {
                            fb.setTime(0);
                            popEffectHandler.removeCallbacks(popEffectRunnable[0]);
                        }
                        fb.setState(2);
                       // fb.startAnimation(animation1);
                    }
                    case MotionEvent.ACTION_CANCEL:
                        // RELEASED

                        break;


                }
                return false;
            }
        });






    }
}

