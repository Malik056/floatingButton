package com.jsb.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.biometric.BiometricPrompt.PromptInfo;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class BiometricAuthActivity extends FragmentActivity {


    private final String KEY_NAME = "Key_For_Authentication";
   WindowManager wm;
    KeyStore keyStore = null;
    View mDialog;
    View mCncDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric_auth);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final FrameLayout root = findViewById(R.id.root);


        final FrameLayout.LayoutParams para1;
        final FrameLayout.LayoutParams para2;

        DisplayMetrics dm2 = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm2);

        para1 = new FrameLayout.LayoutParams((Double.valueOf( dm2.widthPixels * 0.80)).intValue(),(Double.valueOf( dm2.heightPixels * 0.60)).intValue(),Gravity.CENTER);
        para2 = new FrameLayout.LayoutParams((Double.valueOf( dm2.widthPixels * 0.75)).intValue(),(Double.valueOf( dm2.heightPixels * 0.50)).intValue(),Gravity.CENTER);


        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

            {

                mDialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupwindow, null, false);
                mCncDialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupcancle, null, false);
                root.addView(mCncDialog,para2);



                TextView btncn;
                Button cancel;
                btncn = mCncDialog.findViewById(R.id.cancel1);
                cancel = mCncDialog.findViewById(R.id.cancelbtn);
                btncn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        wm.removeView(mCncDialog);
                        root.removeView(mCncDialog);
                        finish();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
//                        wm.removeView(mCncDialog);
                        root.removeView(mCncDialog);

                        root.addView(mDialog,para1);

                        /////////////////////////////
                        //***************************
                        //FingerPrint Detection Block
                        //***************************
                        /////////////////////////////
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
                            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
                            TextView textView = mDialog.findViewById(R.id.textView);


                            // Check whether the device has a Fingerprint sensor.
                            if (fingerprintManager != null) {
                                if (!fingerprintManager.isHardwareDetected()) {

                                    textView.setText("Your Device does not have a Fingerprint Sensor");
                                } else {
                                    // Checks whether fingerprint permission is set on manifest
                                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                                        textView.setText("Fingerprint authentication permission not enabled");
                                    } else {
                                        // Check whether at least one fingerprint is registered
                                        if (!fingerprintManager.hasEnrolledFingerprints()) {
                                            textView.setText("Register at least one fingerprint in Settings");
                                        } else {
                                            // Checks whether lock screen security is enabled or not
                                            if (keyguardManager != null) {
                                                if (!keyguardManager.isKeyguardSecure()) {
                                                    textView.setText("Lock screen security not enabled in Settings");
                                                } else {
                                                    generateKey();
                                                    if (cipherInit()) {
                                                        FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                                        FingerprintHandler helper = new FingerprintHandler(BiometricAuthActivity.this);
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
                            btnX = mDialog.findViewById(R.id.cancel);
                            btnX.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    root.removeView(mDialog);
                                    finish();
                                }
                            });
                        }

                    }
                });
            }

//            finish();
        }
        else {

            final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            final View mDialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupwindow, null, false);
            final View mCncDialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupcancle,null, false);


            final DisplayMetrics dm = new DisplayMetrics();
            assert wm != null;
            wm.getDefaultDisplay().getMetrics(dm);

            final WindowManager.LayoutParams params1;
            final WindowManager.LayoutParams params2;

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

                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wm.removeView(mCncDialog);
                 wm.addView(mDialog, params2);


                    Executor executor = ContextCompat.getMainExecutor(getApplicationContext());
                    BiometricPrompt biometricPrompt = new BiometricPrompt(BiometricAuthActivity.this,
                            executor, new BiometricPrompt.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationError(int errorCode,
                                                          @NonNull CharSequence errString) {
                            super.onAuthenticationError(errorCode, errString);
                            Toast.makeText(getApplicationContext(),
                                    "Authentication error: " + errString, Toast.LENGTH_SHORT)
                                    .show();
                        }

                        @Override
                        public void onAuthenticationSucceeded(
                                @NonNull BiometricPrompt.AuthenticationResult result) {
                            super.onAuthenticationSucceeded(result);
                            Toast.makeText(getApplicationContext(),
                                    "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), FloatingWindow.class);
                            startService(intent);
                            finish();
                        }

                        @Override
                        public void onAuthenticationFailed() {
                            super.onAuthenticationFailed();
                            Toast.makeText(getApplicationContext(), "Authentication failed",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });

                    PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                            .setTitle("Biometric login for my app")
                            .setSubtitle("Log in using your biometric credential")
                            .setNegativeButtonText("Use account Password")
                            .setDeviceCredentialAllowed(true)
                            .setConfirmationRequired(false)
                            .build();

                    biometricPrompt.authenticate(promptInfo);
                }
            });
        }

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
            } else {
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
