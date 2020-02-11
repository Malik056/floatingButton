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
import android.widget.Toast;

import com.thekhaeng.pushdownanim.PushDownAnim;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;
import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_STATIC_DP;


public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ConstraintLayout root = findViewById(R.id.root);
        final FloatingButton fb = findViewById(R.id.floatingBtn);

        final Animation animation= AnimationUtils.loadAnimation(this,R.anim.alpha);
        final Animation animation1= AnimationUtils.loadAnimation(this,R.anim.alpha);
        animation.setInterpolator(new BounceInterpolator());

        fb.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.startAnimation(animation);

                final int[] finalProgress = new int[1];
                finalProgress[0] = 0;
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fb.setProgress(finalProgress[0]++);
                        if(fb.progress < 100) {
                            handler.postDelayed(this, 50);
                        }
                    }
                }, 50);

                return false;
            }
        });




        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                     v.startAnimation(animation1);


            }
        });






    }
}

