package com.jsb.project;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;

import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {


    private FragmentActivity context;
    private CancellationSignal cancellationSignal = new CancellationSignal();


    // Constructor
    public FingerprintHandler(FragmentActivity mContext) {
        context = mContext;
    }

    public void cancelAuth() {
        cancellationSignal.cancel();
    }


    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        cancellationSignal = new CancellationSignal();
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
//        this.update("Fingerprint Authentication error\n" + errString, false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString, false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.", false);
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Fingerprint Authentication succeeded.", true);
        Intent intent = new Intent(context, FloatingWindow.class);
        context.startService(intent);
        try {
            Intent sendMessage = new Intent();
            sendMessage.setAction(FloatingWindow.BUTTON_ENABLED);
            context.sendBroadcast(sendMessage);
            SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.shared_preferences_for_message_name), Context.MODE_PRIVATE).edit();
            editor.putString("message", "Universe Rescued");
            editor.putBoolean(context.getString(R.string.is_notification_active), false);
            editor.apply();
            clearNotification();
            context.finish();
        }
        catch (Exception ignored) {

        }
    }

    public void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }



    public void update(String e, Boolean success){
        TextView textView = ((FragmentActivity)context).findViewById(R.id.textView);
        textView.setText(e);
        if(success){
            textView.setTextColor(ContextCompat.getColor(context,R.color.green));
        }
        else {
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
    }
}
