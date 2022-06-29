package com.kl.biometrics;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

public class JavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);

        BioMetricUtil bioMetricUtil = new BioMetricUtil();
        bioMetricUtil.showBioMetricPrompt("", "", "", this, new BiometricAuthListener() {
            @Override
            public void onBiometricAuthenticationSuccess(@NonNull BiometricPrompt.AuthenticationResult result) {

            }

            @Override
            public void onBiometricAuthenticationError(int errorCode, @NonNull String errorMessage) {

            }
        }, false);
    }
}