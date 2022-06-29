package com.kl.biometrics;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;


public class BioMetricUtil {

    /**
     * Checks if the device has Biometric support
     */
    private Integer hasBiometricCapability(Context context) {
        BiometricManager biometricManager = BiometricManager.from(context);
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK);
    }


    /**
     * Checks if Biometric Authentication (example: Fingerprint) is set in the device
     */
    public Boolean isBiometricReady(Context context) {
        return hasBiometricCapability(context) == BiometricManager.BIOMETRIC_SUCCESS;
    }

    /**
     * Prepares PromptInfo dialog with provided configuration
     */

    private BiometricPrompt.PromptInfo setBiometricPromptInfo(
            String title,
            String subTitle,
            String description,
            Boolean allowDeviceCredential
    ) {
        BiometricPrompt.PromptInfo.Builder builder = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subTitle)
                .setDescription(description)
                .setConfirmationRequired(true);

        if(allowDeviceCredential) {
            builder.setDeviceCredentialAllowed(true);
        } else {
            builder.setNegativeButtonText("Cancel");
        }
        return builder.build();
    }


    /**
     * Initializes BiometricPrompt with the caller and callback handlers
     */

    private BiometricPrompt initBioMetricPrompt(AppCompatActivity activity, BiometricAuthListener listener) {
        Executor executor = ContextCompat.getMainExecutor(activity);
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                listener.onBiometricAuthenticationError(errorCode, errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                listener.onBiometricAuthenticationSuccess(result);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.w("BIOMETRIC PROMPT", "Authentication failed for an unknown reason");

            }
        };
        return new BiometricPrompt(activity, executor, callback);
    }

    /**
     * Displays a BiometricPrompt with provided configurations
     */

    public void showBioMetricPrompt(
            String title,
            String subtitle,
            String description,
            AppCompatActivity activity,
            BiometricAuthListener listener,
            Boolean allowDeviceCredential
    ) {
        BiometricPrompt.PromptInfo promptInfo = setBiometricPromptInfo(
                title,
                subtitle,
                description,
                allowDeviceCredential
        );

        BiometricPrompt biometricPrompt = initBioMetricPrompt(activity, listener);
        biometricPrompt.authenticate(promptInfo);
    }

    //with crypto option ( method overloading )
    public void showBioMetricPrompt(
            String title,
            String subtitle,
            String description,
            AppCompatActivity activity,
            BiometricAuthListener listener,
            BiometricPrompt.CryptoObject cryptoObject,
            Boolean allowDeviceCredential
    ) {
        BiometricPrompt.PromptInfo promptInfo = setBiometricPromptInfo(
                title,
                subtitle,
                description,
                allowDeviceCredential
        );

        BiometricPrompt biometricPrompt = initBioMetricPrompt(activity, listener);
        biometricPrompt.authenticate(promptInfo, cryptoObject);
    }



    /**
     * Navigates to Device's Settings screen Biometric Setup
     */
    public void launchBiometricSettings(Context context) {
        ActivityCompat.startActivity(
                context,
                new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS),
                null
        );
    }

    /**
     * returns a boolean based on FingerPrint unlock availability
     */
    public Boolean isFingerPrintAvailable(Context context) {
        boolean hasFingerprint = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasFingerprint = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT);
        }
        return hasFingerprint;
    }

    /**
     * returns a boolean based on FaceId unlock availability
     */
    public Boolean isFaceIdAvailable(Context context) {
        boolean hasFace = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            hasFace = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_FACE);
        }
        return hasFace;
    }
}

