package com.stone.mmt.app.notelite;

import static com.stone.mmt.app.notelite.library.password.PwSetting.PW_PREFERENCE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.stone.mmt.app.notelite.library.ToastMsg;

import java.util.concurrent.Executor;

public class Main extends AppCompatActivity {
    private String passwordData;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btnGo = findViewById(R.id.btnGo);
        EditText etGo = findViewById(R.id.etGo);
        LottieAnimationView btnFprint = findViewById(R.id.btnFprint);
        LinearLayout welcome_screen = findViewById(R.id.welcome_screen);
        RelativeLayout welcome_pw = findViewById(R.id.welcome_pw);

        SharedPreferences sharedPreferences = getSharedPreferences(PW_PREFERENCE, MODE_PRIVATE);
        passwordData = sharedPreferences.getString(PW_PREFERENCE,"");

        if (passwordData.isEmpty()) {
            welcome_pw.setVisibility(View.INVISIBLE);
            welcome_screen.setVisibility(View.VISIBLE);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                startActivity(new Intent(Main.this, Note_ls.class));
                finish();
            }, 700); // 700 ms delay
        } else {
            welcome_pw.setVisibility(View.VISIBLE);
            welcome_screen.setVisibility(View.INVISIBLE);
            showFpAndPwBox();
            checkAndroidVersionForBiometric();
        }
        btnFprint.setOnClickListener(v -> {
            //ToastMsg.toastMsg(Main.this,"FingerPrint");
            biometricPrompt.authenticate(promptInfo);
        });

        btnGo.setOnClickListener(v -> {
            if (v.getId() == R.id.btnGo) {
                String inputPassword = etGo.getText().toString();
                if (inputPassword.contentEquals(passwordData)) {
                    startActivity(new Intent(Main.this,Note_ls.class));
                    finish();
                } else {
                    ToastMsg.toastMsg(Main.this,"Enter your correct password");
                }
            }
        });
    }
    private void showFpAndPwBox() {
        Executor executor = ContextCompat.getMainExecutor(Main.this);
        biometricPrompt = new BiometricPrompt(Main.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                //Log.d("MainFp","onAuthenticationError "+errorCode);
                //Log.d("MainFp","onAuthenticationError "+errString);
                if (errorCode == 11) {
                    ToastMsg.toastMsg(Main.this,"Password not added in device setting");
                }
                //ToastMsg.toastMsg(Main.this,"Unregistered fingerprint detected");
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //Log.d("MainFp","onAuthenticationSucceeded ");
                startActivity(new Intent(Main.this,Note_ls.class));
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //Log.d("MainFp","onAuthenticationFailed ");
            }
        });
    }
    private void checkAndroidVersionForBiometric() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            int allowedAuthenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL;
            promptInfo = new BiometricPrompt.PromptInfo
                    .Builder()
                    .setTitle("Note Lite")
                    .setSubtitle("Enter password or use fingerprint to unlock")
                    .setAllowedAuthenticators(allowedAuthenticators)
                    .build();
        } else {
            promptInfo = new BiometricPrompt.PromptInfo
                    .Builder()
                    .setTitle("Note Lite")
                    .setSubtitle("Enter password or use fingerprint to unlock")
                    .setDeviceCredentialAllowed(true)
                    .build();

        }
    }
}
