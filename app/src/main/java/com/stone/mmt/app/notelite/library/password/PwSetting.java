package com.stone.mmt.app.notelite.library.password;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.stone.mmt.app.notelite.R;
import com.stone.mmt.app.notelite.library.CheckInternet;
import com.stone.mmt.app.notelite.library.ToastMsg;

public class PwSetting extends AppCompatActivity implements View.OnClickListener {
    private EditText etOldPw,etNewPw,etConfirmPw;
    private String oldPwPreference;
    private SharedPreferences sharedPreferences;
    public static final String PW_PREFERENCE = "PW";
    private RewardedAd rewardedAd;
    //private static final String TAG = "MainLog";
    private CheckInternet checkInternet;
    private TextView tvForgot;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pw_holder);
        etOldPw = findViewById(R.id.etOldPw);
        etNewPw = findViewById(R.id.etNewPw);
        etConfirmPw = findViewById(R.id.etConfirmPw);
        CardView etOldCv = findViewById(R.id.etOldCv);
        tvForgot = findViewById(R.id.tvForgot);
        Button btnSavePw = findViewById(R.id.btnSavePw);
        Button btnDeletePw = findViewById(R.id.btnDeletePw);

        btnSavePw.setOnClickListener(this);
        btnDeletePw.setOnClickListener(this);
        tvForgot.setOnClickListener(this);
        checkInternet = new CheckInternet(PwSetting.this);

        MobileAds.initialize(PwSetting.this, initializationStatus -> {});


        sharedPreferences = getSharedPreferences(PW_PREFERENCE,MODE_PRIVATE);
        oldPwPreference = sharedPreferences.getString(PW_PREFERENCE,"");

        if (oldPwPreference.isEmpty()) {
            etOldCv.setVisibility(View.GONE);
            tvForgot.setVisibility(View.GONE);
            btnDeletePw.setVisibility(View.GONE);
        } else {
            etOldCv.setVisibility(View.VISIBLE);
            tvForgot.setVisibility(View.VISIBLE);
            btnDeletePw.setVisibility(View.VISIBLE);
        }
    }
    private void showRewardAds() {
        if (rewardedAd != null) {
            Activity activityContext = PwSetting.this;
            rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                    //Log.d(TAG, "onAdClicked");
                }
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.

                     //Log.d(TAG, "onAdDismissedFullScreenContent");
                     rewardedAd = null;
                    ToastMsg.toastMsg(PwSetting.this,"Thanks for supporting ❤");
                }
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    // Called when ad fails to show.
                    //Log.e(TAG, "onAdFailedToShowFullScreenContent");
                    rewardedAd = null;
                }
                @Override
                public void onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    //Log.d(TAG, "Ad recorded an impression.");
                }
                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    //Log.d(TAG, "Ad showed fullscreen content.");
                }
            });
            rewardedAd.show(activityContext, rewardItem -> {
                // Handle the reward.
                //Log.d(TAG, "The user earned the reward.");
                etOldPw.setText(String.valueOf(oldPwPreference));
            });
        } else {
            //Log.d(TAG, "Reward null");
            ToastMsg.toastMsg(PwSetting.this,"Something wrong, try again");
            tvForgot.setEnabled(true);
        }
    }

    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        //Log.d(TAG, "onAdFailedToLoad");
                        rewardedAd = null;
                        ToastMsg.toastMsg(PwSetting.this,"Something wrong, try again");
                        tvForgot.setEnabled(true);
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        //Log.d(TAG, "onAdLoaded");
                        showRewardAds();
                        tvForgot.setEnabled(true);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String oldPwData = etOldPw.getText().toString();
        String newPwData = etNewPw.getText().toString();
        String newConPwData = etConfirmPw.getText().toString();
        if (id == R.id.btnSavePw) {
            if (oldPwData.contentEquals(oldPwPreference)) {
                if (newPwData.isEmpty()) {
                    ToastMsg.toastMsg(PwSetting.this,"Enter your new password");
                } else {
                    if (newPwData.contentEquals(newConPwData)) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(PW_PREFERENCE, newConPwData);
                        editor.apply();
                        ToastMsg.toastMsg(PwSetting.this,"Password added");
                        finish();
                    } else {
                        ToastMsg.toastMsg(PwSetting.this,"Passwords don't match, try again.");
                    }
                }
            } else {
                ToastMsg.toastMsg(PwSetting.this, "Enter your correct old password");
            }
        } else if (id == R.id.btnDeletePw) {
            if (oldPwData.isEmpty()) {
                ToastMsg.toastMsg(PwSetting.this,"Enter your correct old password");
            } else {
                if (oldPwData.contentEquals(oldPwPreference)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(PW_PREFERENCE,"");
                    editor.apply();
                    ToastMsg.toastMsg(PwSetting.this, "Password deleted");
                    finish();
                } else {
                    ToastMsg.toastMsg(PwSetting.this, "Enter your correct old password");
                }
            }
        } else if (id == R.id.tvForgot) {
            tvForgot.setEnabled(false);
            if (checkInternet.isNetworkAvailable()) {
                ToastMsg.toastMsg(PwSetting.this,"Wait a few second");
                //Log.d(TAG, "Online");
                loadAd();
            } else {
                ToastMsg.toastMsg(PwSetting.this,"No internet Connection");
                //Log.d(TAG, "Offline");
                tvForgot.setEnabled(true);
            }
        }
    }
}
