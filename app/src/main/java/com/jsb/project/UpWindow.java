package com.jsb.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UpWindow extends AppCompatActivity {

    View mDialog;
    View mCncDialog;
    WindowManager wm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_window);

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        mDialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupwindow, null, false);
        mCncDialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupcancle,null, false);

        final Executor executor = Executors.newSingleThreadExecutor();
        final BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this)
                .setTitle("Fingerprint Authentication")
                .setNegativeButton("cancel", executor, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).build();

        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        final WindowManager.LayoutParams params1 = new WindowManager.LayoutParams(
                (Double.valueOf( dm.widthPixels * 0.80)).intValue(), (Double.valueOf( dm.heightPixels * 0.55)).intValue(),
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params1.gravity = Gravity.CENTER;
        DisplayMetrics dm1 = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm1);
        final WindowManager.LayoutParams params2 = new WindowManager.LayoutParams(
                (Double.valueOf( dm.widthPixels * 0.75)).intValue(), (Double.valueOf( dm.heightPixels * 0.50)).intValue(),
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params1.gravity = Gravity.CENTER;



        wm.addView(mCncDialog, params2);
        TextView btncn;
        Button cancel;
        btncn=(TextView) mCncDialog.findViewById(R.id.cancel1);
        cancel = (Button) mCncDialog.findViewById(R.id.cancelbtn);
        btncn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeView(mCncDialog);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeView(mCncDialog);
                wm.addView(mDialog, params1);
                ImageView fingerPrint;
                fingerPrint = (ImageView) mDialog.findViewById(R.id.finger);
                fingerPrint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
                            @Override
                            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                                super.onAuthenticationSucceeded(result);
                                Intent intent = new Intent(UpWindow.this, FloatingWindow.class);
                                startActivity(intent);

                            }
                        });

                    }

                });


            }
        });
        TextView btnX;
        btnX=(TextView) mDialog.findViewById(R.id.cancel);
        btnX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeView(mDialog);

            }
        });
    }
}
