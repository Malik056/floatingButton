package com.jsb.project;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class FloatingWindow extends Service {

    WindowManager wm;
    LinearLayout ll;
    FloatingButton openapp;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);


        ll = new LinearLayout(this);
        ll.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(layoutParams);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;
        openapp = new FloatingButton(this);
    //    final FloatingButton fb = findViewById(R.id.floatingBtn);
        openapp.setState(2);


        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable[] runnable = new Runnable[1];
        final Handler popEffectHandler = new Handler(Looper.getMainLooper());
        final Runnable[] popEffectRunnable = new Runnable[1];
        openapp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                openapp.setState(1);


//
//                final int[] pressTimeElapsed = new int[1];
//                pressTimeElapsed[0] = 1;
//                popEffectRunnable[0] = new Runnable() {
//                    @Override
//                    public void run() {
//                        if(pressTimeElapsed[0] < 15) {
//                            openapp.setTime(pressTimeElapsed[0]++);
//                            popEffectHandler.postDelayed(this, 10);
//                        }
//                    }
//                };
//                popEffectHandler.postDelayed(popEffectRunnable[0], 10);

                final int[] finalProgress = new int[1];
                finalProgress[0] = 0;

                runnable[0] = new Runnable() {
                    @Override
                    public void run() {
                        openapp.setProgress(finalProgress[0]++);
                        if(openapp.progress < 100) {
                            handler.postDelayed(this, 15);
                        }

                    }
                };



                handler.postDelayed(runnable[0], 15);

                return false;

            }

        });











//
//
//
//

        openapp.setOnTouchListener(new View.OnTouchListener() {

            WindowManager.LayoutParams updatepar = params;
            double x;
            double y;
            double px;
            double py;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    {

                        openapp.setState(3);
                        //fb.startAnimation(scale);
                    }
                    // PRESSED
                    break;
                    case MotionEvent.ACTION_UP:{
                        if(runnable[0] != null) {
//                            finalProgress[0] = 0;
                            openapp.setProgress(0);
                            handler.removeCallbacks(runnable[0]);
                        }
                        if(popEffectRunnable[0] != null) {
                            openapp.setTime(0);
                            popEffectHandler.removeCallbacks(popEffectRunnable[0]);
                        }
                        openapp.setState(2);
                        // fb.startAnimation(animation1);
                    }
                    case MotionEvent.ACTION_CANCEL:
                        // RELEASED

                        break;


                }
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        x = updatepar.x;
                        y = updatepar.y;

                        px = event.getRawX();
                        py = event.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:

                        updatepar.x = (int) (x+(event.getRawX()-px));
                        updatepar.y = (int) (y+(event.getRawY()-py));

                        wm.updateViewLayout(ll,updatepar);

                    default:
                        break;
                }
                return false;
            }
        });


        //  ImageView openapp = new ImageView(this);
        //openapp.setImageResource(R.mipmap.ic_launcher);
        ViewGroup.LayoutParams butnparams = new ViewGroup.LayoutParams(
                250,250);
        openapp.setLayoutParams(butnparams);

//
//        ColorDrawable[] color = {new ColorDrawable(Color.TRANSPARENT), new ColorDrawable(Color.TRANSPARENT)};
//        TransitionDrawable trans = new TransitionDrawable(color);
//        //This will work also on old devices. The latest API says you have to use setBackground instead.
//        openapp.setBackgroundDrawable(trans);
//        trans.startTransition(5000);


        ll.addView(openapp);
        wm.addView(ll,params);
//
//        openapp.setOnTouchListener(new View.OnTouchListener() {
//
//
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//
//
//                return false;
//
//            }
//        });

//        openapp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent home = new Intent(FloatingWindow.this,MainActivity.class);
//                home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(home);
//            }
//        });



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        wm.removeView(ll);
    }
}
