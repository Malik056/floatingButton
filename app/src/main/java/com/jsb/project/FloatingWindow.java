package com.jsb.project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Gravity;
import android.view.MotionEvent;
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
    LinearLayout ll;
    WindowManager wm;
    FloatingButton floatingButton;
    static final String id = "com.jsb.project.FloatingWindow#MainChannel";
    public static int NOTIFICATION_ID = 1000;


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
                                                NotificationChannel notificationChannel = new NotificationChannel(id, name, importance);
                                                notificationChannel.setName(name);
                                                notificationChannel.setDescription(description);
                                                notificationChannel.enableLights(true);
                                                notificationChannel.setLightColor(Color.WHITE);
                                                notificationChannel.enableVibration(false);

                                                if (notificationManager != null) {
                                                    notificationManager.createNotificationChannel(notificationChannel);
                                                }


                                            }

                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), id)
                                                    .setSmallIcon(R.drawable.logo)
                                                    .setContentTitle("New Notification")
                                                    .setContentText(message)
                                                    .setAutoCancel(false)
                                                    .setDefaults(Notification.DEFAULT_SOUND)
                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                                    .setOngoing(true);

                                            Intent intent = new Intent(getApplicationContext(), BiometricAuthActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                            PendingIntent pendingIntent = PendingIntent.getActivity(FloatingWindow.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            builder.setContentIntent(pendingIntent);

                                            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(FloatingWindow.this);
                                            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

                                            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences_for_message_name), MODE_PRIVATE).edit();
                                            editor.putBoolean(getString(R.string.is_notification_active), true);
                                            editor.putBoolean("isClosedByMe",true);
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

        ViewGroup.LayoutParams btnParams = new ViewGroup.LayoutParams(
                250, 250);
        floatingButton.setLayoutParams(btnParams);
        ll.addView(floatingButton);
        wm.addView(ll, params);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_for_message_name),MODE_PRIVATE);
        boolean isClosedByMe = preferences.getBoolean("isClosedByMe", false);
        if(isClosedByMe) {
            try {
                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.shared_preferences_for_message_name)
                        , MODE_PRIVATE).edit();
                editor.putString("message", "Hello World");
                editor.apply();
                Intent sendMessage = new Intent();
                sendMessage.setAction(FloatingWindow.BUTTON_DISABLED);
                sendBroadcast(sendMessage);
                stopSelf();
                wm.removeView(ll);
            } catch (Exception ignored) {

            }
        }
        else {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:"+getPackageName()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


}
