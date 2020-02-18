package com.jsb.project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class UpWindow extends FragmentActivity {

    private static final String KEY_NAME = "MyKeyForAuthentication";
    View mDialog;
    View mCncDialog;
    WindowManager wm;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        {
//                                        View root = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_up_window, null, false);

            wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            mDialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupwindow, null, false);
            mCncDialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupcancle,null, false);


            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);

            final WindowManager.LayoutParams params1;
            final WindowManager.LayoutParams params2;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                params1 = new WindowManager.LayoutParams(
                        (Double.valueOf( dm.widthPixels * 0.80)).intValue(), (Double.valueOf( dm.heightPixels * 0.55)).intValue(),
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
                params2 = new WindowManager.LayoutParams(
                        (Double.valueOf( dm.widthPixels * 0.75)).intValue(), (Double.valueOf( dm.heightPixels * 0.50)).intValue(),
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
            }
            else {
                params1 = new WindowManager.LayoutParams(
                        (Double.valueOf( dm.widthPixels * 0.80)).intValue(), (Double.valueOf( dm.heightPixels * 0.55)).intValue(),
                        WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);

                params2 = new WindowManager.LayoutParams(
                        (Double.valueOf( dm.widthPixels * 0.75)).intValue(), (Double.valueOf( dm.heightPixels * 0.50)).intValue(),
                        WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
            }
            params1.gravity = Gravity.CENTER;
            DisplayMetrics dm1 = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm1);
            params2.gravity = Gravity.CENTER;



            wm.addView(mCncDialog, params2);
            TextView btncn;
            Button cancel;
            btncn= mCncDialog.findViewById(R.id.cancel1);
            cancel = mCncDialog.findViewById(R.id.cancelbtn);
            btncn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wm.removeView(mCncDialog);
                    finish();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    wm.removeView(mCncDialog);
//                                                Intent intent = new Intent(UpWindow.this, FingerprintActivity.class);
//                                                startActivity(intent);
                    wm.addView(mDialog, params2);

                    /////////////////////////////
                    //***************************
                    //FingerPrint Detection Block
                    //***************************
                    /////////////////////////////
                    if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                        final FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
                        TextView textView = mDialog.findViewById(R.id.textView);


                        // Check whether the device has a Fingerprint sensor.
                        if (fingerprintManager != null) {
                            if(!fingerprintManager.isHardwareDetected()){

                                textView.setText("Your Device does not have a Fingerprint Sensor");
                            }else {
                                // Checks whether fingerprint permission is set on manifest
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                                    textView.setText("Fingerprint authentication permission not enabled");
                                }else{
                                    // Check whether at least one fingerprint is registered
                                    if (!fingerprintManager.hasEnrolledFingerprints()) {
                                        textView.setText("Register at least one fingerprint in Settings");
                                    }else{
                                        // Checks whether lock screen security is enabled or not
                                        if (keyguardManager != null) {
                                            if (!keyguardManager.isKeyguardSecure()) {
                                                textView.setText("Lock screen security not enabled in Settings");
                                            }else{
                                                generateKey();
                                                if (cipherInit()) {
                                                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                                    FingerprintHandler helper = new FingerprintHandler(UpWindow.this);
                                                    helper.startAuth(fingerprintManager, cryptoObject);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //  wm.addView(ll, params1);

                        TextView btnX;
                        btnX= mDialog.findViewById(R.id.cancel);
                        btnX.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                wm.removeView(mDialog);
                                finish();
                            }
                        });
                    }
//                                                else {
//                                                    BiometricManager biometricManager = BiometricManager.from(getApplicationContext());
//                                                    switch (biometricManager.canAuthenticate()) {
//                                                        case BiometricManager.BIOMETRIC_SUCCESS:
//                                                            Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
//                                                            executor = ContextCompat.getMainExecutor(FloatingWindow.this);
//                                                            biometricPrompt = new BiometricPrompt(context,
//                                                                    executor, new BiometricPrompt.AuthenticationCallback() {
//                                                                @Override
//                                                                public void onAuthenticationError(int errorCode,
//                                                                                                  @NonNull CharSequence errString) {
//                                                                    super.onAuthenticationError(errorCode, errString);
//                                                                    Toast.makeText(getApplicationContext(),
//                                                                            "Authentication error: " + errString, Toast.LENGTH_SHORT)
//                                                                            .show();
//                                                                }
//
//                                                                @Override
//                                                                public void onAuthenticationSucceeded(
//                                                                        @NonNull BiometricPrompt.AuthenticationResult result) {
//                                                                    super.onAuthenticationSucceeded(result);
//                                                                    Toast.makeText(getApplicationContext(),
//                                                                            "Authentication succeeded!", Toast.LENGTH_SHORT).show();
//                                                                }
//
//                                                                @Override
//                                                                public void onAuthenticationFailed() {
//                                                                    super.onAuthenticationFailed();
//                                                                    Toast.makeText(getApplicationContext(), "Authentication failed",
//                                                                            Toast.LENGTH_SHORT)
//                                                                            .show();
//                                                                }
//                                                            });
//
//                                                            promptInfo = new BiometricPrompt.PromptInfo.Builder()
//                                                                    .setTitle("Biometric login for my app")
//                                                                    .setSubtitle("Log in using your biometric credential")
//                                                                    .setNegativeButtonText("Use account password")
//                                                                    .build();
//
//                                                            break;
//                                                        case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
//                                                            Log.e("MY_APP_TAG", "No biometric features available on this device.");
//                                                            break;
//                                                        case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
//                                                            Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
//                                                            break;
//                                                        case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
//                                                            Log.e("MY_APP_TAG", "The user hasn't associated " +
//                                                                    "any biometric credentials with their account.");
//                                                            break;
//                                                    }
//                                                }

                }
            });
        }


















