package com.jsb.project;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MessageActivity extends AppCompatActivity {

    //    AlertDialog alert;
    TextView message;
    ActionReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        message = findViewById(R.id.message);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FloatingWindow.BUTTON_DISABLED);
        intentFilter.addAction(FloatingWindow.BUTTON_ENABLED);
        receiver = new ActionReceiver();
        registerReceiver(receiver, intentFilter);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_preferences_for_message_name), MODE_PRIVATE);
        String text = sharedPreferences.getString("message", "Welcome");
        message.setText(text);

        boolean notificationVisible = isNotificationVisible();
        boolean sharedPreferenceValue = sharedPreferences.getBoolean(getString(R.string.is_notification_active), false);

        if (!sharedPreferenceValue || !isNotificationVisible()) {
            start_stop();
            if (notificationVisible != sharedPreferenceValue) {
                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.shared_preferences_for_message_name), MODE_PRIVATE).edit();
                if (notificationVisible) {
                    editor.putBoolean(getString(R.string.is_notification_active), true);
                } else {
                    editor.putBoolean(getString(R.string.is_notification_active), false);
                }
                editor.apply();
            }
        }


    }

    private boolean isNotificationVisible() {

        NotificationManager manager = getSystemService(NotificationManager.class);
        StatusBarNotification[] activeNotifications;
        if (manager != null) {
            activeNotifications = manager.getActiveNotifications();

            for (StatusBarNotification notification : activeNotifications) {
                if (notification.getId() == FloatingWindow.NOTIFICATION_ID) {
                    return true;
                }
            }
        }
        return false;
    }

    public void start_stop() {
        if (checkPermission()) {
            if (!isMyServiceRunning(FloatingWindow.class)) {
                startService(new Intent(MessageActivity.this, FloatingWindow.class));
            }
        } else {
            reqPermission();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            if (checkPermission()) {
                start_stop();
            } else {
                reqPermission();
            }
        }
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this);
        } else {
            return true;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }


    private void reqPermission() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false);
        alertBuilder.setTitle("Screen overlay detected");
        alertBuilder.setMessage("Enable 'Draw over other apps' in your system setting.");
        alertBuilder.setPositiveButton("OPEN SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    Intent intent;
                    intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
//

            }
        });
        alertBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(getApplicationContext(), "This App needs Overlay permission to Work", Toast.LENGTH_LONG).show();
                finish();

            }
        });
        alertBuilder.show();
//        alert = alertBuilder.create();
//        alert.show();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        alert.dismiss();
    }

    private boolean isMyServiceRunning(@SuppressWarnings("SameParameterValue") Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    class ActionReceiver extends BroadcastReceiver {

        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(FloatingWindow.BUTTON_DISABLED)) {
                if (message != null) {
                    message.setText("Hello World");
                }
            } else if (intent.getAction() != null && intent.getAction().equals(FloatingWindow.BUTTON_ENABLED)) {
                if (message != null) {
                    message.setText("Universe Rescued");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
