package com.jsb.project;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class FloatingWindow extends Service {

    WindowManager wm;
//    LinearLayout ll;
    FloatingButton floatingButton;
    View mDialog;
    View mCncDialog;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        // mDialog= new Dialog(this);
        // mCncDialog= new Dialog(this);
//        ll = new LinearLayout(this);
        floatingButton = new FloatingButton(this);
        mDialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupwindow, null, false);
        mCncDialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupcancle,null, false);
//        ll.setBackgroundColor(Color.BLACK);
//        WindowManager.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,
//                0);
//        ll.setLayoutParams(layoutParams);
//        floatingButton.setLayoutParams(layoutParams);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        final WindowManager.LayoutParams params1 = new WindowManager.LayoutParams(
                (Double.valueOf( dm.widthPixels * 0.85)).intValue(), (Double.valueOf( dm.heightPixels * 0.60)).intValue(),
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params1.gravity = Gravity.CENTER;


        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;


        floatingButton.setState(2);

        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable[] runnable = new Runnable[1];
        final Handler popEffectHandler = new Handler(Looper.getMainLooper());
        final Runnable[] popEffectRunnable = new Runnable[1];
        floatingButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                floatingButton.setState(1);


//
//                final int[] pressTimeElapsed = new int[1];
//                pressTimeElapsed[0] = 1;
//                popEffectRunnable[0] = new Runnable() {
//                    @Override
//                    public void run() {
//                        if(pressTimeElapsed[0] < 15) {
//                            floatingButton.setTime(pressTimeElapsed[0]++);
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
                        floatingButton.setProgress(finalProgress[0]++);
                        if(floatingButton.progress < 100) {
                            handler.postDelayed(this, 15);
                        }
                        else{

                            wm.addView(mCncDialog, params1);
//                            wm.addView(ll,params);
//                            ShowpopupCncl();
                        }


                    }
                };



                handler.postDelayed(runnable[0], 15);

                return false;


            }

        });


        floatingButton.setOnTouchListener(new View.OnTouchListener() {

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

                        floatingButton.setState(3);
                        //fb.startAnimation(scale);
                    }
                    // PRESSED
                    break;
                    case MotionEvent.ACTION_UP:{
                        if(runnable[0] != null) {
//                            finalProgress[0] = 0;
                            floatingButton.setProgress(0);
                            handler.removeCallbacks(runnable[0]);
                        }
                        if(popEffectRunnable[0] != null) {
                            floatingButton.setTime(0);
                            popEffectHandler.removeCallbacks(popEffectRunnable[0]);
                        }
                        floatingButton.setState(2);
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

                        wm.updateViewLayout(floatingButton,updatepar);

                    default:
                        break;
                }
                return false;
            }
        });



        ViewGroup.LayoutParams butnparams = new ViewGroup.LayoutParams(
                250,250);
        floatingButton.setLayoutParams(butnparams);




//        ll.addView(floatingButton);/
//        ll.addView(mDialog);
        wm.addView(floatingButton,params);

    }

    public void Showpopup(){


        TextView btncnc;
        btncnc=(TextView) mDialog.findViewById(R.id.cancel);
        btncnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ll.removeView(mDialog);
                Toast.makeText(getApplicationContext(), "Cancel Button Small", Toast.LENGTH_LONG).show();
                Log.d("FloatingButtonCancelButton", "CancelButtonClicked");
            }
        });
//        ll.addView(mDialog);

    }

    public void ShowpopupCncl(){

        TextView btncn;
        Button cancel;
//        mCncDialog.setContentView(R.layout.popupcancle);
        btncn=(TextView) mCncDialog.findViewById(R.id.cancel1);
        cancel = (Button) mCncDialog.findViewById(R.id.cancelbtn);
        btncn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mCncDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Cancel Button Small help screen", Toast.LENGTH_LONG).show();
                Log.d("FloatingButtonCircleCancelButton", "CancelButtonClicked circle cacel btton");

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Showpopup();
                Toast.makeText(getApplicationContext(), "Cancel Button Large", Toast.LENGTH_LONG).show();
                Log.d("FloatingButtonHelpCancelButton", "CancelButtonClicked Help button");
//
            }
        });
//
//       mCncDialog.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
//        wm.removeView(ll);
        wm.removeView(floatingButton);
    }
}
