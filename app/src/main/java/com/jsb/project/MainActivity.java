package com.jsb.project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        AlarmManagerCompat alarmManagerCompat = getSystemService(AlarmManagerCompat.class);
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
//        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//        filter.addAction(Intent.ACTION_POWER_CONNECTED);
//        filter.addAction(Intent.ACTION_SCREEN_ON);
//
//        receiver = new ServiceRestartReceiver();
//        registerReceiver(receiver, filter);

//        KeyguardManager manager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//
//        if(manager != null && manager.isKeyguardSecure()) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
//        }
//        else {
//            Toast.makeText(getApplicationContext(), secu)
//        }

//        BiometricManager biometricManager = BiometricManager.from(getApplicationContext());
//        switch (biometricManager.canAuthenticate()) {
//            case BiometricManager.BIOMETRIC_SUCCESS:
//                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
////                Intent intent = new Intent(getApplicationContext(), BackgroundService.class);
////                startService(intent);
//
//                break;
//            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
//                Log.e("MY_APP_TAG", "No biometric features available on this device.");
//                Toast.makeText(getApplicationContext(), "This Device Does not support Biometric Authentication", Toast.LENGTH_LONG).show();
////                finish();
//                break;
//            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
//                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
//                Toast.makeText(getApplicationContext(), "Make sure you have Biometric Authentication setup", Toast.LENGTH_LONG).show();
//                finish();
//                break;
//            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
//                Toast.makeText(getApplicationContext(), "Please Add a Biometric Authentication and re-run the app", Toast.LENGTH_LONG).show();
//                Log.e("MY_APP_TAG", "The user hasn't associated " +
//                        "any biometric credentials with their account.");
//                finish();
//                break;
//        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
