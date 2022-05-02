package com.arthitraders.driver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    ImageView logo;
    boolean login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        logo=findViewById(R.id.logo);
        logo.animate().scaleX(0.5f).scaleY(0.5f).setDuration(2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Helper.sharedpreferencesdriver = getSharedPreferences(Helper.MyPREFERENCES, Context.MODE_PRIVATE);
                login=Helper.sharedpreferencesdriver.getBoolean("FIRSTTIME_LOGIN",false);
                Intent intent;
                if (!login){
                    intent = new Intent(SplashScreen.this, LoginScreen.class);
                }else{
                    intent = new Intent(SplashScreen.this, HomeScreen.class);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                finish();
            }
        },3000);
    }
}