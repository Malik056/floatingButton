package com.jsb.project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class FloatingWindow extends Service {

    public static final String BUTTON_DISABLED = "Button_Disabled";
    public static final String BUTTON_ENABLED = "Button_Enabled";
    public static final String RESTART_SERVICE = "com.jsb.project.restartService";
    LinearLayout ll;
    WindowManager wm;
    FloatingButton floatingButton;
    public static final String CHANNEL_ID = "com.jsb.project.FloatingWindow#MainChannel";
    static final String idForForegoundSerivceNotificationChannel = "com.jsb.project.FloatingWindow#ForegroundServiceChannel";
    public static int NOTIFICATION_ID = 1000;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
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

        final WindowManager.LayoutParams params;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT);
        } else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT);
        }


        params.gravity = Gravity.START;
        params.x = 0;
        params.y = 0;

        floatingButton = new FloatingButton(this);
        floatingButton.setState(2);
        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable[] runnable = new Runnable[1];
//        final Handler popEffectHandler = new Handler(Looper.getMainLooper());

        floatingButton.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        floatingButton.setState(1);

                        final int[] finalProgress = new int[]{0};
                        runnable[0] = new Runnable() {
                            @Override
                            public void run() {
                                floatingButton.setProgress(finalProgress[0]++);
                                if (floatingButton.progress < 100) {
                                    handler.postDelayed(this, 15);
                                } else {

                                    floatingButton.startFlashAnimation(new OnFlashAnimationEnded() {
                                        @Override
                                        public void onAnimationEnded() {
                                            wm.removeView(ll);
                                            String message = "Get Help";
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                CharSequence name = "Channel";
                                                String description = "D_Channel";
                                                int importance = NotificationManager.IMPORTANCE_HIGH;
                                                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                                                notificationChannel.setName(name);
                                                notificationChannel.setDescription(description);
                                                notificationChannel.enableLights(true);
                                                notificationChannel.setLightColor(Color.WHITE);
                                                notificationChannel.enableVibration(false);

                                                NotificationChannel notificationChannelForForegroundService = new NotificationChannel(idForForegoundSerivceNotificationChannel, name, NotificationManager.IMPORTANCE_MIN);
                                                notificationChannelForForegroundService.setName("FLoatingButtonChannel");
                                                notificationChannelForForegroundService.setDescription("FloatingButton");
                                                notificationChannelForForegroundService.enableLights(true);
                                                notificationChannelForForegroundService.setLightColor(Color.WHITE);
                                                notificationChannelForForegroundService.enableVibration(false);
                                                notificationChannelForForegroundService.setSound(null, null);

                                                if (notificationManager != null) {
                                                    notificationManager.createNotificationChannel(notificationChannel);
                                                    notificationManager.createNotificationChannel(notificationChannelForForegroundService);
                                                }

                                            }

                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                                    .setSmallIcon(R.drawable.logo)
                                                    .setContentTitle("New Notification")
                                                    .setContentText(message)
                                                    .setAutoCancel(false)
                                                    .setDefaults(Notification.DEFAULT_SOUND)
                                                    .setPriority(NotificationCompat.PRIORITY_MAX)
                                                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                                    .setOngoing(true);

//                                            Intent i = new Intent(FloatingWindow.this, UpWindow.class);
//                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                                            Intent intent = new Intent(getApplicationContext(), BiometricAuthActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                            PendingIntent pendingIntent = PendingIntent.getActivity(FloatingWindow.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            builder.setContentIntent(pendingIntent);

                                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(FloatingWindow.this);
                                            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

                                            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences_for_message_name), MODE_PRIVATE).edit();
                                            editor.putBoolean(getString(R.string.is_notification_active), true);
                                            editor.putBoolean("isStoppedByUser", true);
                                            editor.apply();

                                            stopSelf();
                                        }
                                    });

                                }


                            }
                        };

                        handler.postDelayed(runnable[0], 15);
                        return false;
                    }

                });


        floatingButton.setOnTouchListener(new View.OnTouchListener() {

            WindowManager.LayoutParams updateParameter = params;
            double x;
            double y;
            double px;
            double py;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS) {
                    v.performClick();
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        floatingButton.setState(3);
                        //fb.startAnimation(scale);
                    }
                    // PRESSED
                    break;
                    case MotionEvent.ACTION_UP: {
                        if (runnable[0] != null) {
//                            finalProgress[0] = 0;
                            floatingButton.setProgress(0);
                            handler.removeCallbacks(runnable[0]);
                        }
                        floatingButton.setState(2);
                        // fb.startAnimation(animation1);
                    }
                    case MotionEvent.ACTION_CANCEL:
                        // RELEASED

                        break;


                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        x = updateParameter.x;
                        y = updateParameter.y;

                        px = event.getRawX();
                        py = event.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:

                        updateParameter.x = (int) (x + (event.getRawX() - px));
                        updateParameter.y = (int) (y + (event.getRawY() - py));

                        wm.updateViewLayout(ll, updateParameter);

                    default:
                        break;
                }
                return false;
            }

        });

        ViewGroup.LayoutParams btnParams = new ViewGroup.LayoutParams(Float.valueOf(getScreenWidth() * 0.40f).intValue(), Float.valueOf(getScreenWidth() * 0.40f).intValue());
        floatingButton.setLayoutParams(btnParams);
        ll.addView(floatingButton);
        wm.addView(ll, params);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), idForForegoundSerivceNotificationChannel)
//                .setSmallIcon(R.drawable.logo)
//                .setContentTitle("Notification!")
//                .setContentText("Floating button is active!")
//                .setAutoCancel(false)
//                .setPriority(NotificationCompat.PRIORITY_MIN)
//                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
//                .setOngoing(true);
//        try {
//
//            startForeground(2000, builder.build());
//        } catch (Exception ex) {
//
//            ex.printStackTrace();
//        }
//        }
    }


    @Override
    public void onDestroy() {

//        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_for_message_name), MODE_PRIVATE);
//        boolean isStopedByUser = preferences.getBoolean("isStoppedByUser", false);

//        if(!isStopedByUser) {
//            Intent intent = new Intent(getApplicationContext(), FloatingWindow.class);
//            intent.setAction("startMyBackgroundService");
//            sendBroadcast(intent);
//            Toast.makeText(getApplicationContext(), "sentBroadcast", Toast.LENGTH_LONG).show();
//            super.onDestroy();
//            return;
//        }

        try {
            SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.shared_preferences_for_message_name), MODE_PRIVATE).edit();
            editor.putString("message", "Hello World");
            editor.putBoolean("isStoppedByUser", false);
            editor.apply();
            Intent sendMessage = new Intent();
            sendMessage.setAction(FloatingWindow.BUTTON_DISABLED);
            sendBroadcast(sendMessage);
//            stopSelf();
            wm.removeView(ll);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.onDestroy();
    }

    public static float getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }



}
