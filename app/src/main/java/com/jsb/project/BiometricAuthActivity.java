package com.jsb.project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricPrompt;
import androidx.biometric.BiometricPrompt.PromptInfo;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import java.util.concurrent.Executor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

@SuppressWarnings("deprecation")
public class BiometricAuthActivity extends FragmentActivity {


    private final String KEY_NAME = "Key_For_Authentication";
    WindowManager wm;
    KeyStore keyStore = null;
    View mDialog;
    View mCncDialog;
    private int INTENT_AUTHENTICATE = 3039;
    private FingerprintHandler helper = new FingerprintHandler(BiometricAuthActivity.this);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric_auth);
    }

    @SuppressLint("InflateParams")
    @Override
    protected void onStart() {
        super.onStart();
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final FrameLayout root = findViewById(R.id.root);


        final FrameLayout.LayoutParams para1;
        final FrameLayout.LayoutParams para2;

        DisplayMetrics dm2 = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm2);

        para1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        para2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        para1.setMargins(40, 40, 40, 40);
        para2.setMargins(40, 40, 40, 40);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

            {

                mDialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupwindow, null, false);
                mCncDialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupcancle, null, false);
                root.addView(mCncDialog, para2);


                ImageView btnCancel;
                Button cancel;
                btnCancel = mCncDialog.findViewById(R.id.cancel1);
                cancel = mCncDialog.findViewById(R.id.cancelbtn);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        root.removeView(mCncDialog);
                        finish();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @SuppressWarnings("deprecation")
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
//                        wm.removeView(mCncDialog);
                        root.removeView(mCncDialog);

                        root.addView(mDialog, para1);

                        /////////////////////////////
                        //***************************
                        //FingerPrint Detection Block
                        //***************************
                        /////////////////////////////
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            final KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
                            TextView textView = mDialog.findViewById(R.id.textView);

                            TextView pin = mDialog.findViewById(R.id.pin);
                            TextView password = mDialog.findViewById(R.id.password);

                            pin.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    KeyguardManager km = keyguardManager;
//
//                                    if (km != null && km.isKeyguardSecure()) {
//                                        Intent authIntent = km.createConfirmDeviceCredentialIntent("Enter your passwrod", "");
//                                        startActivityForResult(authIntent, INTENT_AUTHENTICATE);
//                                    }
                                    Intent intent = new Intent(Intent.ACTION_RUN);
                                    intent.setComponent(new ComponentName("com.android.settings",
                                            "com.android.settings.ConfirmLockPassword"));
                                    startActivityForResult(intent, INTENT_AUTHENTICATE);
                                }
                            });

                            password.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_RUN);
                                    intent.setComponent(new ComponentName("com.android.settings",
                                            "com.android.settings.ConfirmLockPassword"));
                                    startActivityForResult(intent, INTENT_AUTHENTICATE);
                                }
                            });

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
                                            Intent intent = new Intent(Intent.ACTION_RUN);
                                            intent.setComponent(new ComponentName("com.android.settings",
                                                    "com.android.settings.ConfirmLockPassword"));
                                            startActivityForResult(intent, INTENT_AUTHENTICATE);

                                        } else {
                                            // Checks whether lock screen security is enabled or not
                                            if (keyguardManager != null) {
                                                if (!keyguardManager.isKeyguardSecure()) {
                                                    textView.setText("Lock screen security not enabled in Settings");
                                                } else {
                                                    generateKey();
                                                    if (cipherInit()) {
                                                        FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                                        helper.startAuth(fingerprintManager, cryptoObject);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                ImageView btnX;
                                btnX = mDialog.findViewById(R.id.cancel);
                                btnX.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        root.removeView(mDialog);
                                        finish();
                                    }
                                });
                            } else {
                                Intent intent = new Intent(Intent.ACTION_RUN);
                                intent.setComponent(new ComponentName("com.android.settings",
                                        "com.android.settings.ConfirmLockPassword"));
                                startActivityForResult(intent, INTENT_AUTHENTICATE);
                            }
                            //  wm.addView(ll, params1);


                        } else {
                            Intent intent = new Intent(Intent.ACTION_RUN);
                            intent.setComponent(new ComponentName("com.android.settings",
                                    "com.android.settings.ConfirmLockPassword"));
                            startActivityForResult(intent, INTENT_AUTHENTICATE);
                        }
                        //  wm.addView(ll, params1);


                    }
                });
            }

        } else {

            @SuppressLint("InflateParams") final View mCncDialog = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupcancle, null, false);
            root.addView(mCncDialog, para2);
            ImageView btnCancel;
            Button cancel;
            btnCancel = mCncDialog.findViewById(R.id.cancel1);
            cancel = mCncDialog.findViewById(R.id.cancelbtn);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    root.removeView(mCncDialog);
                    finish();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    root.removeView(mCncDialog);

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
                            finish();
                        }

                        @Override
                        public void onAuthenticationSucceeded(
                                @NonNull BiometricPrompt.AuthenticationResult result) {
                            super.onAuthenticationSucceeded(result);
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


    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_AUTHENTICATE) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(getApplicationContext(), FloatingWindow.class);
                startService(intent);
                finish();
            }
//            else {
//
//                final KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//                FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
//                TextView textView = mDialog.findViewById(R.id.textView);
//                if (fingerprintManager != null) {
//                    if (!fingerprintManager.isHardwareDetected()) {
//
//                        textView.setText("Your Device does not have a Fingerprint Sensor");
//                    } else {
//                        // Checks whether fingerprint permission is set on manifest
//                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
//                            textView.setText("Fingerprint authentication permission not enabled");
//                        } else {
//                            // Check whether at least one fingerprint is registered
//                            if (!fingerprintManager.hasEnrolledFingerprints()) {
//                                textView.setText("Register at least one fingerprint in Settings");
//                            } else {
//                                // Checks whether lock screen security is enabled or not
//                                if (keyguardManager != null) {
//                                    if (!keyguardManager.isKeyguardSecure()) {
//                                        textView.setText("Lock screen security not enabled in Settings");
//                                    } else {
//                                        generateKey();
//                                        if (cipherInit()) {
//                                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
//                                            helper.startAuth(fingerprintManager, cryptoObject);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        helper.cancelAuth();
    }

    @Override
    protected void onStop() {
        super.onStop();
        helper.cancelAuth();
    }
}
