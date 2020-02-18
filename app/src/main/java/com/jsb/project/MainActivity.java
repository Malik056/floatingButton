package com.jsb.project;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;

public class MainActivity extends AppCompatActivity {


    AlertDialog alert;
    boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BiometricManager biometricManager = BiometricManager.from(getApplicationContext());
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                Toast.makeText(getApplicationContext(), "This Device Does not support Biometric Authentication", Toast.LENGTH_LONG).show();
                finish();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                Toast.makeText(getApplicationContext(), "Make sure you have Biometric Authentication setup", Toast.LENGTH_LONG).show();
                finish();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getApplicationContext(), "Please Add a Biometric Authentication and re-run the app", Toast.LENGTH_LONG).show();
                Log.e("MY_APP_TAG", "The user hasn't associated " +
                        "any biometric credentials with their account.");
                finish();
                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        start_stop();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                finish();
//            }
//        },2000);

        if (isMyServiceRunning(FloatingWindow.class)){
            started = true;
        }

    }

    public void start_stop() {
        if (checkPermission()) {
            if(!isMyServiceRunning(FloatingWindow.class)) {
                startService(new Intent(MainActivity.this, FloatingWindow.class));
                if(alert!=null&& alert.isShowing()) {
                    alert.dismiss();
                }
                finish();
                started = true;
            }
            else {
                finish();
            }
        }else {
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
                finish();
            }
        }
    }


    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                reqPermission();
                return false;
            }
            else {
                return true;
            }
        }else{
            return true;
        }

    }

    private void reqPermission() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Screen overlay detected");
        alertBuilder.setMessage("Enable 'Draw over other apps' in your system setting.");
        alertBuilder.setPositiveButton("OPEN SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    Intent intent;
                    intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
//                    startActivityForResult(intent,RESULT_OK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        alert = alertBuilder.create();
        alert.show();

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        alert.dismiss();
    }

    private boolean isMyServiceRunning(@SuppressWarnings("SameParameterValue") Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if(manager == null) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
