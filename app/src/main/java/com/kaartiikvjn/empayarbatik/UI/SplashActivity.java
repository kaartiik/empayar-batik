package com.kaartiikvjn.empayarbatik.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.kaartiikvjn.empayarbatik.R;
import com.kaartiikvjn.empayarbatik.utils.BaseActivity;
import com.kaartiikvjn.empayarbatik.utils.Constants;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (getAuth().getCurrentUser() == null)
            {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }else {
                if (getPreferenceString(Constants.userEmail).equals(Constants.adminMail)){
                    startActivity(new Intent(SplashActivity.this, AddItem.class));
                }else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
            }
            finish();
        },1500);
    }
}