package com.jsb.project;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
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
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FloatingWindow extends Service {

    WindowManager wm;
    LinearLayout ll;
    FloatingButton floatingButton;
    View mDialog;
    View mCncDialog;
    //View fingerAuthentication;
    public static final String CHANNEL_ID = "channel_id";

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




        floatingButton = new FloatingButton(this);
        floatingButton.setState(2);
        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable[] runnable = new Runnable[1];
        final Handler popEffectHandler = new Handler(Looper.getMainLooper());
        final Runnable[] popEffectRunnable = new Runnable[1];
        floatingButton.setOnLongClickListener(
                new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                floatingButton.setState(1);

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

                               floatingButton.setState(5);
                               colorAnimation(floatingButton);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    wm.removeView(ll);
                                    String message= "Get Help";
                                    String id= "Main Channel";
                                    if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.O){
                                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                        CharSequence name="Channel";
                                        String description="D_Channel";
                                        int importance=NotificationManager.IMPORTANCE_HIGH;
                                        NotificationChannel notificationChannel = new NotificationChannel(id,name,importance);
                                        notificationChannel.setName(name);
                                        notificationChannel.setDescription(description);
                                        notificationChannel.enableLights(true);
                                        notificationChannel.setLightColor(Color.WHITE);
                                        notificationChannel.enableVibration(false);

                                        if(notificationManager != null){
                                            notificationManager.createNotificationChannel(notificationChannel);
                                        }

                                    }

                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),id)
                                            .setSmallIcon(R.drawable.logo)
                                            .setContentTitle("New Notification")
                                            .setContentText(message)
                                            .setAutoCancel(true)
                                            .setDefaults(Notification.DEFAULT_SOUND)
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                            .setOngoing(true);

                                    Intent i = new Intent(FloatingWindow.this,UpWindow.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    PendingIntent pendingIntent = PendingIntent.getActivity(FloatingWindow.this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
                                    builder.setContentIntent(pendingIntent);

                                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(FloatingWindow.this);
                                    notificationManagerCompat.notify(1000,builder.build());
                                }
                            }, 3000);


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

                        wm.updateViewLayout(ll,updatepar);

                    default:
                        break;
                }
                return false;
            }
        });


        ViewGroup.LayoutParams butnparams = new ViewGroup.LayoutParams(
                250,250);
        floatingButton.setLayoutParams(butnparams);
        ll.addView(floatingButton);
        wm.addView(ll, params);

    }

    private void colorAnimation(FloatingButton f){
        int startColor=0xffff0000;
        int endColor=0xff000000;
        ValueAnimator colorAnim = ObjectAnimator.ofInt(f,"backgroundColor",startColor,endColor);
        colorAnim.setDuration(500);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(100);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();

    }



    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel";
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        wm.removeView(ll);
    }
}
