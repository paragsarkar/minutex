package com.minutex.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.minutex.MainActivity;
import com.minutex.R;
import com.minutex.common.util.AndroidUtil;

/**
 * Created by LENOVO PC on 07-07-2016.
 */
public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT =3000;

    private View view;
    private ImageView iv_splash;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater)SplashActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.splashscreen,null);
        setContentView(view);
        checkNetwork();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        },SPLASH_TIME_OUT);
    }
    private void checkNetwork()
    {
        if(!AndroidUtil.isConnected(SplashActivity.this))
        {
            showSnackbar("Network is not available");
        }
    }
    private void showSnackbar(String string)
    {
        Snackbar.make(view,string,Snackbar.LENGTH_INDEFINITE).show();
    }
}
