package com.kl.biometrics

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt

class MainActivity : AppCompatActivity(), BiometricAuthListener {

    private lateinit var buttonBiometricsLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonBiometricsLogin = findViewById(R.id.buttonBiometricsLogin)

        //button visibility
        showBiometricLoginOption()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasFingerprint = packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val hasFace = packageManager.hasSystemFeature(PackageManager.FEATURE_FACE)
            val hasIris = packageManager.hasSystemFeature(PackageManager.FEATURE_IRIS)
        }
    }


    fun onClickBiometrics(view: View) {
        BiometricKotlinUtil.showBiometricPrompt(
            activity = this,
            listener = this,
            cryptoObject = null,
            allowDeviceCredential = false
        )
    }

    override fun onBiometricAuthenticationSuccess(result: BiometricPrompt.AuthenticationResult) {
        Toast.makeText(this, "Biometric success", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onBiometricAuthenticationError(errorCode: Int, errorMessage: String) {
        Toast.makeText(this, "Biometric login. Error: $errorMessage", Toast.LENGTH_SHORT)
            .show()
    }

    private fun showBiometricLoginOption() {
        buttonBiometricsLogin.visibility =
            if (BiometricKotlinUtil.isBiometricReady(this)) View.VISIBLE
            else View.GONE
    }
}