package com.maksat.participantapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;

import com.maksat.participantapp.models.TokenBody;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OnboardingActivity extends AppCompatActivity {
    public static final int STARTUP_DELAY = 300;
    public static final int ANIM_ITEM_DURATION = 1000;
    public static final int ITEM_DELAY = 300;

    private boolean animationStarted = false;
    private SharedPreferences preferences;
    private com.maksat.participantapp.OnboardingActivity activity;
    private Handler handler;
    private Runnable runnable= this::changeActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        preferences = this.getSharedPreferences("event", Context.MODE_PRIVATE);
        setTheme(R.style.Theme_ParticipantApp);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        activity = this;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (!hasFocus || animationStarted) {
            return;
        }

        animate();

        super.onWindowFocusChanged(hasFocus);
    }

    private void animate() {
        Requests preloadInfo = new Requests(activity);
        preloadInfo.getDictionary();
        ImageView logoImageView = findViewById(R.id.img_logo);

        ViewCompat.animate(logoImageView)
                .translationY(-150).alpha(1)
                .setStartDelay(STARTUP_DELAY)
                .setDuration(ANIM_ITEM_DURATION).setInterpolator(
                new DecelerateInterpolator(1.2f)).start();

        TextView v = findViewById(R.id.tt_tv);
        ViewPropertyAnimatorCompat viewAnimator;
        viewAnimator = ViewCompat.animate(v)
                .translationY(-50).alpha(1)
                .setStartDelay(ITEM_DELAY + 500)
                .setDuration(600);
        viewAnimator.setInterpolator(new DecelerateInterpolator()).start();
        if (!preferences.getString("tokenToken", "").equals("")) {
            Set<String> set = new HashSet<>(preferences.getStringSet("tokenRoles", new HashSet<>()));
            List<String> roles = new ArrayList<>(set);
            Variables.token = new TokenBody(preferences.getInt("tokenPId", 0),
                    preferences.getInt("tokenEId", 0), preferences.getString("tokenToken", ""),
                    preferences.getString("tokenToken", ""), roles, preferences.getInt("tokenLID", 0));
            /*preloadInfo.getProfile();
            preloadInfo.setOnProfileLoadedListener(this::changeActivity);*/
        } else {
            Variables.token = null;
        }
        handler = new Handler();
        handler.postDelayed(runnable, 2000);

    }

    private void changeActivity(){
        Intent intent=new Intent(com.maksat.participantapp.OnboardingActivity.this, MainActivity.class);
        startActivity(intent);
        ActivityCompat.finishAffinity(com.maksat.participantapp.OnboardingActivity.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (handler!=null){
            handler.removeCallbacks(runnable);
        }
        finish();
    }
}