//        setContentView(R.layout.activity_up_window);
//
//        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//        mDialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupwindow, null, false);
//        mCncDialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupcancle,null, false);
//
////        final Executor executor = Executors.newSingleThreadExecutor();
////        final BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(this)
////                .setTitle("Fingerprint Authentication")
////                .setNegativeButton("cancel", executor, new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////
////                    }
////                }).build();
//
//        DisplayMetrics dm = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(dm);
//
//        final WindowManager.LayoutParams params1;
//        final WindowManager.LayoutParams params2;
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            params1 = new WindowManager.LayoutParams(
//                    (Double.valueOf( dm.widthPixels * 0.80)).intValue(), (Double.valueOf( dm.heightPixels * 0.55)).intValue(),
//                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                    PixelFormat.TRANSLUCENT);
//            params2 = new WindowManager.LayoutParams(
//                    (Double.valueOf( dm.widthPixels * 0.75)).intValue(), (Double.valueOf( dm.heightPixels * 0.50)).intValue(),
//                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                    PixelFormat.TRANSLUCENT);
//        }
//        else {
//            params1 = new WindowManager.LayoutParams(
//                    (Double.valueOf( dm.widthPixels * 0.80)).intValue(), (Double.valueOf( dm.heightPixels * 0.55)).intValue(),
//                    WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW,
//                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                    PixelFormat.TRANSLUCENT);
//
//            params2 = new WindowManager.LayoutParams(
//                    (Double.valueOf( dm.widthPixels * 0.75)).intValue(), (Double.valueOf( dm.heightPixels * 0.50)).intValue(),
//                    WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW,
//                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                    PixelFormat.TRANSLUCENT);
//        }
//        params1.gravity = Gravity.CENTER;
//        DisplayMetrics dm1 = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(dm1);
//        params2.gravity = Gravity.CENTER;
//
//
//
//        wm.addView(mCncDialog, params2);
//        TextView btncn;
//        Button cancel;
//        btncn= mCncDialog.findViewById(R.id.cancel1);
//        cancel = mCncDialog.findViewById(R.id.cancelbtn);
//        btncn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                wm.removeView(mCncDialog);
//                finish();
//
//            }
//        });
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                wm.removeView(mCncDialog);
//                Intent intent = new Intent(UpWindow.this, FingerprintActivity.class);
//                startActivity(intent);
//                finish();
////                wm.addView(mDialog, params1);
////                ImageView fingerPrint;
////                fingerPrint = (ImageView) mDialog.findViewById(R.id.finger);
////                fingerPrint.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
////                            @Override
////                            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
////                                super.onAuthenticationSucceeded(result);
////                                Intent intent = new Intent(UpWindow.this, FloatingWindow.class);
////                                startActivity(intent);
//
//                   //         }
////                        });
//
//  //                  }
//
//    //            });
//
//
//            }
//        });

    }

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
            if (keyStore != null) {
                keyStore.load(null);
            }
            else {
                return;
            }
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
    private Cipher cipher;
    KeyStore keyStore = null;

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
