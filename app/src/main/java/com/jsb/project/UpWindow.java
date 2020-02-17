package com.jsb.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class UpWindow extends AppCompatActivity {

    private KeyStore keyStore;
    private static final String KEY_NAME = "wasi";
    private Cipher cipher;
    TextView textView;
    View mDialog;
    View mCncDialog;
    WindowManager wm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_window);

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        textView=(TextView)findViewById(R.id.textView);


        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        mDialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupwindow, null, false);
        mCncDialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupcancle,null, false);

//        final Executor executor = Executors.newSingleThreadExecutor();
//        final BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this)
//                .setTitle("Fingerprint Authentication")
//                .setNegativeButton("cancel", executor, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).build();

        if(!fingerprintManager.isHardwareDetected()){

            textView.setText("Your Device does not have a Fingerprint Sensor");
        }else {
            // Checks whether fingerprint permission is set on manifest
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                textView.setText("Fingerprint authentication permission not enabled");
            }else{
                // Check whether at least one fingerprint is registered
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    textView.setText("Register at least one fingerprint in Settings");
                }else{
                    // Checks whether lock screen security is enabled or not
                    if (!keyguardManager.isKeyguardSecure()) {
                        textView.setText("Lock screen security not enabled in Settings");
                    }else{
                        generateKey();


                        if (cipherInit()) {
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            FingerprintHandler helper = new FingerprintHandler(this);
                            helper.startAuth(fingerprintManager, cryptoObject);
                        }
                    }
                }
            }
        }

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
                finish();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeView(mCncDialog);
//                Intent intent = new Intent(UpWindow.this, FingerprintActivity.class);
//                startActivity(intent);
//                finish();
                wm.addView(mDialog, params1);



//                ImageView fingerPrint;
//                fingerPrint = (ImageView) mDialog.findViewById(R.id.finger);
//                fingerPrint.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
//                            @Override
//                            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
//                                super.onAuthenticationSucceeded(result);
//                                Intent intent = new Intent(UpWindow.this, FloatingWindow.class);
//                                startActivity(intent);

                   //         }
//                        });

  //                  }

    //            });


            }
        });

    }


    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }


        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }


        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

}
